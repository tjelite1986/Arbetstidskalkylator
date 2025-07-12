package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.example.timereportcalculator.R
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.WorkShiftTemplate
import com.example.timereportcalculator.data.SwedishHolidayCalculator
import com.example.timereportcalculator.calculator.PayCalculator
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

@Composable
fun TimeEntryCard(
    entry: TimeEntry,
    settings: Settings = Settings(),
    onEntryChange: (TimeEntry) -> Unit,
    onDelete: (TimeEntry) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showBreakStartTimePicker by remember { mutableStateOf(false) }
    var showBreakEndTimePicker by remember { mutableStateOf(false) }
    var showWorkShiftTemplateDialog by remember { mutableStateOf(false) }
    var breakMinutesText by remember { mutableStateOf(entry.breakMinutes.toString()) }
    
    val dayOfWeek = entry.date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale("sv", "SE"))
    
    // Automatisk helgdagsdetektering
    val isSwedishHoliday = if (settings.automaticHolidayDetection) {
        SwedishHolidayCalculator.isSwedishHoliday(entry.date)
    } else {
        false
    }
    
    // Beräkna automatiska raster om automatiska raster är aktiverat
    val calculatedBreakMinutes = if (settings.workTimeSettings.automaticBreaks && 
                                     entry.breakStart == null && entry.breakEnd == null && 
                                     entry.breakMinutes == 0 &&
                                     entry.startTime != null && entry.endTime != null) {
        val totalMinutes = ChronoUnit.MINUTES.between(entry.startTime, entry.endTime)
        val initialWorkHours = totalMinutes / 60.0
        
        when {
            initialWorkHours >= settings.workTimeSettings.thirdBreakThreshold -> settings.workTimeSettings.thirdBreakMinutes
            initialWorkHours >= settings.workTimeSettings.secondBreakThreshold -> settings.workTimeSettings.secondBreakMinutes
            initialWorkHours >= settings.workTimeSettings.firstBreakThreshold -> settings.workTimeSettings.firstBreakMinutes
            else -> 0
        }
    } else {
        entry.breakMinutes
    }
    
    // Uppdatera breakMinutesText när automatiska raster beräknas
    LaunchedEffect(calculatedBreakMinutes) {
        if (calculatedBreakMinutes != entry.breakMinutes && calculatedBreakMinutes > 0) {
            breakMinutesText = calculatedBreakMinutes.toString()
            onEntryChange(entry.copy(breakMinutes = calculatedBreakMinutes))
        }
    }
    
    // Synkronisera breakMinutesText med entry.breakMinutes
    LaunchedEffect(entry.breakMinutes) {
        breakMinutesText = entry.breakMinutes.toString()
    }
    
    // Automatiskt markera som röd dag om det är helgdag
    LaunchedEffect(isSwedishHoliday) {
        if (isSwedishHoliday && !entry.isRedDay) {
            onEntryChange(entry.copy(isRedDay = true))
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with expand/collapse functionality
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$dayOfWeek ${entry.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Show summary info when collapsed
                    if (!isExpanded) {
                        Text(
                            text = "${entry.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "--:--"} - ${entry.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "--:--"} | ${String.format("%.1f", entry.workHours)}h | ${String.format("%.0f", entry.totalPay)} kr",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        
                        // Show status badges
                        Row {
                            if (entry.isRedDay) {
                                Text(
                                    text = "Röd dag",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.Red,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            if (entry.isSickDay) {
                                Text(
                                    text = "Sjukdag",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.Blue
                                )
                            }
                        }
                    }
                }
                
                Row {
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Minimera" else "Expandera"
                        )
                    }
                    
                    IconButton(onClick = { onDelete(entry) }) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = stringResource(R.string.remove),
                            tint = MaterialTheme.colors.error
                        )
                    }
                }
            }
            
            // Only show detailed content when expanded
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Date picker button
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Välj datum",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ändra datum: ${entry.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Checkboxes row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = entry.isRedDay,
                        onCheckedChange = { isChecked ->
                            onEntryChange(entry.copy(isRedDay = isChecked))
                        }
                    )
                    Text(stringResource(R.string.red_day))
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = entry.isSickDay,
                        onCheckedChange = { isChecked ->
                            onEntryChange(entry.copy(isSickDay = isChecked))
                        }
                    )
                    Text(stringResource(R.string.sick_day))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Time inputs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Start time button
                OutlinedButton(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Välj starttid",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = entry.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Starttid",
                        maxLines = 1
                    )
                }
                
                // End time button
                OutlinedButton(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Välj sluttid",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = entry.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Sluttid",
                        maxLines = 1
                    )
                }
            }
            
            // Work shift template button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { showWorkShiftTemplateDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = "Arbetspass-mallar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Använd mall")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Break time inputs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Break start button
                OutlinedButton(
                    onClick = { showBreakStartTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Välj rast start",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = entry.breakStart?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Rast start",
                        maxLines = 1
                    )
                }
                
                // Break end button
                OutlinedButton(
                    onClick = { showBreakEndTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Välj rast slut",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = entry.breakEnd?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Rast slut",
                        maxLines = 1
                    )
                }
                
                // Break minutes text field
                OutlinedTextField(
                    value = breakMinutesText,
                    onValueChange = { newValue ->
                        breakMinutesText = newValue
                        try {
                            val minutes = newValue.toIntOrNull() ?: 0
                            onEntryChange(entry.copy(breakMinutes = minutes))
                        } catch (e: NumberFormatException) {
                            // Invalid number format, don't update
                        }
                    },
                    label = { Text(stringResource(R.string.break_minutes)) },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Results display
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Resultat",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Arbetstid:")
                        Text("${String.format("%.2f", entry.workHours)} tim")
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Grundlön:")
                        Text("${String.format("%.2f", entry.basePay)} kr")
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("OB-tillägg:")
                        Text("${String.format("%.2f", entry.obPay)} kr")
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Totalt brutto:")
                        Text("${String.format("%.2f", entry.totalPay)} kr", fontWeight = FontWeight.Bold)
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Skatt:")
                        Text("${String.format("%.2f", entry.taxAmount)} kr")
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Netto:")
                        Text("${String.format("%.2f", entry.netPay)} kr", fontWeight = FontWeight.Bold)
                    }
                }
            }
            } // End of isExpanded block
        }
    }
    
    // Show DatePicker dialog when requested
    if (showDatePicker) {
        DatePickerDialog(
            selectedDate = entry.date,
            onDateSelected = { selectedDate ->
                onEntryChange(entry.copy(date = selectedDate))
            },
            onDismiss = { showDatePicker = false }
        )
    }
    
    // Show TimePicker dialogs
    if (showStartTimePicker) {
        TimePickerDialog(
            selectedTime = entry.startTime,
            onTimeSelected = { selectedTime ->
                onEntryChange(entry.copy(startTime = selectedTime))
            },
            onDismiss = { showStartTimePicker = false },
            title = "Välj starttid"
        )
    }
    
    if (showEndTimePicker) {
        TimePickerDialog(
            selectedTime = entry.endTime,
            onTimeSelected = { selectedTime ->
                onEntryChange(entry.copy(endTime = selectedTime))
            },
            onDismiss = { showEndTimePicker = false },
            title = "Välj sluttid"
        )
    }
    
    if (showBreakStartTimePicker) {
        TimePickerDialog(
            selectedTime = entry.breakStart,
            onTimeSelected = { selectedTime ->
                onEntryChange(entry.copy(breakStart = selectedTime))
            },
            onDismiss = { showBreakStartTimePicker = false },
            title = "Välj rast start"
        )
    }
    
    if (showBreakEndTimePicker) {
        TimePickerDialog(
            selectedTime = entry.breakEnd,
            onTimeSelected = { selectedTime ->
                onEntryChange(entry.copy(breakEnd = selectedTime))
            },
            onDismiss = { showBreakEndTimePicker = false },
            title = "Välj rast slut"
        )
    }
    
    if (showWorkShiftTemplateDialog) {
        WorkShiftTemplateDialog(
            isOpen = showWorkShiftTemplateDialog,
            onDismiss = { showWorkShiftTemplateDialog = false },
            onTemplateSelected = { template ->
                onEntryChange(
                    entry.copy(
                        startTime = template.startTime,
                        endTime = template.endTime,
                        breakMinutes = template.breakMinutes
                    )
                )
                breakMinutesText = template.breakMinutes.toString()
            }
        )
    }
}