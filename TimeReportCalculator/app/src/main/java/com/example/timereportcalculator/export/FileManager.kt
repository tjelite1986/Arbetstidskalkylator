package com.example.timereportcalculator.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.data.TimeReportSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileManager(private val context: Context) {
    
    private val internalDir = File(context.filesDir, "timereports")
    
    init {
        if (!internalDir.exists()) {
            internalDir.mkdirs()
        }
    }
    
    suspend fun saveToJson(entries: List<TimeEntry>, settings: Settings): Result<String> = withContext(Dispatchers.IO) {
        try {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
            val fileName = "tidrapport_$timestamp.json"
            val file = File(internalDir, fileName)
            
            val jsonData = TimeReportSerializer.toJson(entries, settings)
            file.writeText(jsonData)
            
            Result.success(fileName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loadFromJson(fileName: String): Result<Pair<List<TimeEntry>, Settings>> = withContext(Dispatchers.IO) {
        try {
            val file = File(internalDir, fileName)
            if (!file.exists()) {
                return@withContext Result.failure(Exception("Filen finns inte"))
            }
            
            val jsonData = file.readText()
            val (entries, settings) = TimeReportSerializer.fromJson(jsonData)
            
            Result.success(Pair(entries, settings))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun exportToCsv(entries: List<TimeEntry>, destinationUri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                outputStream.bufferedWriter().use { writer ->
                    // CSV Header
                    writer.append("Datum,Veckodag,Starttid,Sluttid,Rast(min),Arbetstid(h),Grundlön,OB-tillägg,Totalt,Skatt,Netto,Röd dag,Sjukdag\n")
                    
                    entries.forEach { entry ->
                        val dayOfWeek = when (entry.date.dayOfWeek.value) {
                            1 -> "Måndag"
                            2 -> "Tisdag" 
                            3 -> "Onsdag"
                            4 -> "Torsdag"
                            5 -> "Fredag"
                            6 -> "Lördag"
                            7 -> "Söndag"
                            else -> "Okänd"
                        }
                        
                        writer.append("${entry.date},")
                        writer.append("$dayOfWeek,")
                        writer.append("${entry.startTime ?: ""},")
                        writer.append("${entry.endTime ?: ""},")
                        writer.append("${entry.breakMinutes},")
                        writer.append("${String.format("%.2f", entry.workHours)},")
                        writer.append("${String.format("%.2f", entry.basePay)},")
                        writer.append("${String.format("%.2f", entry.obPay)},")
                        writer.append("${String.format("%.2f", entry.totalPay)},")
                        writer.append("${String.format("%.2f", entry.taxAmount)},")
                        writer.append("${String.format("%.2f", entry.netPay)},")
                        writer.append("${if (entry.isRedDay) "Ja" else "Nej"},")
                        writer.append("${if (entry.isSickDay) "Ja" else "Nej"}\n")
                    }
                }
            }
            
            Result.success("CSV-fil sparad framgångsrikt")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun exportToExcel(entries: List<TimeEntry>, destinationUri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                outputStream.bufferedWriter().use { writer ->
                    // Tab-separated header
                    writer.append("Datum\tVeckodag\tStarttid\tSluttid\tRast(min)\tArbetstid(h)\tGrundlön\tOB-tillägg\tTotalt\tSkatt\tNetto\tRöd dag\tSjukdag\n")
                    
                    entries.forEach { entry ->
                        val dayOfWeek = when (entry.date.dayOfWeek.value) {
                            1 -> "Måndag"
                            2 -> "Tisdag"
                            3 -> "Onsdag" 
                            4 -> "Torsdag"
                            5 -> "Fredag"
                            6 -> "Lördag"
                            7 -> "Söndag"
                            else -> "Okänd"
                        }
                        
                        writer.append("${entry.date}\t")
                        writer.append("$dayOfWeek\t")
                        writer.append("${entry.startTime ?: ""}\t")
                        writer.append("${entry.endTime ?: ""}\t")
                        writer.append("${entry.breakMinutes}\t")
                        writer.append("${String.format("%.2f", entry.workHours)}\t")
                        writer.append("${String.format("%.2f", entry.basePay)}\t")
                        writer.append("${String.format("%.2f", entry.obPay)}\t")
                        writer.append("${String.format("%.2f", entry.totalPay)}\t")
                        writer.append("${String.format("%.2f", entry.taxAmount)}\t")
                        writer.append("${String.format("%.2f", entry.netPay)}\t")
                        writer.append("${if (entry.isRedDay) "Ja" else "Nej"}\t")
                        writer.append("${if (entry.isSickDay) "Ja" else "Nej"}\n")
                    }
                }
            }
            
            Result.success("Excel-fil sparad framgångsrikt")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getSavedFiles(): List<String> {
        return try {
            internalDir.listFiles()
                ?.filter { it.extension == "json" }
                ?.map { it.name }
                ?.sortedDescending() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun createDefaultFileName(type: String): String {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
        return when (type) {
            "csv" -> "tidrapport_$timestamp.csv"
            "excel" -> "tidrapport_$timestamp.xlsx"
            else -> "tidrapport_$timestamp.txt"
        }
    }
    
    fun deleteSavedFile(fileName: String): Boolean {
        return try {
            val file = File(internalDir, fileName)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun exportJsonToUri(entries: List<TimeEntry>, settings: Settings, destinationUri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val jsonData = TimeReportSerializer.toJson(entries, settings)
            
            context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                outputStream.bufferedWriter().use { writer ->
                    writer.write(jsonData)
                }
            }
            
            Result.success("JSON-fil sparad framgångsrikt")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun importJsonFromUri(sourceUri: Uri): Result<Pair<List<TimeEntry>, Settings>> = withContext(Dispatchers.IO) {
        try {
            val jsonData = context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                inputStream.bufferedReader().use { reader ->
                    reader.readText()
                }
            } ?: return@withContext Result.failure(Exception("Kunde inte läsa fil"))
            
            val (entries, settings) = TimeReportSerializer.fromJson(jsonData)
            Result.success(Pair(entries, settings))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun openGoogleDriveIntent(): Intent {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://drive.google.com")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        
        // Försök öppna Google Drive-appen först
        val driveIntent = Intent().apply {
            setPackage("com.google.android.apps.docs")
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        return if (context.packageManager.resolveActivity(driveIntent, 0) != null) {
            driveIntent
        } else {
            intent // Fallback till webbläsare
        }
    }
}