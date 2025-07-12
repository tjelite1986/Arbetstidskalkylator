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
    onTemplateSelected: (WorkShiftTemplate) -> Unit
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
    onDelete: () -> Unit
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
    var startHour by remember { mutableStateOf("08") }
    var startMinute by remember { mutableStateOf("00") }
    var endHour by remember { mutableStateOf("17") }
    var endMinute by remember { mutableStateOf("00") }
    var breakMinutes by remember { mutableStateOf("60") }
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ny mall",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        
        // Compact time inputs
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Start:", style = MaterialTheme.typography.body2)
            
            OutlinedTextField(
                value = startHour,
                onValueChange = { if (it.length <= 2) startHour = it },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Text(":")
            OutlinedTextField(
                value = startMinute,
                onValueChange = { if (it.length <= 2) startMinute = it },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Slut:  ", style = MaterialTheme.typography.body2)
            
            OutlinedTextField(
                value = endHour,
                onValueChange = { if (it.length <= 2) endHour = it },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Text(":")
            OutlinedTextField(
                value = endMinute,
                onValueChange = { if (it.length <= 2) endMinute = it },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
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
        val preview = remember(startHour, startMinute, endHour, endMinute, breakMinutes) {
            try {
                WorkShiftTemplate(
                    startTime = LocalTime.of(
                        startHour.toIntOrNull() ?: 8,
                        startMinute.toIntOrNull() ?: 0
                    ),
                    endTime = LocalTime.of(
                        endHour.toIntOrNull() ?: 17,
                        endMinute.toIntOrNull() ?: 0
                    ),
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
                            startTime = LocalTime.of(
                                startHour.toIntOrNull() ?: 8,
                                startMinute.toIntOrNull() ?: 0
                            ),
                            endTime = LocalTime.of(
                                endHour.toIntOrNull() ?: 17,
                                endMinute.toIntOrNull() ?: 0
                            ),
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
}