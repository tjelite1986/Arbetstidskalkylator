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
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import java.time.format.DateTimeFormatter

data class ImportOptions(
    val importEntries: Boolean = true,
    val importSettings: Boolean = true,
    val mergeMode: MergeMode = MergeMode.REPLACE,
    val skipDuplicates: Boolean = true
)

enum class MergeMode {
    REPLACE, // Ersätt all befintlig data
    MERGE,   // Lägg till nya poster, behåll befintliga
    APPEND   // Lägg till alla poster, även dubbletter
}

@Composable
fun ImportDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    importData: Pair<List<TimeEntry>, Settings>?,
    currentEntries: List<TimeEntry>,
    currentSettings: Settings,
    onConfirm: (List<TimeEntry>, Settings, ImportOptions) -> Unit
) {
    var importOptions by remember { mutableStateOf(ImportOptions()) }
    
    if (isOpen && importData != null) {
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
                            Icons.Default.FileDownload,
                            contentDescription = "Import",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Importera Data",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    // Data preview
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Data som ska importeras:",
                                style = MaterialTheme.typography.subtitle2,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "📊 Tidsrapporter:",
                                    style = MaterialTheme.typography.body2
                                )
                                Text(
                                    text = "${importData.first.size} poster",
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            if (importData.first.isNotEmpty()) {
                                val dateRange = importData.first.let { entries ->
                                    val minDate = entries.minBy { it.date }.date
                                    val maxDate = entries.maxBy { it.date }.date
                                    "${minDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} - ${maxDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
                                }
                                Text(
                                    text = "📅 Datumspan: $dateRange",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "⚙️ Inställningar:",
                                    style = MaterialTheme.typography.body2
                                )
                                Text(
                                    text = "Inkluderade",
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    // Current data info
                    if (currentEntries.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "Nuvarande data:",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${currentEntries.size} befintliga tidsrapporter",
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    
                    // Import options
                    Text(
                        text = "Importalternativ:",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // What to import
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = importOptions.importEntries,
                            onCheckedChange = { importOptions = importOptions.copy(importEntries = it) }
                        )
                        Text(
                            text = "Importera tidsrapporter",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = importOptions.importSettings,
                            onCheckedChange = { importOptions = importOptions.copy(importSettings = it) }
                        )
                        Text(
                            text = "Importera inställningar",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    if (importOptions.importEntries && currentEntries.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Sammanfogningsläge:",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Column(
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            MergeMode.values().forEach { mode ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = importOptions.mergeMode == mode,
                                        onClick = { importOptions = importOptions.copy(mergeMode = mode) }
                                    )
                                    Column(
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(
                                            text = when (mode) {
                                                MergeMode.REPLACE -> "Ersätt all data"
                                                MergeMode.MERGE -> "Sammanfoga data"
                                                MergeMode.APPEND -> "Lägg till alla"
                                            },
                                            style = MaterialTheme.typography.body2,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = when (mode) {
                                                MergeMode.REPLACE -> "Ta bort befintliga poster och ersätt med importerade"
                                                MergeMode.MERGE -> "Behåll befintliga poster, lägg till nya (hoppa över dubbletter)"
                                                MergeMode.APPEND -> "Lägg till alla importerade poster (tillåt dubbletter)"
                                            },
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                        
                        if (importOptions.mergeMode == MergeMode.MERGE) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Checkbox(
                                    checked = importOptions.skipDuplicates,
                                    onCheckedChange = { importOptions = importOptions.copy(skipDuplicates = it) }
                                )
                                Text(
                                    text = "Hoppa över dubbletter (samma datum och tid)",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.body2
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
                                val finalEntries = when {
                                    !importOptions.importEntries -> currentEntries
                                    importOptions.mergeMode == MergeMode.REPLACE -> importData.first
                                    importOptions.mergeMode == MergeMode.APPEND -> currentEntries + importData.first
                                    else -> {
                                        // Merge mode
                                        val newEntries = if (importOptions.skipDuplicates) {
                                            importData.first.filter { importEntry ->
                                                !currentEntries.any { currentEntry ->
                                                    currentEntry.date == importEntry.date &&
                                                    currentEntry.startTime == importEntry.startTime &&
                                                    currentEntry.endTime == importEntry.endTime
                                                }
                                            }
                                        } else {
                                            importData.first
                                        }
                                        currentEntries + newEntries
                                    }
                                }
                                
                                val finalSettings = if (importOptions.importSettings) {
                                    importData.second
                                } else {
                                    currentSettings
                                }
                                
                                onConfirm(finalEntries, finalSettings, importOptions)
                            },
                            modifier = Modifier.weight(1f),
                            enabled = importOptions.importEntries || importOptions.importSettings
                        ) {
                            Text("Importera")
                        }
                    }
                }
            }
        }
    }
}