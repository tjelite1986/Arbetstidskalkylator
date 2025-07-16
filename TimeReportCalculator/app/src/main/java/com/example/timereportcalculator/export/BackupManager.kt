package com.example.timereportcalculator.export

import android.content.Context
import android.content.SharedPreferences
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

data class BackupMetadata(
    val fileName: String,
    val createdAt: LocalDateTime,
    val entryCount: Int,
    val isAutomatic: Boolean,
    val description: String? = null
)

class BackupManager(private val context: Context) {
    
    private val fileManager = FileManager(context)
    private val prefs: SharedPreferences = context.getSharedPreferences("backup_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_AUTO_BACKUP_ENABLED = "auto_backup_enabled"
        private const val KEY_BACKUP_INTERVAL_HOURS = "backup_interval_hours"
        private const val KEY_LAST_AUTO_BACKUP = "last_auto_backup"
        private const val KEY_MAX_BACKUPS = "max_backups"
        private const val KEY_BACKUP_ON_CHANGES = "backup_on_changes"
        
        private const val DEFAULT_BACKUP_INTERVAL_HOURS = 24
        private const val DEFAULT_MAX_BACKUPS = 10
    }
    
    // Settings
    var isAutoBackupEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_BACKUP_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_BACKUP_ENABLED, value).apply()
    
    var backupIntervalHours: Int
        get() = prefs.getInt(KEY_BACKUP_INTERVAL_HOURS, DEFAULT_BACKUP_INTERVAL_HOURS)
        set(value) = prefs.edit().putInt(KEY_BACKUP_INTERVAL_HOURS, value).apply()
    
    var maxBackups: Int
        get() = prefs.getInt(KEY_MAX_BACKUPS, DEFAULT_MAX_BACKUPS)
        set(value) = prefs.edit().putInt(KEY_MAX_BACKUPS, value).apply()
    
    var backupOnChanges: Boolean
        get() = prefs.getBoolean(KEY_BACKUP_ON_CHANGES, true)
        set(value) = prefs.edit().putBoolean(KEY_BACKUP_ON_CHANGES, value).apply()
    
    private var lastAutoBackup: LocalDateTime
        get() {
            val timestamp = prefs.getLong(KEY_LAST_AUTO_BACKUP, 0)
            return if (timestamp > 0) {
                LocalDateTime.parse(
                    java.time.Instant.ofEpochMilli(timestamp).toString(),
                    DateTimeFormatter.ISO_INSTANT
                )
            } else {
                LocalDateTime.MIN
            }
        }
        set(value) {
            val timestamp = java.time.ZoneId.systemDefault().rules.getOffset(value).totalSeconds * 1000L
            prefs.edit().putLong(KEY_LAST_AUTO_BACKUP, timestamp).apply()
        }
    
    suspend fun createBackup(
        entries: List<TimeEntry>,
        settings: Settings,
        isAutomatic: Boolean = false,
        description: String? = null
    ): Result<BackupMetadata> = withContext(Dispatchers.IO) {
        try {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
            val prefix = if (isAutomatic) "auto_backup" else "manual_backup"
            val fileName = "${prefix}_$timestamp.json"
            
            val result = fileManager.saveToJson(entries, settings)
            if (result.isSuccess) {
                val metadata = BackupMetadata(
                    fileName = result.getOrNull() ?: fileName,
                    createdAt = LocalDateTime.now(),
                    entryCount = entries.size,
                    isAutomatic = isAutomatic,
                    description = description
                )
                
                if (isAutomatic) {
                    lastAutoBackup = LocalDateTime.now()
                }
                
                // Clean up old backups if needed
                cleanupOldBackups()
                
                Result.success(metadata)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Backup failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun shouldCreateAutoBackup(): Boolean = withContext(Dispatchers.IO) {
        if (!isAutoBackupEnabled) return@withContext false
        
        val now = LocalDateTime.now()
        val hoursSinceLastBackup = java.time.Duration.between(lastAutoBackup, now).toHours()
        
        return@withContext hoursSinceLastBackup >= backupIntervalHours
    }
    
    suspend fun getBackupMetadata(): List<BackupMetadata> = withContext(Dispatchers.IO) {
        try {
            val backupDir = File(context.filesDir, "timereports")
            if (!backupDir.exists()) return@withContext emptyList()
            
            backupDir.listFiles()
                ?.filter { it.extension == "json" }
                ?.mapNotNull { file ->
                    try {
                        val fileName = file.name
                        val isAutomatic = fileName.startsWith("auto_backup")
                        
                        // Parse timestamp from filename
                        val timestampPattern = """(\d{4}-\d{2}-\d{2}_\d{2}-\d{2})""".toRegex()
                        val timestampMatch = timestampPattern.find(fileName)
                        val createdAt = timestampMatch?.let { match ->
                            try {
                                LocalDateTime.parse(match.value, DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
                            } catch (e: Exception) {
                                LocalDateTime.now()
                            }
                        } ?: LocalDateTime.now()
                        
                        // Try to load file to get entry count
                        val entryCount = try {
                            val loadResult = fileManager.loadFromJson(fileName)
                            loadResult.getOrNull()?.first?.size ?: 0
                        } catch (e: Exception) {
                            0
                        }
                        
                        BackupMetadata(
                            fileName = fileName,
                            createdAt = createdAt,
                            entryCount = entryCount,
                            isAutomatic = isAutomatic,
                            description = if (isAutomatic) "Automatisk backup" else "Manuell backup"
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                ?.sortedByDescending { it.createdAt }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private suspend fun cleanupOldBackups() = withContext(Dispatchers.IO) {
        try {
            val allBackups = getBackupMetadata()
            
            // Keep only the most recent backups (maxBackups)
            val backupsToDelete = allBackups.drop(maxBackups)
            
            backupsToDelete.forEach { backup ->
                try {
                    fileManager.deleteSavedFile(backup.fileName)
                } catch (e: Exception) {
                    // Continue even if deletion fails
                }
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }
    
    suspend fun deleteBackup(fileName: String): Boolean = withContext(Dispatchers.IO) {
        fileManager.deleteSavedFile(fileName)
    }
    
    suspend fun loadBackup(fileName: String): Result<Pair<List<TimeEntry>, Settings>> = withContext(Dispatchers.IO) {
        fileManager.loadFromJson(fileName)
    }
    
    fun getBackupSettings(): BackupSettings {
        return BackupSettings(
            isAutoBackupEnabled = isAutoBackupEnabled,
            backupIntervalHours = backupIntervalHours,
            maxBackups = maxBackups,
            backupOnChanges = backupOnChanges
        )
    }
    
    fun updateBackupSettings(settings: BackupSettings) {
        isAutoBackupEnabled = settings.isAutoBackupEnabled
        backupIntervalHours = settings.backupIntervalHours
        maxBackups = settings.maxBackups
        backupOnChanges = settings.backupOnChanges
    }
}

data class BackupSettings(
    val isAutoBackupEnabled: Boolean = false,
    val backupIntervalHours: Int = 24,
    val maxBackups: Int = 10,
    val backupOnChanges: Boolean = true
)