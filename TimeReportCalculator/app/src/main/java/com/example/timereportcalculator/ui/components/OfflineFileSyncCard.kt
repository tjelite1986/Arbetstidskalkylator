package com.example.timereportcalculator.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.export.FileManager
import kotlinx.coroutines.launch

@Composable
fun OfflineFileSyncCard(
    timeEntries: List<TimeEntry>,
    settings: Settings,
    onDataLoaded: (List<TimeEntry>, Settings) -> Unit,
    onStatusMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fileManager = remember { FileManager(context) }
    
    var isLoading by remember { mutableStateOf(false) }
    var showSavedFiles by remember { mutableStateOf(false) }
    var savedFiles by remember { mutableStateOf(listOf<String>()) }
    var isExpanded by remember { mutableStateOf(false) }
    
    val csvFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            scope.launch {
                isLoading = true
                try {
                    val result = fileManager.exportToCsv(timeEntries, it)
                    if (result.isSuccess) {
                        onStatusMessage("CSV-fil sparad framgångsrikt")
                    } else {
                        onStatusMessage("Fel vid CSV-export: ${result.exceptionOrNull()?.message}")
                    }
                } finally {
                    isLoading = false
                }
            }
        }
    }
    
    val excelFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri ->
        uri?.let {
            scope.launch {
                isLoading = true
                try {
                    val result = fileManager.exportToExcel(timeEntries, it)
                    if (result.isSuccess) {
                        onStatusMessage("Excel-fil sparad framgångsrikt")
                    } else {
                        onStatusMessage("Fel vid Excel-export: ${result.exceptionOrNull()?.message}")
                    }
                } finally {
                    isLoading = false
                }
            }
        }
    }
    
    LaunchedEffect(Unit) {
        savedFiles = fileManager.getSavedFiles()
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header - clickable to expand/collapse
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(bottom = if (isExpanded) 16.dp else 0.dp)
            ) {
                Icon(
                    Icons.Default.Save,
                    contentDescription = "Offline lagring",
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Offline Filhantering",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                // Status indicator when collapsed
                if (!isExpanded) {
                    Text(
                        text = "${savedFiles.size} sparade",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                // Expand/collapse icon
                Icon(
                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Minimera" else "Expandera",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
            
            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Text(
                        text = "Spara och ladda dina tidsrapporter lokalt på enheten, eller exportera till CSV/Excel.",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Save/Load buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Save JSON button
                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    try {
                                        val result = fileManager.saveToJson(timeEntries, settings)
                                        if (result.isSuccess) {
                                            onStatusMessage("Data sparad som: ${result.getOrNull()}")
                                            savedFiles = fileManager.getSavedFiles()
                                        } else {
                                            onStatusMessage("Fel vid sparande: ${result.exceptionOrNull()?.message}")
                                        }
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && timeEntries.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Default.Save,
                                    contentDescription = "Spara",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Spara JSON")
                        }
                        
                        // Load button
                        Button(
                            onClick = { showSavedFiles = !showSavedFiles },
                            modifier = Modifier.weight(1f),
                            enabled = savedFiles.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF2196F3),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.FolderOpen,
                                contentDescription = "Ladda",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ladda (${savedFiles.size})")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Export buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Export CSV button
                        Button(
                            onClick = {
                                csvFileLauncher.launch(fileManager.createDefaultFileName("csv"))
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && timeEntries.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFFF9800),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.TableChart,
                                contentDescription = "CSV",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("CSV")
                        }
                        
                        // Export Excel button
                        Button(
                            onClick = {
                                excelFileLauncher.launch(fileManager.createDefaultFileName("excel"))
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && timeEntries.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = "Excel",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Excel")
                        }
                    }
                    
                    // Show saved files section
                    if (showSavedFiles && savedFiles.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sparade filer:",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold
                        )
                        
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 200.dp)
                        ) {
                            items(savedFiles) { fileName ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    elevation = 2.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Description,
                                            contentDescription = "Fil",
                                            tint = Color(0xFF4CAF50),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        Text(
                                            text = fileName,
                                            style = MaterialTheme.typography.body2,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        // Load button
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    isLoading = true
                                                    try {
                                                        val result = fileManager.loadFromJson(fileName)
                                                        if (result.isSuccess) {
                                                            val (entries, loadedSettings) = result.getOrNull()!!
                                                            onDataLoaded(entries, loadedSettings)
                                                            onStatusMessage("Data laddad från: $fileName")
                                                            showSavedFiles = false
                                                        } else {
                                                            onStatusMessage("Fel vid laddning: ${result.exceptionOrNull()?.message}")
                                                        }
                                                    } finally {
                                                        isLoading = false
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.CloudDownload,
                                                contentDescription = "Ladda",
                                                tint = Color(0xFF2196F3)
                                            )
                                        }
                                        
                                        // Delete button
                                        IconButton(
                                            onClick = {
                                                if (fileManager.deleteSavedFile(fileName)) {
                                                    savedFiles = fileManager.getSavedFiles()
                                                    onStatusMessage("$fileName raderad")
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Radera",
                                                tint = Color(0xFFF44336)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}