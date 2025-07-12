package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun FilePickerDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onExportJson: () -> Unit,
    onImportJson: () -> Unit,
    onOpenGoogleDrive: () -> Unit,
    savedFiles: List<String>,
    onLoadSavedFile: (String) -> Unit,
    onDeleteSavedFile: (String) -> Unit
) {
    if (isOpen) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filhantering",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Stäng")
                        }
                    }
                    
                    Divider()
                    
                    Text(
                        text = "Export/Import",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Medium
                    )
                    
                    // Export JSON button
                    Button(
                        onClick = onExportJson,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Exportera JSON-fil")
                    }
                    
                    // Import JSON button
                    Button(
                        onClick = onImportJson,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        )
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Importera JSON-fil")
                    }
                    
                    // Google Drive button
                    OutlinedButton(
                        onClick = onOpenGoogleDrive,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4285F4) // Google Blue
                        )
                    ) {
                        Icon(
                            Icons.Default.CloudQueue,
                            contentDescription = null,
                            tint = Color(0xFF4285F4)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Öppna Google Drive", color = Color(0xFF4285F4))
                    }
                    
                    Divider()
                    
                    // Local saved files section
                    Text(
                        text = "Lokalt sparade filer",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (savedFiles.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 200.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(savedFiles) { fileName ->
                                SavedFileItem(
                                    fileName = fileName,
                                    onLoad = { onLoadSavedFile(fileName) },
                                    onDelete = { onDeleteSavedFile(fileName) }
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Inga lokalt sparade filer",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    Divider()
                    
                    // Info text
                    Card(
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💡 Tips:",
                                style = MaterialTheme.typography.subtitle2,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary
                            )
                            Text(
                                text = "• Exportera för att spara till valfri plats (inklusive Google Drive)\n" +
                                      "• Importera för att läsa in från valfri plats\n" +
                                      "• Google Drive-knappen öppnar Drive-appen direkt",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedFileItem(
    fileName: String,
    onLoad: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = fileName,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onLoad) {
                    Icon(
                        Icons.Default.FileOpen,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Läs in")
                }
                TextButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ta bort", color = MaterialTheme.colors.error)
                }
            }
        }
    }
}