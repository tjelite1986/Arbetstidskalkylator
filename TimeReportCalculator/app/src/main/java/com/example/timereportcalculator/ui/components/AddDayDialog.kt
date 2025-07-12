package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddDayDialog(
    isOpen: Boolean,
    settings: Settings,
    onDismiss: () -> Unit,
    onConfirm: (TimeEntry) -> Unit
) {
    if (!isOpen) return
    
    // State för alla fält
    var date by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(settings.workTimeSettings.defaultStartTime) }
    var endTime by remember { mutableStateOf(settings.workTimeSettings.defaultEndTime) }
    var breakStartTime by remember { mutableStateOf("") }
    var breakEndTime by remember { mutableStateOf("") }
    var breakMinutes by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var useAutomaticBreaks by remember { mutableStateOf(settings.workTimeSettings.automaticBreaks) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showBreakStartTimePicker by remember { mutableStateOf(false) }
    var showBreakEndTimePicker by remember { mutableStateOf(false) }
    
    // Validation state
    var hasErrors by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
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
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📅 Lägg till ny arbetsdag",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Stäng")
                    }
                }
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Datum-sektion
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "📅 Datum",
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Klickbart datum för att öppna kalender
                                OutlinedButton(
                                    onClick = { showDatePicker = true },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = "Välj datum",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                                
                                Row {
                                    IconButton(
                                        onClick = { date = date.minusDays(1) }
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Föregående dag")
                                    }
                                    
                                    IconButton(
                                        onClick = { date = LocalDate.now() }
                                    ) {
                                        Icon(Icons.Default.Today, contentDescription = "Idag")
                                    }
                                    
                                    IconButton(
                                        onClick = { date = date.plusDays(1) }
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Nästa dag")
                                    }
                                }
                            }
                        }
                    }
                    
                    // Arbetstider-sektion
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "⏰ Arbetstider",
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondary
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Klickbar starttid
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Starttid",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    OutlinedButton(
                                        onClick = { showStartTimePicker = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Default.AccessTime,
                                            contentDescription = "Välj starttid",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (startTime.isNotEmpty()) startTime else "08:00",
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                                
                                // Klickbar sluttid
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Sluttid",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    OutlinedButton(
                                        onClick = { showEndTimePicker = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Default.AccessTime,
                                            contentDescription = "Välj sluttid",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (endTime.isNotEmpty()) endTime else "17:00",
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Rast-sektion
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🍽️ Raster",
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Switch(
                                    checked = useAutomaticBreaks,
                                    onCheckedChange = { useAutomaticBreaks = it }
                                )
                            }
                            
                            Text(
                                text = if (useAutomaticBreaks) "Automatiska raster aktiverade" else "Manuell rastinmatning",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            
                            if (!useAutomaticBreaks) {
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Klickbar rast start
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Rast start",
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        OutlinedButton(
                                            onClick = { showBreakStartTimePicker = true },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                Icons.Default.AccessTime,
                                                contentDescription = "Välj rast start",
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (breakStartTime.isNotEmpty()) breakStartTime else "12:00",
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                    }
                                    
                                    // Klickbar rast slut
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Rast slut",
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        OutlinedButton(
                                            onClick = { showBreakEndTimePicker = true },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                Icons.Default.AccessTime,
                                                contentDescription = "Välj rast slut",
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (breakEndTime.isNotEmpty()) breakEndTime else "13:00",
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                    }
                                }
                                
                                // Avskiljare mellan rast start/slut och rastminuter
                                Divider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                                )
                                
                                Text(
                                    text = "Eller ange rastminuter direkt:",
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                OutlinedTextField(
                                    value = breakMinutes,
                                    onValueChange = { breakMinutes = it },
                                    label = { Text("Rastminuter") },
                                    placeholder = { Text("30") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Schedule,
                                            contentDescription = "Rastminuter"
                                        )
                                    }
                                )
                            }
                        }
                    }
                    
                    // Beskrivning-sektion
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "📝 Beskrivning (valfritt)",
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold
                            )
                            
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Beskrivning av arbetsdagen") },
                                placeholder = { Text("T.ex. Lagerarbete, Kundtjänst...") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
                                singleLine = false
                            )
                        }
                    }
                    
                    // Error message
                    if (hasErrors && errorMessage.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = errorMessage,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.error
                            )
                        }
                    }
                }
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
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
                            try {
                                // Validate input
                                if (startTime.isBlank() || endTime.isBlank()) {
                                    errorMessage = "Start- och sluttid måste anges"
                                    hasErrors = true
                                    return@Button
                                }
                                
                                // Parse times
                                val parsedStartTime = try {
                                    LocalTime.parse(startTime)
                                } catch (e: Exception) {
                                    throw IllegalArgumentException("Ogiltig starttid: $startTime")
                                }
                                
                                val parsedEndTime = try {
                                    LocalTime.parse(endTime)
                                } catch (e: Exception) {
                                    throw IllegalArgumentException("Ogiltig sluttid: $endTime")
                                }
                                
                                val parsedBreakStart = if (useAutomaticBreaks || breakStartTime.isBlank()) {
                                    null
                                } else {
                                    try {
                                        LocalTime.parse(breakStartTime)
                                    } catch (e: Exception) {
                                        throw IllegalArgumentException("Ogiltig rast-starttid: $breakStartTime")
                                    }
                                }
                                
                                val parsedBreakEnd = if (useAutomaticBreaks || breakEndTime.isBlank()) {
                                    null
                                } else {
                                    try {
                                        LocalTime.parse(breakEndTime)
                                    } catch (e: Exception) {
                                        throw IllegalArgumentException("Ogiltig rast-sluttid: $breakEndTime")
                                    }
                                }
                                
                                // Create new TimeEntry
                                val newEntry = TimeEntry(
                                    date = date,
                                    startTime = parsedStartTime,
                                    endTime = parsedEndTime,
                                    breakStart = parsedBreakStart,
                                    breakEnd = parsedBreakEnd,
                                    breakMinutes = if (useAutomaticBreaks) 0 else (breakMinutes.toIntOrNull() ?: 0)
                                )
                                
                                onConfirm(newEntry)
                                hasErrors = false
                                errorMessage = ""
                            } catch (e: Exception) {
                                errorMessage = "Fel vid skapande av dag: ${e.message}"
                                hasErrors = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lägg till")
                    }
                }
            }
        }
    }
    
    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            selectedDate = date,
            onDateSelected = { selectedDate ->
                date = selectedDate
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
    
    // TimePicker Dialogs
    if (showStartTimePicker) {
        TimePickerDialog(
            selectedTime = try { LocalTime.parse(startTime) } catch (e: Exception) { LocalTime.of(8, 0) },
            onTimeSelected = { selectedTime ->
                startTime = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false },
            title = "Välj starttid"
        )
    }
    
    if (showEndTimePicker) {
        TimePickerDialog(
            selectedTime = try { LocalTime.parse(endTime) } catch (e: Exception) { LocalTime.of(17, 0) },
            onTimeSelected = { selectedTime ->
                endTime = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false },
            title = "Välj sluttid"
        )
    }
    
    if (showBreakStartTimePicker) {
        TimePickerDialog(
            selectedTime = try { LocalTime.parse(breakStartTime) } catch (e: Exception) { LocalTime.of(12, 0) },
            onTimeSelected = { selectedTime ->
                breakStartTime = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                showBreakStartTimePicker = false
            },
            onDismiss = { showBreakStartTimePicker = false },
            title = "Välj rast start"
        )
    }
    
    if (showBreakEndTimePicker) {
        TimePickerDialog(
            selectedTime = try { LocalTime.parse(breakEndTime) } catch (e: Exception) { LocalTime.of(13, 0) },
            onTimeSelected = { selectedTime ->
                breakEndTime = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                showBreakEndTimePicker = false
            },
            onDismiss = { showBreakEndTimePicker = false },
            title = "Välj rast slut"
        )
    }
}