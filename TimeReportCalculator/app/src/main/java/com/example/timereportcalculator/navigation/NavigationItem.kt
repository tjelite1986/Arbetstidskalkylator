package com.example.timereportcalculator.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : NavigationItem(
        route = "home",
        title = "Start",
        icon = Icons.Default.Home
    )
    
    object TimeReport : NavigationItem(
        route = "time_report",
        title = "Tidrapport",
        icon = Icons.Default.AccessTime
    )
    
    object Statistics : NavigationItem(
        route = "statistics", 
        title = "Kalender",
        icon = Icons.Default.CalendarMonth
    )
    
    object Templates : NavigationItem(
        route = "templates",
        title = "Mallar", 
        icon = Icons.Default.Schedule
    )
    
    object Export : NavigationItem(
        route = "export",
        title = "Export",
        icon = Icons.Default.FileUpload
    )
    
    object Settings : NavigationItem(
        route = "settings",
        title = "Inställningar",
        icon = Icons.Default.Settings
    )
}

val bottomNavigationItems = listOf(
    NavigationItem.Home,
    NavigationItem.TimeReport,
    NavigationItem.Statistics,
    NavigationItem.Templates,
    NavigationItem.Export,
    NavigationItem.Settings
)