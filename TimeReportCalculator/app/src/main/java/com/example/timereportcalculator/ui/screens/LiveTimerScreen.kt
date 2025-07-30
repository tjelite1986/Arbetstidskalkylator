package com.example.timereportcalculator.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.timer.TimerNotificationService
import com.example.timereportcalculator.timer.WorkSessionManager
import com.example.timereportcalculator.ui.components.LiveTimerCard
import com.example.timereportcalculator.ui.components.AddDayDialog

@Composable
fun LiveTimerScreen(
    timeEntries: List<TimeEntry> = emptyList(),
    settings: Settings = Settings(),
    sessionManager: WorkSessionManager = WorkSessionManager.getInstance(),
    onTimeEntriesChanged: (List<TimeEntry>) -> Unit = {},
    onSettingsChanged: (Settings) -> Unit = {}
) {
    val context = LocalContext.current
    
    var showAddDayDialog by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<TimeEntry?>(null) }
    var showLiveTimerHistory by remember { mutableStateOf(false) }
    
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
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = "Live Timer",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Live Arbetstimer",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "Spåra din arbetstid i realtid med automatiska beräkningar",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        
        item {
            // Live Timer Card
            LiveTimerCard(
                sessionManager = sessionManager,
                settings = settings,
                onStartShift = {
                    try {
                        sessionManager.startWorkSession()
                        startTimerService(context)
                    } catch (e: Exception) {
                        android.util.Log.e("LiveTimerScreen", "Failed to start work session", e)
                        // Could show a toast or snackbar here
                    }
                },
                onStopShift = {
                    val completedEntry = sessionManager.stopWorkSession()
                    completedEntry?.let { entry ->
                        // Add the completed entry and sort by date
                        val updatedEntries = (timeEntries + entry).sortedByDescending { it.date }
                        onTimeEntriesChanged(updatedEntries)
                    }
                    stopTimerService(context)
                },
                onStartBreak = {
                    sessionManager.startBreak()
                },
                onEndBreak = {
                    sessionManager.endBreak()
                }
            )
        }
        
        item {
            // Quick Actions
            QuickActionsCard(
                onAddManualEntry = { showAddDayDialog = true },
                onViewHistory = { showLiveTimerHistory = true }
            )
        }
        
        item {
            // Recent entries preview - only show Live Timer entries
            val liveTimerEntries = timeEntries.filter { it.isFromLiveTimer }
            if (liveTimerEntries.isNotEmpty()) {
                RecentEntriesCard(
                    entries = liveTimerEntries.sortedByDescending { it.date }.take(3),
                    onEntryClick = { entry ->
                        selectedEntry = entry
                        showAddDayDialog = true
                    }
                )
            }
        }
        
        item {
            // Tips and information
            TimerTipsCard()
        }
    }
    
    // Add/Edit Day Dialog
    if (showAddDayDialog) {
        AddDayDialog(
            isOpen = showAddDayDialog,
            settings = settings,
            onDismiss = {
                showAddDayDialog = false
                selectedEntry = null
            },
            onConfirm = { newEntry ->
                if (selectedEntry != null) {
                    // Edit existing entry
                    val updatedEntries = timeEntries.map { entry ->
                        if (entry.id == selectedEntry!!.id) newEntry else entry
                    }.sortedByDescending { it.date }
                    onTimeEntriesChanged(updatedEntries)
                } else {
                    // Add new entry and sort by date
                    val updatedEntries = (timeEntries + newEntry).sortedByDescending { it.date }
                    onTimeEntriesChanged(updatedEntries)
                }
                showAddDayDialog = false
                selectedEntry = null
            }
        )
    }
    
    // Live Timer History Dialog
    if (showLiveTimerHistory) {
        LiveTimerHistoryDialog(
            isOpen = showLiveTimerHistory,
            entries = timeEntries.filter { it.isFromLiveTimer }.sortedByDescending { it.date },
            onDismiss = { showLiveTimerHistory = false },
            onEntryClick = { entry ->
                selectedEntry = entry
                showLiveTimerHistory = false
                showAddDayDialog = true
            }
        )
    }
}

@Composable
private fun QuickActionsCard(
    onAddManualEntry: () -> Unit,
    onViewHistory: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Snabbåtgärder",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onAddManualEntry,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Lägg till manuellt")
                }
                
                OutlinedButton(
                    onClick = onViewHistory,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Visa historik")
                }
            }
        }
    }
}

@Composable
private fun RecentEntriesCard(
    entries: List<TimeEntry>,
    onEntryClick: (TimeEntry) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Senaste aktiviteter",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            entries.forEach { entry ->
                RecentEntryItem(
                    entry = entry,
                    onClick = { onEntryClick(entry) }
                )
                if (entry != entries.last()) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun RecentEntryItem(
    entry: TimeEntry,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = entry.date.toString(),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "${String.format("%.1f", entry.workHours)}h • ${String.format("%.0f", entry.totalPay)} kr",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun TimerTipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.05f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tips för bästa resultat",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val tips = listOf(
                "Starta timern innan du börjar arbeta för exakt spårning",
                "Ta regelbundna raster - appen påminner dig automatiskt",
                "OB-satser beräknas automatiskt baserat på aktuell tid",
                "Notifikationer låter dig kontrollera timern utan att öppna appen"
            )
            
            tips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(top = 2.dp, end = 8.dp)
                    )
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

private fun startTimerService(context: Context) {
    try {
        TimerNotificationService.createNotificationChannel(context)
        val intent = Intent(context, TimerNotificationService::class.java).apply {
            action = TimerNotificationService.ACTION_START_TIMER
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // If service fails, continue with UI-only timer
    }
}

private fun stopTimerService(context: Context) {
    val intent = Intent(context, TimerNotificationService::class.java).apply {
        action = TimerNotificationService.ACTION_STOP_TIMER
    }
    context.startService(intent)
}

@Composable
private fun LiveTimerHistoryDialog(
    isOpen: Boolean,
    entries: List<TimeEntry>,
    onDismiss: () -> Unit,
    onEntryClick: (TimeEntry) -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "Live Timer Historik",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )
                }
            },
            text = {
                if (entries.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Inga Live Timer-pass än",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Starta din första timer för att se historik här!",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(entries) { entry ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onEntryClick(entry) },
                                elevation = 2.dp,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = entry.date.toString(),
                                            style = MaterialTheme.typography.subtitle1,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Surface(
                                            color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = "Live Timer",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.caption,
                                                color = MaterialTheme.colors.primary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(4.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${String.format("%.1f", entry.workHours)} timmar",
                                            style = MaterialTheme.typography.body2,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = "${String.format("%.0f", entry.totalPay)} kr",
                                            style = MaterialTheme.typography.body2,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colors.primary
                                        )
                                    }
                                    
                                    if (entry.startTime != null && entry.endTime != null) {
                                        Text(
                                            text = "${entry.startTime} - ${entry.endTime}",
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Stäng")
                }
            }
        )
    }
}