package com.example.timereportcalculator

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timereportcalculator.navigation.NavigationItem
import com.example.timereportcalculator.navigation.bottomNavigationItems
import com.example.timereportcalculator.ui.components.SmoothBottomBar
import com.example.timereportcalculator.ui.screens.*

@Composable
fun MainApp() {
    val navController = rememberNavController()
    var currentDestination by remember { mutableStateOf<NavigationItem>(NavigationItem.Home) }
    
    // Shared state for the entire app - preserves data between tab switches
    var sharedTimeEntries by remember { mutableStateOf(listOf<com.example.timereportcalculator.data.TimeEntry>()) }
    var sharedSettings by remember { mutableStateOf(com.example.timereportcalculator.data.Settings()) }
    
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
            
            composable(NavigationItem.TimeReport.route) {
                currentDestination = NavigationItem.TimeReport
                TimeReportApp(
                    timeEntries = sharedTimeEntries,
                    settings = sharedSettings,
                    onTimeEntriesChanged = { timeEntries ->
                        sharedTimeEntries = timeEntries
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
            
            composable(NavigationItem.Templates.route) {
                currentDestination = NavigationItem.Templates
                TemplatesScreen()
            }
            
            composable(NavigationItem.Export.route) {
                currentDestination = NavigationItem.Export
                ExportScreen()
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