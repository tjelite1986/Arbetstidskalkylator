package com.example.timereportcalculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timereportcalculator.data.WeeklySchedule
import com.example.timereportcalculator.data.WeeklyScheduleEntry
import com.example.timereportcalculator.data.WeeklyScheduleManager
import com.example.timereportcalculator.ui.components.WeeklyScheduleDialog
import java.time.DayOfWeek

@Composable
fun WeeklyScheduleScreen() {
    val context = LocalContext.current
    val scheduleManager = remember { WeeklyScheduleManager(context) }
    
    var schedules by remember { mutableStateOf(scheduleManager.getAllSchedules()) }
    var activeSchedule by remember { mutableStateOf(scheduleManager.getActiveSchedule()) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedSchedule by remember { mutableStateOf<WeeklySchedule?>(null) }
    
    LaunchedEffect(Unit) {
        schedules = scheduleManager.getAllSchedules()
        activeSchedule = scheduleManager.getActiveSchedule()
    }
    
    fun refreshSchedules() {
        schedules = scheduleManager.getAllSchedules()
        activeSchedule = scheduleManager.getActiveSchedule()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Veckoscheman",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Hantera dina arbetsscheman",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
            
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier.size(48.dp),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Skapa nytt schema",
                    tint = Color.White
                )
            }
        }
        
        // Active schedule card
        activeSchedule?.let { schedule ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { 
                        selectedSchedule = schedule
                        showEditDialog = true
                    },
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = schedule.name,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Aktivt schema",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Aktivt",
                                style = MaterialTheme.typography.caption,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Totalt: ${String.format("%.1f", schedule.getTotalWeeklyHours())} timmar/vecka",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Week overview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DayOfWeek.values().forEach { dayOfWeek ->
                            val entry = schedule.getEntryForDay(dayOfWeek)
                            WeekDayCard(
                                entry = entry,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        
        // All schedules list
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Alla scheman",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = "${schedules.size} scheman",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                if (schedules.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = "Inga scheman",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Inga scheman än",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(schedules) { schedule ->
                            ScheduleListItem(
                                schedule = schedule,
                                isActive = schedule.id == activeSchedule?.id,
                                onEdit = { 
                                    selectedSchedule = schedule
                                    showEditDialog = true
                                },
                                onActivate = { 
                                    scheduleManager.setActiveSchedule(schedule.id)
                                    refreshSchedules()
                                },
                                onDelete = { 
                                    scheduleManager.deleteSchedule(schedule.id)
                                    refreshSchedules()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Dialogs
    if (showCreateDialog) {
        WeeklyScheduleDialog(
            schedule = null,
            onDismiss = { showCreateDialog = false },
            onSave = { schedule ->
                scheduleManager.saveSchedule(schedule)
                refreshSchedules()
                showCreateDialog = false
            }
        )
    }
    
    if (showEditDialog && selectedSchedule != null) {
        WeeklyScheduleDialog(
            schedule = selectedSchedule,
            onDismiss = { 
                showEditDialog = false
                selectedSchedule = null
            },
            onSave = { schedule ->
                scheduleManager.saveSchedule(schedule)
                refreshSchedules()
                showEditDialog = false
                selectedSchedule = null
            }
        )
    }
}

@Composable
private fun WeekDayCard(
    entry: WeeklyScheduleEntry?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = entry?.getShortDayName() ?: "",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Medium
        )
        
        Box(
            modifier = Modifier
                .size(40.dp, 24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    if (entry?.isEnabled == true) 
                        MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    else 
                        MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (entry?.isEnabled == true) {
                Text(
                    text = "${String.format("%.1f", entry.getWorkHours())}h",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                )
            } else {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
private fun ScheduleListItem(
    schedule: WeeklySchedule,
    isActive: Boolean,
    onEdit: () -> Unit,
    onActivate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        elevation = 1.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = if (isActive) MaterialTheme.colors.primary.copy(alpha = 0.05f) else MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = schedule.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
                
                Text(
                    text = "${String.format("%.1f", schedule.getTotalWeeklyHours())} timmar/vecka • ${schedule.getWorkingDays().size} arbetsdagar",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isActive) {
                    IconButton(
                        onClick = onActivate,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Aktivera schema",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Aktivt schema",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Ta bort schema",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}