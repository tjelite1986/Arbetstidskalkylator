package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.*

@Composable
fun ThemeSelectionDialog(
    isOpen: Boolean,
    currentThemeSettings: ThemeSettings,
    onThemeSettingsChange: (ThemeSettings) -> Unit,
    onDismiss: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Palette,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "Tema-inställningar",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Theme Mode Selection
                    ThemeModeSelector(
                        currentMode = currentThemeSettings.themeMode,
                        onModeChange = { newMode ->
                            onThemeSettingsChange(currentThemeSettings.copy(themeMode = newMode))
                        }
                    )
                    
                    Divider()
                    
                    // Additional Options
                    ThemeOptions(
                        currentSettings = currentThemeSettings,
                        onSettingsChange = onThemeSettingsChange
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Stäng")
                }
            }
        )
    }
}

@Composable
private fun ThemeModeSelector(
    currentMode: ThemeMode,
    onModeChange: (ThemeMode) -> Unit
) {
    Column {
        Text(
            text = "Tema-läge",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        ThemeMode.values().forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = currentMode == mode,
                        onClick = { onModeChange(mode) }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentMode == mode,
                    onClick = { onModeChange(mode) }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = when (mode) {
                        ThemeMode.LIGHT -> Icons.Default.LightMode
                        ThemeMode.DARK -> Icons.Default.DarkMode
                        ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                    },
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (currentMode == mode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Column {
                    Text(
                        text = mode.displayName,
                        style = MaterialTheme.typography.body1,
                        fontWeight = if (currentMode == mode) FontWeight.Medium else FontWeight.Normal,
                        color = if (currentMode == mode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = mode.description,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}


@Composable
private fun ThemeOptions(
    currentSettings: ThemeSettings,
    onSettingsChange: (ThemeSettings) -> Unit
) {
    Column {
        Text(
            text = "Avancerade alternativ",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Dynamic Colors Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSettingsChange(currentSettings.copy(useDynamicColors = !currentSettings.useDynamicColors))
                }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Dynamiska färger",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Använd systemets färgschema (Android 12+)",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Switch(
                checked = currentSettings.useDynamicColors,
                onCheckedChange = { checked ->
                    onSettingsChange(currentSettings.copy(useDynamicColors = checked))
                }
            )
        }
        
        // Material 3 Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSettingsChange(currentSettings.copy(useMaterial3 = !currentSettings.useMaterial3))
                }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Material Design 3",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Använd det senaste Material Design-systemet",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Switch(
                checked = currentSettings.useMaterial3,
                onCheckedChange = { checked ->
                    onSettingsChange(currentSettings.copy(useMaterial3 = checked))
                }
            )
        }
    }
}

