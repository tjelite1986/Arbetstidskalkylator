package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.timereportcalculator.data.WorkShiftTemplate
import com.example.timereportcalculator.data.WorkShiftTemplateManager
import java.time.LocalTime

@Composable
fun WorkShiftTemplateDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onTemplateSelected: (WorkShiftTemplate) -> Unit,
    onFavoritesChanged: (() -> Unit)? = null
) {
    if (isOpen) {
        val context = LocalContext.current
        val templateManager = remember { WorkShiftTemplateManager(context) }
        var templates by remember { mutableStateOf(templateManager.getAllTemplates()) }
        var showCreateForm by remember { mutableStateOf(false) }
        
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Arbetspass-mallar",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Stäng")
                        }
                    }
                    
                    if (!showCreateForm) {
                        // Compact template list
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 250.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(templates) { template ->
                                CompactTemplateItem(
                                    template = template,
                                    onSelect = { 
                                        onTemplateSelected(template)
                                        onDismiss()
                                    },
                                    onDelete = {
                                        templateManager.deleteTemplate(template.id)
                                        templates = templateManager.getAllTemplates()
                                    },
                                    onToggleFavorite = {
                                        templateManager.toggleFavorite(template.id)
                                        templates = templateManager.getAllTemplates()
                                        onFavoritesChanged?.invoke()
                                    }
                                )
                            }
                        }
                        
                        // Add new template button
                        OutlinedButton(
                            onClick = { showCreateForm = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ny mall")
                        }
                    } else {
                        // Compact create form
                        CompactCreateForm(
                            onSave = { template ->
                                templateManager.saveTemplate(template)
                                templates = templateManager.getAllTemplates()
                                showCreateForm = false
                            },
                            onCancel = { showCreateForm = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactTemplateItem(
    template: WorkShiftTemplate,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main content - clickable to select
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        Modifier.clickable { onSelect() }
                    )
            ) {
                Text(
                    text = template.getDisplayText(),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Favorite button
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    if (template.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (template.isFavorite) "Ta bort från favoriter" else "Lägg till som favorit",
                    tint = if (template.isFavorite) Color(0xFFFFD700) else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
            
            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Ta bort",
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactCreateForm(
    onSave: (WorkShiftTemplate) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(LocalTime.of(8, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(17, 0)) }
    var breakMinutes by remember { mutableStateOf("60") }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ny mall",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        
        // Template name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Mallnamn") },
            placeholder = { Text("T.ex. Kontorstid") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Time picker buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Start time
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
                        text = startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            
            // End time
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
                        text = endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rast:", style = MaterialTheme.typography.body2)
            OutlinedTextField(
                value = breakMinutes,
                onValueChange = { breakMinutes = it },
                modifier = Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Text("min", style = MaterialTheme.typography.body2)
        }
        
        // Preview
        val preview = remember(name, startTime, endTime, breakMinutes) {
            try {
                WorkShiftTemplate(
                    name = name,
                    startTime = startTime,
                    endTime = endTime,
                    breakMinutes = breakMinutes.toIntOrNull() ?: 0
                )
            } catch (e: Exception) {
                null
            }
        }
        
        preview?.let {
            Card(
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Förhandsvisning: ${it.getDisplayText()}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary
                )
            }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Avbryt")
            }
            
            Button(
                onClick = {
                    try {
                        val template = WorkShiftTemplate(
                            name = name,
                            startTime = startTime,
                            endTime = endTime,
                            breakMinutes = breakMinutes.toIntOrNull() ?: 0
                        )
                        onSave(template)
                    } catch (e: Exception) {
                        // Handle invalid time input
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Spara")
            }
        }
    }
    
    // Time pickers
    if (showStartTimePicker) {
        TimePickerDialog(
            selectedTime = startTime,
            onTimeSelected = { selectedTime ->
                startTime = selectedTime
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false },
            title = "Välj starttid"
        )
    }
    
    if (showEndTimePicker) {
        TimePickerDialog(
            selectedTime = endTime,
            onTimeSelected = { selectedTime ->
                endTime = selectedTime
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false },
            title = "Välj sluttid"
        )
    }
}