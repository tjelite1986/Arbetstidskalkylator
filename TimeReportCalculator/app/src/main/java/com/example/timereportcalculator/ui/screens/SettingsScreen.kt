package com.example.timereportcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.Settings

@Composable
fun SettingsScreen(
    settings: Settings = Settings(),
    onSettingsChange: (Settings) -> Unit = {}
) {
    var showAdvancedSettings by remember { mutableStateOf(false) }
    
    if (showAdvancedSettings) {
        AdvancedSettingsScreen(
            settings = settings,
            onSettingsChange = onSettingsChange
        )
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Inställningar",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colors.primary
                )
                
                Text(
                    text = "Inställningar",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                
                Text(
                    text = "Här kommer utökade inställningar:",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SettingItem("💰", "Lön och OB-inställningar")
                    SettingItem("📋", "Avtalsnivåer")
                    SettingItem("🏖️", "Semesterersättning")
                    SettingItem("🎨", "Tema och utseende")
                    SettingItem("🔔", "Notifikationer")
                    SettingItem("🌐", "Språk och region")
                    SettingItem("🔐", "Säkerhet och backup")
                }
                
                Divider()
                
                // Advanced settings button
                Button(
                    onClick = { showAdvancedSettings = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Öppna avancerade inställningar")
                }
                
                
                Text(
                    text = "Klicka på 'Avancerade inställningar' för att anpassa OB-satser, avtalsnivåer och semesterersättning.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SettingItem(icon: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}