package se.thomas.arbetstidskalkylator.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object LogUtils {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    
    fun writeLog(@Suppress("UNUSED_PARAMETER") context: Context, tag: String, message: String) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val logFile = File(downloadsDir, "arbetstidskalkylator_debug.log")
            
            val timestamp = dateFormat.format(Date())
            val logEntry = "[$timestamp] $tag: $message\n"
            
            FileWriter(logFile, true).use { writer ->
                writer.append(logEntry)
            }
            
            // Även skicka till Android logcat
            android.util.Log.d(tag, message)
            
        } catch (e: Exception) {
            android.util.Log.e("LogUtils", "Failed to write log: ${e.message}")
        }
    }
    
    fun clearLog(@Suppress("UNUSED_PARAMETER") context: Context) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val logFile = File(downloadsDir, "arbetstidskalkylator_debug.log")
            if (logFile.exists()) {
                logFile.delete()
            }
        } catch (e: Exception) {
            android.util.Log.e("LogUtils", "Failed to clear log: ${e.message}")
        }
    }
}