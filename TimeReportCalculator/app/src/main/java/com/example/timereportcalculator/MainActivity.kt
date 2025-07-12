package com.example.timereportcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.timereportcalculator.debug.DebugInfo
import com.example.timereportcalculator.ui.theme.TimeReportCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log SHA1 fingerprint for Google Console setup
        DebugInfo.logSHA1Fingerprint(this)
        
        setContent {
            TimeReportCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainApp()
                }
            }
        }
    }
}