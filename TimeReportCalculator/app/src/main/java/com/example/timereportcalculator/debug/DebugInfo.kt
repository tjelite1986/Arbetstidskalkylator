package com.example.timereportcalculator.debug

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

object DebugInfo {
    fun logSHA1Fingerprint(context: Context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            
            for (signature in packageInfo.signatures) {
                val md = MessageDigest.getInstance("SHA1")
                md.update(signature.toByteArray())
                val digest = md.digest()
                
                val sha1 = digest.joinToString(":") { 
                    String.format("%02X", it)
                }
                
                Log.d("DEBUG_SHA1", "SHA1 Fingerprint: $sha1")
                Log.d("DEBUG_SHA1", "Package: ${context.packageName}")
            }
        } catch (e: Exception) {
            Log.e("DEBUG_SHA1", "Error getting SHA1: ${e.message}")
        }
    }
}