package com.example.timereportcalculator

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val totalGrossPay = filteredEntries.sumOf { it.grossPay }
    val totalVacationPay = filteredEntries.sumOf { it.vacationPay }
    val totalPayBeforeTax = filteredEntries.sumOf { it.totalPay }
    val totalTax = filteredEntries.sumOf { it.taxAmount }
    val totalNetPay = filteredEntries.sumOf { it.netPay }
    
    // Calculate OB breakdown totals
    val totalOBBreakdown = mutableMapOf<String, Double>()
    filteredEntries.forEach { entry ->
        entry.obBreakdown.forEach { (description, amount) ->
            totalOBBreakdown[description] = (totalOBBreakdown[description] ?: 0.0) + amount
        }
    }
    
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                color = MaterialTheme.colors.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.time_report_title),
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            text = "Version $versionName",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colors.primary.copy(alpha = 0.2f),
                                        MaterialTheme.colors.primary.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colors.background,
                            MaterialTheme.colors.surface.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring()),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Aktuell tid",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(java.time.LocalDateTime.now()),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring()),
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    PeriodSelector(
                        selectedPeriod = selectedPeriod,
                        onPeriodChanged = { newPeriod ->
                            selectedPeriod = newPeriod
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring())
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(20.dp),
                            clip = false
                        ),
                    elevation = 0.dp,
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colors.primary.copy(alpha = 0.05f),
                                        MaterialTheme.colors.surface
                                    )
                                )
                            )
                    ) {
                        TotalSummaryCard(
                            totalHours = totalHours,
                            totalBasePay = totalBasePay,
                            totalOBPay = totalOBPay,
                            totalGrossPay = totalGrossPay,
                            totalVacationPay = totalVacationPay,
                            totalPayBeforeTax = totalPayBeforeTax,
                            totalTax = totalTax,
                            totalNetPay = totalNetPay,
                            vacationRate = settings.vacationRate,
                            totalOBBreakdown = totalOBBreakdown
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring()),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
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
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            
            // Show status message if available
            if (statusMessage.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(spring()),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = statusMessage,
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            // Show error message if available
            if (errorMessage.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(spring()),
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colors.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring()),
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Åtgärder",
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.elevation(
                                    defaultElevation = 2.dp,
                                    pressedElevation = 4.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.calculate_total),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            Button(
                                onClick = { onTimeEntriesChanged(emptyList()) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.error
                                ),
                                elevation = ButtonDefaults.elevation(
                                    defaultElevation = 2.dp,
                                    pressedElevation = 4.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.clear_data),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            
            if (timeEntries.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(spring()),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colors.primary.copy(alpha = 0.2f),
                                                MaterialTheme.colors.primary.copy(alpha = 0.05f)
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Inga tidsrapporter än",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = "Tryck på + för att lägga till din första dag",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
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
                            .animateContentSize(spring()),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colors.primary.copy(alpha = 0.2f),
                                                MaterialTheme.colors.primary.copy(alpha = 0.05f)
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Inga rader för vald period",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = "Välj en annan period eller lägg till fler tidsrapporter",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
            
            items(filteredEntries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(spring()),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colors.surface
                ) {
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
                                            grossPay = 0.0,
                                            vacationPay = 0.0,
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