package com.example.timereportcalculator.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth  
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon
) {
    object Home : NavigationItem(
        route = "home",
        title = "Start",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    
    object LiveTimer : NavigationItem(
        route = "live_timer",
        title = "Live Timer",
        icon = Icons.Outlined.Timer,
        selectedIcon = Icons.Filled.Timer
    )
    
    object TimeReport : NavigationItem(
        route = "time_report",
        title = "Tidrapport",
        icon = Icons.Outlined.AccessTime,
        selectedIcon = Icons.Filled.AccessTime
    )
    
    object Statistics : NavigationItem(
        route = "statistics", 
        title = "Kalender",
        icon = Icons.Outlined.CalendarMonth,
        selectedIcon = Icons.Filled.CalendarMonth
    )
    
    object WeeklySchedule : NavigationItem(
        route = "weekly_schedule",
        title = "Veckoschema",
        icon = Icons.Outlined.DateRange,
        selectedIcon = Icons.Filled.DateRange
    )
    
    object Export : NavigationItem(
        route = "export",
        title = "Export",
        icon = Icons.Outlined.FileUpload,
        selectedIcon = Icons.Filled.FileUpload
    )
    
    object Settings : NavigationItem(
        route = "settings",
        title = "Inställningar",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )
}

val bottomNavigationItems = listOf(
    NavigationItem.Home,
    NavigationItem.LiveTimer,
    NavigationItem.TimeReport,
    NavigationItem.Statistics,
    NavigationItem.Export,
    NavigationItem.Settings
)