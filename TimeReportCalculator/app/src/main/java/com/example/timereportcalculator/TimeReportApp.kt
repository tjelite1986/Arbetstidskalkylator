package com.example.timereportcalculator

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.timereportcalculator.calculator.PayCalculator
import com.example.timereportcalculator.data.PeriodFilter
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.export.FileManager
import com.example.timereportcalculator.ui.components.AddDayDialog
import com.example.timereportcalculator.ui.components.FilePickerDialog
import com.example.timereportcalculator.ui.components.OfflineFileSyncCard
import com.example.timereportcalculator.ui.components.PeriodSelector
import com.example.timereportcalculator.ui.components.SettingsDialog
import com.example.timereportcalculator.ui.components.TimeEntryCard
import com.example.timereportcalculator.ui.components.TotalSummaryCard
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun TimeReportApp(
    timeEntries: List<TimeEntry> = emptyList(),
    settings: Settings = Settings(),
    onTimeEntriesChanged: (List<TimeEntry>) -> Unit = {},
    onSettingsChanged: (Settings) -> Unit = {}
) {
    // Use external state instead of internal state
    // var timeEntries by remember { mutableStateOf(listOf<TimeEntry>()) }
    // var settings by remember { mutableStateOf(Settings()) }
    var selectedPeriod by remember { mutableStateOf(PeriodFilter()) }
    var statusMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    
    // Dialog states
    var showFilePickerDialog by remember { mutableStateOf(false) }
    var showAddDayDialog by remember { mutableStateOf(false) }
    
    val calculator = remember { PayCalculator() }
    val context = LocalContext.current
    val packageInfo = remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            null
        }
    }
    val versionName = packageInfo?.versionName ?: "Unknown"
    
    // File manager and coroutine scope
    val fileManager = remember { FileManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // File export launcher (JSON, CSV, Excel)
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { destinationUri ->
            coroutineScope.launch {
                try {
                    val result = fileManager.exportJsonToUri(timeEntries, settings, destinationUri)
                    if (result.isSuccess) {
                        statusMessage = "JSON-fil exporterad framgångsrikt"
                    } else {
                        errorMessage = "Kunde inte exportera fil: ${result.exceptionOrNull()?.message}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Fel vid export: ${e.message}"
                }
            }
        }
    }
    
    // File import launcher  
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { sourceUri ->
            coroutineScope.launch {
                try {
                    val result = fileManager.importJsonFromUri(sourceUri)
                    if (result.isSuccess) {
                        val (loadedEntries, loadedSettings) = result.getOrNull() ?: return@launch
                        onTimeEntriesChanged(loadedEntries)
                        onSettingsChanged(loadedSettings)
                        statusMessage = "Data importerad framgångsrikt"
                        showFilePickerDialog = false
                    } else {
                        errorMessage = "Kunde inte importera fil: ${result.exceptionOrNull()?.message}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Fel vid import: ${e.message}"
                }
            }
        }
    }
    
    // Filter entries based on selected period
    val filteredEntries = remember(timeEntries, selectedPeriod) {
        val (startDate, endDate) = selectedPeriod.getDateRange()
        if (startDate == null || endDate == null) {
            timeEntries
        } else {
            timeEntries.filter { entry ->
                !entry.date.isBefore(startDate) && !entry.date.isAfter(endDate)
            }
        }
    }
    
    // Calculate totals from filtered entries
    val totalHours = filteredEntries.sumOf { it.workHours }
    val totalBasePay = filteredEntries.sumOf { it.basePay }
    val totalOBPay = filteredEntries.sumOf { it.obPay }
    val totalGrossPay = filteredEntries.sumOf { it.totalPay }
    val totalTax = filteredEntries.sumOf { it.taxAmount }
    val totalNetPay = filteredEntries.sumOf { it.netPay }
    
    // Clear status message after showing it
    LaunchedEffect(statusMessage) {
        if (statusMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(3000)
            statusMessage = ""
        }
    }
    
    // Clear error message after showing it
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(5000)
            errorMessage = ""
        }
    }
    
    // No need for LaunchedEffect since we're using external state
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = stringResource(R.string.time_report_title),
                            fontSize = 16.sp
                        )
                        Text(
                            text = versionName,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDayDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_day))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Current time display
                Text(
                    text = "Aktuell tid: ${DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(java.time.LocalDateTime.now())}",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            item {
                // Period selector
                PeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onPeriodChanged = { newPeriod ->
                        selectedPeriod = newPeriod
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            item {
                TotalSummaryCard(
                    totalHours = totalHours,
                    totalBasePay = totalBasePay,
                    totalOBPay = totalOBPay,
                    totalGrossPay = totalGrossPay,
                    totalTax = totalTax,
                    totalNetPay = totalNetPay
                )
            }
            
            item {
                // Offline file sync card
                OfflineFileSyncCard(
                    timeEntries = timeEntries,
                    settings = settings,
                    onDataLoaded = { loadedEntries, loadedSettings ->
                        onTimeEntriesChanged(loadedEntries)
                        onSettingsChanged(loadedSettings)
                    },
                    onStatusMessage = { message ->
                        statusMessage = message
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Show status message if available
            if (statusMessage.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = statusMessage,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            
            // Show error message if available
            if (errorMessage.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = errorMessage,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
            
            item {
                Column {
                    // First row: Calculate and Clear buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                try {
                                    val calculatedEntries = timeEntries.map { entry ->
                                        calculator.calculatePay(entry, settings)
                                    }
                                    onTimeEntriesChanged(calculatedEntries)
                                    errorMessage = ""
                                } catch (e: IllegalArgumentException) {
                                    errorMessage = "Fel i tidsrapporter: ${e.message?.replace("End time cannot be before start time", "Sluttid kan inte vara före starttid")?.replace("Break end time cannot be before break start time", "Rast-sluttid kan inte vara före rast-starttid")?.replace("Work hours cannot be negative. Check start/end times.", "Arbetstid kan inte vara negativ. Kontrollera start- och sluttider.")?.replace("Break time cannot exceed total work time", "Rasttid kan inte överstiga total arbetstid")?.replace("Break minutes cannot be negative", "Rastminuter kan inte vara negativa")}"
                                }
                            }
                        ) {
                            Text(stringResource(R.string.calculate_total))
                        }
                        
                        Button(
                            onClick = { onTimeEntriesChanged(emptyList()) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.error
                            )
                        ) {
                            Text(stringResource(R.string.clear_data))
                        }
                    }
                    
                }
            }
            
            if (timeEntries.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Inga tidsrapporter än",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tryck på + för att lägga till din första dag",
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            } else if (filteredEntries.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Inga rader för vald period",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Välj en annan period eller lägg till fler tidsrapporter",
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
            
            items(filteredEntries) { entry ->
                TimeEntryCard(
                    entry = entry,
                    settings = settings,
                    onEntryChange = { updatedEntry ->
                        try {
                            val updatedEntries = timeEntries.map { 
                                if (it.id == updatedEntry.id) {
                                    calculator.calculatePay(updatedEntry, settings)
                                } else it
                            }
                            onTimeEntriesChanged(updatedEntries)
                            errorMessage = ""
                        } catch (e: IllegalArgumentException) {
                            // Update the entry without calculation to preserve user input
                            val updatedEntries = timeEntries.map { 
                                if (it.id == updatedEntry.id) {
                                    updatedEntry.copy(
                                        workHours = 0.0,
                                        basePay = 0.0,
                                        obPay = 0.0,
                                        totalPay = 0.0,
                                        taxAmount = 0.0,
                                        netPay = 0.0
                                    )
                                } else it
                            }
                            onTimeEntriesChanged(updatedEntries)
                            errorMessage = "Fel i tidsrapport: ${e.message?.replace("End time cannot be before start time", "Sluttid kan inte vara före starttid")?.replace("Break end time cannot be before break start time", "Rast-sluttid kan inte vara före rast-starttid")?.replace("Work hours cannot be negative. Check start/end times.", "Arbetstid kan inte vara negativ. Kontrollera start- och sluttider.")?.replace("Break time cannot exceed total work time", "Rasttid kan inte överstiga total arbetstid")?.replace("Break minutes cannot be negative", "Rastminuter kan inte vara negativa")}"
                        }
                    },
                    onDelete = { entryToDelete ->
                        onTimeEntriesChanged(timeEntries.filter { it.id != entryToDelete.id })
                    }
                )
            }
        }
    }
    
    // Add Day Dialog
    AddDayDialog(
        isOpen = showAddDayDialog,
        settings = settings,
        onDismiss = { showAddDayDialog = false },
        onConfirm = { newEntry ->
            onTimeEntriesChanged(timeEntries + newEntry)
            showAddDayDialog = false
        }
    )
    
    // File Picker Dialog
    if (showFilePickerDialog) {
        FilePickerDialog(
            isOpen = showFilePickerDialog,
            onDismiss = { showFilePickerDialog = false },
            onExportJson = {
                if (timeEntries.isNotEmpty()) {
                    val timestamp = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
                    exportLauncher.launch("tidrapport_$timestamp.json")
                } else {
                    errorMessage = "Inga tidsrapporter att exportera"
                }
            },
            onImportJson = {
                importLauncher.launch(arrayOf("application/json", "text/plain"))
            },
            onOpenGoogleDrive = {
                try {
                    val intent = fileManager.openGoogleDriveIntent()
                    context.startActivity(intent)
                } catch (e: Exception) {
                    errorMessage = "Kunde inte öppna Google Drive: ${e.message}"
                }
            },
            savedFiles = fileManager.getSavedFiles(),
            onLoadSavedFile = { fileName ->
                coroutineScope.launch {
                    try {
                        val result = fileManager.loadFromJson(fileName)
                        if (result.isSuccess) {
                            val (loadedEntries, loadedSettings) = result.getOrNull() ?: return@launch
                            onTimeEntriesChanged(loadedEntries)
                            onSettingsChanged(loadedSettings)
                            statusMessage = "Data laddad från lokal fil"
                            showFilePickerDialog = false
                        } else {
                            errorMessage = "Kunde inte ladda fil: ${result.exceptionOrNull()?.message}"
                        }
                    } catch (e: Exception) {
                        errorMessage = "Fel vid laddning: ${e.message}"
                    }
                }
            },
            onDeleteSavedFile = { fileName ->
                if (fileManager.deleteSavedFile(fileName)) {
                    statusMessage = "Fil raderad"
                } else {
                    errorMessage = "Kunde inte radera fil"
                }
            }
        )
    }
}