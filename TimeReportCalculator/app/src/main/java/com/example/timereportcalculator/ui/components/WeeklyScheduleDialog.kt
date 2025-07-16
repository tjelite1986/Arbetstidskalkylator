package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.timereportcalculator.data.WeeklySchedule
import com.example.timereportcalculator.data.WeeklyScheduleEntry
import com.example.timereportcalculator.data.WorkShiftTemplate
import com.example.timereportcalculator.data.WorkShiftTemplateManager
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WeeklyScheduleDialog(
    schedule: WeeklySchedule?,
    onDismiss: () -> Unit,
    onSave: (WeeklySchedule) -> Unit
) {
    var name by remember { mutableStateOf(schedule?.name ?: "Nytt Schema") }
    var entries by remember { mutableStateOf(schedule?.entries ?: WeeklySchedule.getDefaultWeeklyEntries()) }
    var selectedEntry by remember { mutableStateOf<WeeklyScheduleEntry?>(null) }
    var showTimeEditDialog by remember { mutableStateOf(false) }
    var showTemplateSelector by remember { mutableStateOf(false) }
    var hasErrors by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val templateManager = remember { WorkShiftTemplateManager(context) }
    val templates = remember { mutableStateOf(templateManager.getAllTemplates()) }
    
    fun validateAndSave() {
        if (name.isBlank()) {
            hasErrors = true
            errorMessage = "Namn på schema krävs"
            return
        }
        
        val newSchedule = WeeklySchedule(
            id = schedule?.id ?: java.util.UUID.randomUUID().toString(),
            name = name,
            entries = entries,
            isActive = schedule?.isActive ?: true,
            createdAt = schedule?.createdAt ?: System.currentTimeMillis()
        )
        
        onSave(newSchedule)
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (schedule == null) "Skapa Schema" else "Redigera Schema",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Stäng",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Error message
                if (hasErrors) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        backgroundColor = Color(0xFFFFEBEE),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Varning",
                                tint = Color(0xFFE57373),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage,
                                color = Color(0xFFD32F2F),
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
                
                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        hasErrors = false
                    },
                    label = { Text("Namn på schema") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        cursorColor = MaterialTheme.colors.primary
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Template selector button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { showTemplateSelector = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Använd mall",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Använd mall")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    TextButton(
                        onClick = {
                            entries = WeeklySchedule.getDefaultWeeklyEntries()
                        }
                    ) {
                        Text("Återställ")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Weekly schedule entries
                Text(
                    text = "Veckosschema",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(entries) { entry ->
                            WeeklyScheduleEntryRow(
                                entry = entry,
                                onEntryClick = {
                                    selectedEntry = entry
                                    showTimeEditDialog = true
                                },
                                onToggleEnabled = { enabled ->
                                    val updatedEntries = entries.map { e ->
                                        if (e.dayOfWeek == entry.dayOfWeek) {
                                            e.copy(isEnabled = enabled)
                                        } else e
                                    }
                                    entries = updatedEntries
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total tid per vecka:",
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${String.format("%.1f", entries.sumOf { it.getWorkHours() })} timmar",
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary
                            )
                        }
                        
                        Text(
                            text = "Arbetsdagar: ${entries.count { it.isEnabled && it.startTime != null }}",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Avbryt")
                    }
                    
                    Button(
                        onClick = { validateAndSave() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Spara")
                    }
                }
            }
        }
    }
    
    // Time edit dialog
    if (showTimeEditDialog && selectedEntry != null) {
        WeeklyScheduleTimeEditDialog(
            entry = selectedEntry!!,
            onDismiss = { showTimeEditDialog = false },
            onSave = { updatedEntry ->
                val updatedEntries = entries.map { e ->
                    if (e.dayOfWeek == updatedEntry.dayOfWeek) {
                        updatedEntry
                    } else e
                }
                entries = updatedEntries
                showTimeEditDialog = false
                selectedEntry = null
            }
        )
    }
    
    // Template selector dialog
    if (showTemplateSelector) {
        TemplateSelectionDialog(
            templates = templates.value,
            onDismiss = { showTemplateSelector = false },
            onTemplateSelected = { template ->
                // Apply template to all enabled days
                val updatedEntries = entries.map { entry ->
                    if (entry.isEnabled) {
                        entry.copy(
                            startTime = template.startTime,
                            endTime = template.endTime,
                            breakMinutes = template.breakMinutes,
                            template = template
                        )
                    } else entry
                }
                entries = updatedEntries
                showTemplateSelector = false
            }
        )
    }
}

@Composable
private fun WeeklyScheduleEntryRow(
    entry: WeeklyScheduleEntry,
    onEntryClick: () -> Unit,
    onToggleEnabled: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEntryClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Switch(
                checked = entry.isEnabled,
                onCheckedChange = onToggleEnabled,
                modifier = Modifier.size(32.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                    checkedTrackColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = entry.getDayName(),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium,
                color = if (entry.isEnabled) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = entry.getDisplayText(),
                style = MaterialTheme.typography.body2,
                color = if (entry.isEnabled) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            
            if (entry.isEnabled) {
                Text(
                    text = "${String.format("%.1f", entry.getWorkHours())}h",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun WeeklyScheduleTimeEditDialog(
    entry: WeeklyScheduleEntry,
    onDismiss: () -> Unit,
    onSave: (WeeklyScheduleEntry) -> Unit
) {
    var startTime by remember { mutableStateOf(entry.startTime ?: LocalTime.of(8, 0)) }
    var endTime by remember { mutableStateOf(entry.endTime ?: LocalTime.of(17, 0)) }
    var breakMinutes by remember { mutableStateOf(entry.breakMinutes.toString()) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = entry.getDayName(),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Start time
                OutlinedTextField(
                    value = startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = { },
                    label = { Text("Starttid") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartTimePicker = true },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showStartTimePicker = true }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Välj starttid")
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // End time
                OutlinedTextField(
                    value = endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = { },
                    label = { Text("Sluttid") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndTimePicker = true },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showEndTimePicker = true }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Välj sluttid")
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Break minutes
                OutlinedTextField(
                    value = breakMinutes,
                    onValueChange = { breakMinutes = it },
                    label = { Text("Rast (minuter)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Avbryt")
                    }
                    
                    Button(
                        onClick = {
                            val updatedEntry = entry.copy(
                                startTime = startTime,
                                endTime = endTime,
                                breakMinutes = breakMinutes.toIntOrNull() ?: 0,
                                isEnabled = true
                            )
                            onSave(updatedEntry)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Spara")
                    }
                }
            }
        }
    }
    
    // Time pickers
    if (showStartTimePicker) {
        TimePickerDialog(
            selectedTime = startTime,
            onTimeSelected = { 
                startTime = it
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false }
        )
    }
    
    if (showEndTimePicker) {
        TimePickerDialog(
            selectedTime = endTime,
            onTimeSelected = { 
                endTime = it
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false }
        )
    }
}

@Composable
private fun TemplateSelectionDialog(
    templates: List<WorkShiftTemplate>,
    onDismiss: () -> Unit,
    onTemplateSelected: (WorkShiftTemplate) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Välj mall",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn {
                    items(templates) { template ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onTemplateSelected(template) },
                            elevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = template.name,
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = template.getDisplayText(),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Avbryt")
                }
            }
        }
    }
}