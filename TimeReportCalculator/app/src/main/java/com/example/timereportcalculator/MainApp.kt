package com.example.timereportcalculator

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timereportcalculator.navigation.NavigationItem
import com.example.timereportcalculator.navigation.bottomNavigationItems
import com.example.timereportcalculator.ui.components.SmoothBottomBar
import com.example.timereportcalculator.ui.screens.*
import kotlinx.coroutines.launch

@Composable
fun MainApp() {
    val navController = rememberNavController()
    var currentDestination by remember { mutableStateOf<NavigationItem>(NavigationItem.Home) }
    
    // Persistent state for the entire app - auto-saves/loads from files
    val context = androidx.compose.ui.platform.LocalContext.current
    val fileManager = remember { com.example.timereportcalculator.export.FileManager(context) }
    
    var sharedTimeEntries by remember { mutableStateOf(listOf<com.example.timereportcalculator.data.TimeEntry>()) }
    var sharedSettings by remember { mutableStateOf(com.example.timereportcalculator.data.Settings()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Shared WorkSessionManager to persist across tab switches
    val sharedSessionManager = remember { 
        com.example.timereportcalculator.timer.WorkSessionManager.getInstance()
    }
    
    // Auto-load data on app start
    LaunchedEffect(Unit) {
        try {
            val autoSaveFile = fileManager.getSavedFiles().firstOrNull { it.startsWith("autosave_") }
            if (autoSaveFile != null) {
                val result = fileManager.loadFromJson(autoSaveFile)
                if (result.isSuccess) {
                    val (entries, settings) = result.getOrNull() ?: return@LaunchedEffect
                    sharedTimeEntries = entries
                    sharedSettings = settings
                }
            }
        } catch (e: Exception) {
            // Continue with empty state if loading fails
        } finally {
            isLoading = false
        }
    }
    
    // Listen for completed Live Timer sessions
    LaunchedEffect(Unit) {
        sharedSessionManager.completedSessions.collect { completedEntry ->
            if (completedEntry != null) {
                // Add completed session to shared entries
                sharedTimeEntries = (sharedTimeEntries + completedEntry).sortedByDescending { it.date }
            }
        }
    }
    
    // Auto-save data whenever it changes
    LaunchedEffect(sharedTimeEntries, sharedSettings) {
        if (!isLoading && (sharedTimeEntries.isNotEmpty() || sharedSettings != com.example.timereportcalculator.data.Settings())) {
            try {
                // Share settings with notification service for earnings calculation
                com.example.timereportcalculator.timer.TimerNotificationService.sharedSettings = sharedSettings
                
                // Auto-save with timestamp
                val result = fileManager.saveToJson(sharedTimeEntries, sharedSettings)
                if (result.isSuccess) {
                    val fileName = result.getOrNull()
                    if (fileName != null) {
                        // Rename to autosave format for easy identification
                        val autoSaveFileName = "autosave_${java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))}.json"
                        val oldFile = java.io.File(context.filesDir, "timereports/$fileName")
                        val newFile = java.io.File(context.filesDir, "timereports/$autoSaveFileName")
                        oldFile.renameTo(newFile)
                        
                        // Keep only the latest 3 autosave files
                        val autosaveFiles = fileManager.getSavedFiles()
                            .filter { it.startsWith("autosave_") }
                            .sortedDescending()
                        autosaveFiles.drop(3).forEach { oldFileName ->
                            fileManager.deleteSavedFile(oldFileName)
                        }
                    }
                }
            } catch (e: Exception) {
                // Silently fail auto-save to not disrupt user experience
            }
        }
    }
    
    Scaffold(
        bottomBar = {
            SmoothBottomBar(
                items = bottomNavigationItems,
                selectedItem = currentDestination,
                onItemSelected = { item ->
                    currentDestination = item
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationItem.Home.route) {
                currentDestination = NavigationItem.Home
                HomeScreen()
            }
            
            composable(NavigationItem.LiveTimer.route) {
                currentDestination = NavigationItem.LiveTimer
                LiveTimerScreen(
                    timeEntries = sharedTimeEntries,
                    settings = sharedSettings,
                    sessionManager = sharedSessionManager,
                    onTimeEntriesChanged = { timeEntries ->
                        // Sort entries by date when updating
                        sharedTimeEntries = timeEntries.sortedByDescending { it.date }
                    },
                    onSettingsChanged = { settings ->
                        sharedSettings = settings
                    }
                )
            }
            
            composable(NavigationItem.TimeReport.route) {
                currentDestination = NavigationItem.TimeReport
                TimeReportApp(
                    timeEntries = sharedTimeEntries,
                    settings = sharedSettings,
                    onTimeEntriesChanged = { timeEntries ->
                        // Sort entries by date when updating
                        sharedTimeEntries = timeEntries.sortedByDescending { it.date }
                    },
                    onSettingsChanged = { settings ->
                        sharedSettings = settings
                    }
                )
            }
            
            composable(NavigationItem.Statistics.route) {
                currentDestination = NavigationItem.Statistics
                StatisticsScreen(
                    timeEntries = sharedTimeEntries,
                    settings = sharedSettings
                )
            }
            
            composable(NavigationItem.WeeklySchedule.route) {
                currentDestination = NavigationItem.WeeklySchedule
                WeeklyScheduleScreen()
            }
            
            composable(NavigationItem.Export.route) {
                currentDestination = NavigationItem.Export
                ExportScreen(
                    timeEntries = sharedTimeEntries,
                    settings = sharedSettings,
                    onTimeEntriesChanged = { timeEntries ->
                        // Sort entries by date when updating
                        sharedTimeEntries = timeEntries.sortedByDescending { it.date }
                    },
                    onSettingsChanged = { settings ->
                        sharedSettings = settings
                    }
                )
            }
            
            composable(NavigationItem.Settings.route) {
                currentDestination = NavigationItem.Settings
                SettingsScreen(
                    settings = sharedSettings,
                    onSettingsChange = { settings ->
                        sharedSettings = settings
                    }
                )
            }
        }
    }
}