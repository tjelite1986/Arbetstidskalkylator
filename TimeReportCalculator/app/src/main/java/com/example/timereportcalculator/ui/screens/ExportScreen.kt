package com.example.timereportcalculator.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.export.DriveBackupInfo
import com.example.timereportcalculator.export.FileManager
import com.example.timereportcalculator.export.BackupManager
import com.example.timereportcalculator.export.PaySlipPdfGenerator
import com.example.timereportcalculator.ui.components.ImportDialog
import com.example.timereportcalculator.ui.components.ImportOptions
import com.example.timereportcalculator.ui.components.BackupSettingsDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ExportScreen(
    timeEntries: List<TimeEntry> = emptyList(),
    settings: Settings = Settings(),
    onTimeEntriesChanged: (List<TimeEntry>) -> Unit = {},
    onSettingsChanged: (Settings) -> Unit = {}
) {
    val context = LocalContext.current
    val fileManager = remember { FileManager(context) }
    val backupManager = remember { BackupManager(context) }
    val coroutineScope = rememberCoroutineScope()
    
    // State for UI
    var statusMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var savedFiles by remember { mutableStateOf(fileManager.getSavedFiles()) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showBackupSettingsDialog by remember { mutableStateOf(false) }
    var pendingImportData by remember { mutableStateOf<Pair<List<TimeEntry>, Settings>?>(null) }
    var backupMetadata by remember { mutableStateOf(listOf<com.example.timereportcalculator.export.BackupMetadata>()) }
    
    // File launchers
    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { destinationUri ->
            coroutineScope.launch {
                try {
                    val result = fileManager.exportJsonToUri(timeEntries, settings, destinationUri)
                    if (result.isSuccess) {
                        statusMessage = "JSON-fil exporterad framgångsrikt"
                    } else {
                        errorMessage = "Kunde inte exportera JSON: ${result.exceptionOrNull()?.message}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Fel vid JSON-export: ${e.message}"
                }
            }
        }
    }
    
    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let { destinationUri ->
            coroutineScope.launch {
                try {
                    val result = fileManager.exportToCsv(timeEntries, destinationUri)
                    if (result.isSuccess) {
                        statusMessage = "CSV-fil exporterad framgångsrikt"
                    } else {
                        errorMessage = "Kunde inte exportera CSV: ${result.exceptionOrNull()?.message}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Fel vid CSV-export: ${e.message}"
                }
            }
        }
    }
    
    val exportExcelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri ->
        uri?.let { destinationUri ->
            coroutineScope.launch {
                try {
                    val result = fileManager.exportToExcel(timeEntries, destinationUri)
                    if (result.isSuccess) {
                        statusMessage = "Excel-fil exporterad framgångsrikt"
                    } else {
                        errorMessage = "Kunde inte exportera Excel: ${result.exceptionOrNull()?.message}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Fel vid Excel-export: ${e.message}"
                }
            }
        }
    }
    
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { sourceUri ->
            coroutineScope.launch {
                try {
                    val result = fileManager.importJsonFromUri(sourceUri)
                    if (result.isSuccess) {
                        val (loadedEntries, loadedSettings) = result.getOrNull() ?: return@launch
                        // Show import dialog for user to choose options
                        pendingImportData = Pair(loadedEntries, loadedSettings)
                        showImportDialog = true
                    } else {
                        errorMessage = "Kunde inte importera fil: ${result.exceptionOrNull()?.message}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Fel vid import: ${e.message}"
                }
            }
        }
    }
    
    // Load backup metadata
    LaunchedEffect(Unit) {
        backupMetadata = backupManager.getBackupMetadata()
    }
    
    // Clear status/error messages after delay
    LaunchedEffect(statusMessage) {
        if (statusMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(3000)
            statusMessage = ""
        }
    }
    
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(5000)
            errorMessage = ""
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.FileUpload,
                        contentDescription = "Export",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Column(
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Export & Backup",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${timeEntries.size} tidsrapporter",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        
        // Status/Error messages
        if (statusMessage.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = statusMessage,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
        
        if (errorMessage.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colors.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = errorMessage,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
        
        item {
            // Export Section
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            Icons.Default.FileUpload,
                            contentDescription = "Export",
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "Exportera Data",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // JSON Export
                        Button(
                            onClick = {
                                if (timeEntries.isNotEmpty()) {
                                    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
                                    exportJsonLauncher.launch("tidrapport_$timestamp.json")
                                } else {
                                    errorMessage = "Inga tidsrapporter att exportera"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = timeEntries.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.DataObject,
                                contentDescription = "JSON",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Exportera som JSON",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        // CSV Export
                        Button(
                            onClick = {
                                if (timeEntries.isNotEmpty()) {
                                    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
                                    exportCsvLauncher.launch("tidrapport_$timestamp.csv")
                                } else {
                                    errorMessage = "Inga tidsrapporter att exportera"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = timeEntries.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.TableChart,
                                contentDescription = "CSV",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Exportera som CSV",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        // Excel Export
                        Button(
                            onClick = {
                                if (timeEntries.isNotEmpty()) {
                                    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
                                    exportExcelLauncher.launch("tidrapport_$timestamp.xlsx")
                                } else {
                                    errorMessage = "Inga tidsrapporter att exportera"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = timeEntries.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = "Excel",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Exportera som Excel",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // Lönebesked (PDF) Section
            val paySlipGenerator = remember { PaySlipPdfGenerator(context) }
            val now = LocalDate.now()
            var selectedYear by remember { mutableStateOf(now.year) }
            var selectedMonth by remember { mutableStateOf(now.monthValue) }
            var yearExpanded by remember { mutableStateOf(false) }
            var monthExpanded by remember { mutableStateOf(false) }
            var isGenerating by remember { mutableStateOf(false) }

            val swedishMonths = listOf(
                "Januari", "Februari", "Mars", "April", "Maj", "Juni",
                "Juli", "Augusti", "September", "Oktober", "November", "December"
            )

            val monthEntries = timeEntries.filter {
                it.date.year == selectedYear && it.date.monthValue == selectedMonth
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            Icons.Default.Description,
                            contentDescription = "L\u00f6nebesked",
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "L\u00f6nebesked (PDF)",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Text(
                        text = "Generera en l\u00f6nespecifikation som liknar ett riktigt svenskt l\u00f6nebesked.",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Month/Year picker row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Month dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { monthExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(swedishMonths[selectedMonth - 1])
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            DropdownMenu(
                                expanded = monthExpanded,
                                onDismissRequest = { monthExpanded = false }
                            ) {
                                swedishMonths.forEachIndexed { index, name ->
                                    DropdownMenuItem(onClick = {
                                        selectedMonth = index + 1
                                        monthExpanded = false
                                    }) {
                                        Text(name)
                                    }
                                }
                            }
                        }

                        // Year dropdown
                        Box(modifier = Modifier.weight(0.6f)) {
                            OutlinedButton(
                                onClick = { yearExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("$selectedYear")
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            DropdownMenu(
                                expanded = yearExpanded,
                                onDismissRequest = { yearExpanded = false }
                            ) {
                                val years = (now.year - 2..now.year + 1).toList()
                                years.forEach { year ->
                                    DropdownMenuItem(onClick = {
                                        selectedYear = year
                                        yearExpanded = false
                                    }) {
                                        Text("$year")
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${monthEntries.size} poster f\u00f6r ${swedishMonths[selectedMonth - 1]} $selectedYear",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (monthEntries.isNotEmpty()) {
                                isGenerating = true
                                coroutineScope.launch {
                                    try {
                                        val file = withContext(Dispatchers.IO) {
                                            paySlipGenerator.generate(
                                                timeEntries, settings, selectedYear, selectedMonth
                                            )
                                        }
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            file
                                        )
                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "application/pdf"
                                            putExtra(Intent.EXTRA_STREAM, uri)
                                            putExtra(Intent.EXTRA_SUBJECT, "L\u00f6nebesked ${swedishMonths[selectedMonth - 1]} $selectedYear")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(
                                            Intent.createChooser(shareIntent, "Spara/Dela l\u00f6nebesked")
                                        )
                                        statusMessage = "L\u00f6nebesked genererat f\u00f6r ${swedishMonths[selectedMonth - 1]} $selectedYear"
                                    } catch (e: Exception) {
                                        errorMessage = "Kunde inte generera l\u00f6nebesked: ${e.message}"
                                    } finally {
                                        isGenerating = false
                                    }
                                }
                            } else {
                                errorMessage = "Inga poster f\u00f6r ${swedishMonths[selectedMonth - 1]} $selectedYear"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = monthEntries.isNotEmpty() && !isGenerating
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = MaterialTheme.colors.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.PictureAsPdf,
                                contentDescription = "PDF",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = if (isGenerating) "Genererar..." else "Generera L\u00f6nebesked",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        item {
            // Import Section
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            Icons.Default.FileDownload,
                            contentDescription = "Import",
                            tint = MaterialTheme.colors.secondary
                        )
                        Text(
                            text = "Importera Data",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    Button(
                        onClick = {
                            importLauncher.launch(arrayOf("application/json", "text/plain"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        )
                    ) {
                        Icon(
                            Icons.Default.FolderOpen,
                            contentDescription = "Import",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Importera JSON-fil",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    Text(
                        text = "Importerar både tidsrapporter och inställningar",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        
        item {
            // Backup Section
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            Icons.Default.Backup,
                            contentDescription = "Backup",
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "Lokala Backups",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val result = backupManager.createBackup(timeEntries, settings, isAutomatic = false)
                                        if (result.isSuccess) {
                                            val metadata = result.getOrNull()
                                            statusMessage = "Backup skapad: ${metadata?.fileName}"
                                            backupMetadata = backupManager.getBackupMetadata()
                                        } else {
                                            errorMessage = "Kunde inte skapa backup: ${result.exceptionOrNull()?.message}"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Fel vid backup: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = timeEntries.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = "Save Backup",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Skapa Backup",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        OutlinedButton(
                            onClick = { showBackupSettingsDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Backup Settings",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Inställningar",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
                    if (backupMetadata.isNotEmpty()) {
                        Text(
                            text = "Sparade backups:",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                }
            }
        }
        
        // Backup Files List
        items(backupMetadata) { metadata ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (metadata.isAutomatic) Icons.Default.Schedule else @Suppress("DEPRECATION") Icons.Filled.InsertDriveFile,
                        contentDescription = "File",
                        tint = if (metadata.isAutomatic) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = metadata.fileName,
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${metadata.description} • ${metadata.entryCount} poster",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = metadata.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    
                    // Load button
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val result = backupManager.loadBackup(metadata.fileName)
                                    if (result.isSuccess) {
                                        val (loadedEntries, loadedSettings) = result.getOrNull() ?: return@launch
                                        // Show import dialog for user to choose options
                                        pendingImportData = Pair(loadedEntries, loadedSettings)
                                        showImportDialog = true
                                    } else {
                                        errorMessage = "Kunde inte ladda backup: ${result.exceptionOrNull()?.message}"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Fel vid återställning: ${e.message}"
                                }
                            }
                        }
                    ) {
                        Text("Ladda")
                    }
                    
                    // Delete button
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                if (backupManager.deleteBackup(metadata.fileName)) {
                                    statusMessage = "Backup raderad"
                                    backupMetadata = backupManager.getBackupMetadata()
                                } else {
                                    errorMessage = "Kunde inte radera backup"
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Radera",
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
        
        item {
            // Google Drive Backup Section
            val googleDriveManager = backupManager.getGoogleDriveBackupManager()
            var driveSignedIn by remember { mutableStateOf(googleDriveManager.isSignedIn()) }
            var driveAccount by remember { mutableStateOf(googleDriveManager.getSignedInAccount()) }
            var driveBackups by remember { mutableStateOf<List<DriveBackupInfo>>(emptyList()) }
            var isLoadingDriveBackups by remember { mutableStateOf(false) }
            var isUploadingToDrive by remember { mutableStateOf(false) }

            // Google Sign-In launcher
            val signInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val signInResult = googleDriveManager.handleSignInResult(result.data)
                if (signInResult.isSuccess) {
                    driveSignedIn = true
                    driveAccount = signInResult.getOrNull()
                    statusMessage = "Ansluten till Google Drive som ${driveAccount?.email}"
                    // Load Drive backups after sign-in
                    coroutineScope.launch {
                        isLoadingDriveBackups = true
                        val listResult = backupManager.listGoogleDriveBackups()
                        if (listResult.isSuccess) {
                            driveBackups = listResult.getOrNull() ?: emptyList()
                        }
                        isLoadingDriveBackups = false
                    }
                } else {
                    errorMessage = signInResult.exceptionOrNull()?.message ?: "Google Sign-In misslyckades"
                }
            }

            // Load Drive backups when already signed in
            LaunchedEffect(driveSignedIn) {
                if (driveSignedIn) {
                    isLoadingDriveBackups = true
                    val listResult = backupManager.listGoogleDriveBackups()
                    if (listResult.isSuccess) {
                        driveBackups = listResult.getOrNull() ?: emptyList()
                    }
                    isLoadingDriveBackups = false
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            Icons.Default.CloudUpload,
                            contentDescription = "Google Drive",
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "Google Drive Backup",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (driveSignedIn) {
                        // Signed in state
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                            elevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Connected",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = "Ansluten till Google Drive",
                                        style = MaterialTheme.typography.body2,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Text(
                                        text = driveAccount?.email ?: "Okänd användare",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.primary.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Manual backup to Drive
                            Button(
                                onClick = {
                                    if (timeEntries.isNotEmpty()) {
                                        isUploadingToDrive = true
                                        coroutineScope.launch {
                                            try {
                                                val localResult = backupManager.createBackup(timeEntries, settings, isAutomatic = false)
                                                if (localResult.isSuccess) {
                                                    val metadata = localResult.getOrNull()
                                                    val driveResult = backupManager.uploadToGoogleDrive(metadata?.fileName ?: "")
                                                    if (driveResult.isSuccess) {
                                                        statusMessage = "Backup skapad och uppladdad till Google Drive"
                                                        backupMetadata = backupManager.getBackupMetadata()
                                                        // Refresh Drive backups list
                                                        val listResult = backupManager.listGoogleDriveBackups()
                                                        if (listResult.isSuccess) {
                                                            driveBackups = listResult.getOrNull() ?: emptyList()
                                                        }
                                                    } else {
                                                        statusMessage = "Lokal backup skapad, men kunde inte ladda upp till Drive: ${driveResult.exceptionOrNull()?.message}"
                                                    }
                                                } else {
                                                    errorMessage = "Kunde inte skapa backup: ${localResult.exceptionOrNull()?.message}"
                                                }
                                            } catch (e: Exception) {
                                                errorMessage = "Fel vid Drive backup: ${e.message}"
                                            } finally {
                                                isUploadingToDrive = false
                                            }
                                        }
                                    } else {
                                        errorMessage = "Inga tidsrapporter att säkerhetskopiera"
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = timeEntries.isNotEmpty() && !isUploadingToDrive
                            ) {
                                if (isUploadingToDrive) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colors.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.CloudUpload,
                                        contentDescription = "Upload",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = if (isUploadingToDrive) "Laddar upp..." else "Backup till Drive",
                                    modifier = Modifier.padding(start = 4.dp),
                                    fontSize = MaterialTheme.typography.caption.fontSize
                                )
                            }

                            // Sign out button
                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        backupManager.disableGoogleDriveBackup()
                                        driveSignedIn = false
                                        driveAccount = null
                                        driveBackups = emptyList()
                                        statusMessage = "Frånkopplad från Google Drive"
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Logout,
                                    contentDescription = "Sign Out",
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Koppla från",
                                    modifier = Modifier.padding(start = 4.dp),
                                    fontSize = MaterialTheme.typography.caption.fontSize
                                )
                            }
                        }

                        // Drive backups list
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Backups i Google Drive:",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isLoadingDriveBackups) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "Hämtar backups...",
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        } else if (driveBackups.isEmpty()) {
                            Text(
                                text = "Inga backups i Google Drive ännu",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        } else {
                            driveBackups.forEach { backup ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    elevation = 1.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CloudDone,
                                            contentDescription = "Drive backup",
                                            tint = MaterialTheme.colors.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = backup.name,
                                                style = MaterialTheme.typography.body2,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = backup.createdTime,
                                                style = MaterialTheme.typography.caption,
                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                                            )
                                        }
                                        TextButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    try {
                                                        val downloadResult = backupManager.downloadFromGoogleDrive(backup.id)
                                                        if (downloadResult.isSuccess) {
                                                            val localFileName = downloadResult.getOrNull() ?: return@launch
                                                            val loadResult = backupManager.loadBackup(localFileName)
                                                            if (loadResult.isSuccess) {
                                                                val (loadedEntries, loadedSettings) = loadResult.getOrNull() ?: return@launch
                                                                pendingImportData = Pair(loadedEntries, loadedSettings)
                                                                showImportDialog = true
                                                            } else {
                                                                errorMessage = "Kunde inte läsa nedladdad backup: ${loadResult.exceptionOrNull()?.message}"
                                                            }
                                                        } else {
                                                            errorMessage = "Kunde inte ladda ner från Drive: ${downloadResult.exceptionOrNull()?.message}"
                                                        }
                                                    } catch (e: Exception) {
                                                        errorMessage = "Fel vid nedladdning: ${e.message}"
                                                    }
                                                }
                                            }
                                        ) {
                                            Text("Ladda ner")
                                        }
                                    }
                                }
                            }
                        }

                        Text(
                            text = "Automatiska backups laddas också upp till Drive när de aktiveras i inställningarna",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 8.dp)
                        )

                    } else {
                        // Not signed in state
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = MaterialTheme.colors.surface,
                            elevation = 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Cloud,
                                        contentDescription = "Not Connected",
                                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Inte ansluten till Google Drive",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Text(
                                    text = "Anslut för att automatiskt säkerhetskopiera till molnet",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                signInLauncher.launch(googleDriveManager.getSignInIntent())
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            )
                        ) {
                            Icon(
                                Icons.Default.Login,
                                contentDescription = "Sign In",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Anslut till Google Drive",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Text(
                            text = "Säker anslutning med ditt Google-konto",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
    
    // Import Dialog
    ImportDialog(
        isOpen = showImportDialog,
        onDismiss = { 
            showImportDialog = false
            pendingImportData = null
        },
        importData = pendingImportData,
        currentEntries = timeEntries,
        currentSettings = settings,
        onConfirm = { finalEntries, finalSettings, importOptions ->
            onTimeEntriesChanged(finalEntries)
            onSettingsChanged(finalSettings)
            
            val importedCount = pendingImportData?.first?.size ?: 0
            val finalCount = finalEntries.size
            val addedCount = when (importOptions.mergeMode) {
                com.example.timereportcalculator.ui.components.MergeMode.REPLACE -> importedCount
                com.example.timereportcalculator.ui.components.MergeMode.APPEND -> importedCount
                com.example.timereportcalculator.ui.components.MergeMode.MERGE -> finalCount - timeEntries.size
            }
            
            statusMessage = when {
                importOptions.importEntries && importOptions.importSettings -> 
                    "Import slutförd: $addedCount poster och inställningar"
                importOptions.importEntries -> 
                    "Import slutförd: $addedCount poster"
                importOptions.importSettings -> 
                    "Import slutförd: endast inställningar"
                else -> "Import slutförd"
            }
            
            savedFiles = fileManager.getSavedFiles()
            showImportDialog = false
            pendingImportData = null
        }
    )
    
    // Backup Settings Dialog
    BackupSettingsDialog(
        isOpen = showBackupSettingsDialog,
        onDismiss = { showBackupSettingsDialog = false },
        currentSettings = backupManager.getBackupSettings(),
        onSettingsChanged = { newSettings ->
            backupManager.updateBackupSettings(newSettings)
            statusMessage = "Backup-inställningar sparade"
        }
    )
}