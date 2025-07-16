package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.timereportcalculator.export.BackupSettings

@Composable
fun BackupSettingsDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentSettings: BackupSettings,
    onSettingsChanged: (BackupSettings) -> Unit
) {
    var settings by remember { mutableStateOf(currentSettings) }
    
    if (isOpen) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Backup Settings",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Backup-inställningar",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    // Auto backup toggle
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Schedule,
                                    contentDescription = "Auto Backup",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "Automatiska backups",
                                        style = MaterialTheme.typography.subtitle1,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Skapa backups automatiskt enligt schema",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                Switch(
                                    checked = settings.isAutoBackupEnabled,
                                    onCheckedChange = { 
                                        settings = settings.copy(isAutoBackupEnabled = it)
                                    }
                                )
                            }
                        }
                    }
                    
                    // Backup interval settings
                    if (settings.isAutoBackupEnabled) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.AccessTime,
                                        contentDescription = "Interval",
                                        tint = MaterialTheme.colors.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Backup-intervall",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                                
                                Text(
                                    text = "Skapa backup var ${settings.backupIntervalHours} timme${if (settings.backupIntervalHours != 1) "r" else ""}",
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Slider(
                                    value = settings.backupIntervalHours.toFloat(),
                                    onValueChange = { 
                                        settings = settings.copy(backupIntervalHours = it.toInt())
                                    },
                                    valueRange = 1f..168f, // 1 hour to 1 week
                                    steps = 23, // 24 total values
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "1 timme",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "1 vecka",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                
                                // Quick presets
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 12.dp)
                                ) {
                                    val presets = listOf(
                                        "6h" to 6,
                                        "12h" to 12,
                                        "24h" to 24,
                                        "48h" to 48,
                                        "1v" to 168
                                    )
                                    
                                    presets.forEach { (label, hours) ->
                                        OutlinedButton(
                                            onClick = { 
                                                settings = settings.copy(backupIntervalHours = hours)
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                backgroundColor = if (settings.backupIntervalHours == hours) 
                                                    MaterialTheme.colors.primary.copy(alpha = 0.1f) 
                                                else 
                                                    MaterialTheme.colors.surface
                                            )
                                        ) {
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.caption
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Max backups setting
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Storage,
                                    contentDescription = "Max Backups",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Max antal backups",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            
                            Text(
                                text = "Behåll högst ${settings.maxBackups} backup${if (settings.maxBackups != 1) "s" else ""} (äldre raderas automatiskt)",
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            Slider(
                                value = settings.maxBackups.toFloat(),
                                onValueChange = { 
                                    settings = settings.copy(maxBackups = it.toInt())
                                },
                                valueRange = 3f..50f,
                                steps = 47,
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "3 backups",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "50 backups",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                    
                    // Backup on changes setting
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Sync,
                                    contentDescription = "Backup on Changes",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "Backup vid ändringar",
                                        style = MaterialTheme.typography.subtitle2,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Skapa backup automatiskt när data ändras",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                Switch(
                                    checked = settings.backupOnChanges,
                                    onCheckedChange = { 
                                        settings = settings.copy(backupOnChanges = it)
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Avbryt")
                        }
                        
                        Button(
                            onClick = {
                                onSettingsChanged(settings)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Spara")
                        }
                    }
                }
            }
        }
    }
}