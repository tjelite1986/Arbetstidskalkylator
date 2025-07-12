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
                                OutlinedTextField(
                                    value = startTime,
                                    onValueChange = { startTime = it },
                                    label = { Text("Starttid") },
                                    placeholder = { Text("08:00") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                
                                OutlinedTextField(
                                    value = endTime,
                                    onValueChange = { endTime = it },
                                    label = { Text("Sluttid") },
                                    placeholder = { Text("17:00") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
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
                                    OutlinedTextField(
                                        value = breakStartTime,
                                        onValueChange = { breakStartTime = it },
                                        label = { Text("Rast start") },
                                        placeholder = { Text("12:00") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    
                                    OutlinedTextField(
                                        value = breakEndTime,
                                        onValueChange = { breakEndTime = it },
                                        label = { Text("Rast slut") },
                                        placeholder = { Text("13:00") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }
                                
                                Text(
                                    text = "Eller ange rastminuter direkt:",
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = breakMinutes,
                                    onValueChange = { breakMinutes = it },
                                    label = { Text("Rastminuter") },
                                    placeholder = { Text("30") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
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
}