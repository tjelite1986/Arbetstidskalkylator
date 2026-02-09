package com.example.timereportcalculator.export

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GoogleDriveBackupManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("google_drive_backup_prefs", Context.MODE_PRIVATE)
    private val fileManager = FileManager(context)
    
    companion object {
        private const val KEY_GOOGLE_DRIVE_ENABLED = "google_drive_enabled"
        private const val KEY_LAST_DRIVE_BACKUP = "last_drive_backup"
        private const val APP_FOLDER_NAME = "TimeReportCalculator_Backups"
    }
    
    var isGoogleDriveEnabled: Boolean
        get() = prefs.getBoolean(KEY_GOOGLE_DRIVE_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_GOOGLE_DRIVE_ENABLED, value).apply()
    
    private var lastDriveBackup: LocalDateTime
        get() {
            val timestamp = prefs.getLong(KEY_LAST_DRIVE_BACKUP, 0)
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
            val timestamp = java.time.ZoneId.systemDefault().rules.getOffset(value).totalSeconds.toLong() * 1000L
            prefs.edit().putLong(KEY_LAST_DRIVE_BACKUP, timestamp).apply()
        }
    
    private fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        
        return GoogleSignIn.getClient(context, gso)
    }
    
    private fun getDriveService(account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, 
            listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account
        
        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("TimeReportCalculator")
            .build()
    }
    
    suspend fun signInToGoogleDrive(activity: ComponentActivity): Result<GoogleSignInAccount> = withContext(Dispatchers.IO) {
        try {
            val signInClient = getGoogleSignInClient()
            val account = GoogleSignIn.getLastSignedInAccount(context)
            
            if (account != null && GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_FILE))) {
                isGoogleDriveEnabled = true
                Result.success(account)
            } else {
                // Need to sign in - this would typically trigger UI flow
                Result.failure(Exception("User needs to sign in to Google Drive"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadBackupToDrive(localFileName: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(context)
                ?: return@withContext Result.failure(Exception("Not signed in to Google Drive"))
            
            val driveService = getDriveService(account)
            
            // Create app folder if it doesn't exist
            val appFolderId = getOrCreateAppFolder(driveService)
                ?: return@withContext Result.failure(Exception("Could not create app folder"))
            
            // Read local backup file
            val localFile = java.io.File(context.filesDir, "timereports/$localFileName")
            if (!localFile.exists()) {
                return@withContext Result.failure(Exception("Local backup file not found: $localFileName"))
            }
            
            // Create drive file metadata
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
            val driveFileName = "backup_$timestamp.json"
            
            val fileMetadata = File().apply {
                name = driveFileName
                parents = listOf(appFolderId)
            }
            
            // Upload file content
            val mediaContent = com.google.api.client.http.FileContent("application/json", localFile)
            val uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, name, createdTime")
                .execute()
            
            lastDriveBackup = LocalDateTime.now()
            
            Result.success("Backup uploaded to Google Drive: ${uploadedFile.name}")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun getOrCreateAppFolder(driveService: Drive): String? = withContext(Dispatchers.IO) {
        try {
            // Search for existing app folder
            val query = "name='$APP_FOLDER_NAME' and mimeType='application/vnd.google-apps.folder' and trashed=false"
            val result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .execute()
            
            if (result.files.isNotEmpty()) {
                return@withContext result.files[0].id
            }
            
            // Create new app folder
            val folderMetadata = File().apply {
                name = APP_FOLDER_NAME
                mimeType = "application/vnd.google-apps.folder"
            }
            
            val folder = driveService.files().create(folderMetadata)
                .setFields("id")
                .execute()
            
            folder.id
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun listDriveBackups(): Result<List<DriveBackupInfo>> = withContext(Dispatchers.IO) {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(context)
                ?: return@withContext Result.failure(Exception("Not signed in to Google Drive"))
            
            val driveService = getDriveService(account)
            val appFolderId = getOrCreateAppFolder(driveService)
                ?: return@withContext Result.failure(Exception("Could not access app folder"))
            
            val query = "'$appFolderId' in parents and mimeType='application/json' and trashed=false"
            val result = driveService.files().list()
                .setQ(query)
                .setOrderBy("createdTime desc")
                .setFields("files(id, name, createdTime, size)")
                .execute()
            
            val backups = result.files.map { file ->
                DriveBackupInfo(
                    id = file.id,
                    name = file.name,
                    createdTime = file.createdTime?.toString() ?: "",
                    size = file.size?.toLong() ?: 0L
                )
            }
            
            Result.success(backups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun downloadBackupFromDrive(fileId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(context)
                ?: return@withContext Result.failure(Exception("Not signed in to Google Drive"))
            
            val driveService = getDriveService(account)
            
            // Download file content
            val outputStream = ByteArrayOutputStream()
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            
            val content = outputStream.toString("UTF-8")
            
            // Save to local file
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
            val localFileName = "drive_backup_$timestamp.json"
            val localFile = java.io.File(context.filesDir, "timereports/$localFileName")
            localFile.parentFile?.mkdirs()
            localFile.writeText(content)
            
            Result.success(localFileName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getSignInIntent(): Intent {
        return getGoogleSignInClient().signInIntent
    }

    fun handleSignInResult(data: Intent?): Result<GoogleSignInAccount> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            isGoogleDriveEnabled = true
            Result.success(account)
        } catch (e: ApiException) {
            Result.failure(Exception("Google Sign-In misslyckades (kod ${e.statusCode}): ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOutFromGoogleDrive() = withContext(Dispatchers.IO) {
        try {
            val signInClient = getGoogleSignInClient()
            signInClient.signOut()
            isGoogleDriveEnabled = false
        } catch (e: Exception) {
            // Ignore sign out errors
        }
    }
    
    fun isSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null && GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_FILE))
    }
    
    fun getSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}

data class DriveBackupInfo(
    val id: String,
    val name: String,
    val createdTime: String,
    val size: Long
)