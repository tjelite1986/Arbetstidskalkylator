package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.timereportcalculator.data.TimeEntry
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TimeEntryDetailsDialog(
    timeEntry: TimeEntry,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(24.dp)
            ) {
                // Header with date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = timeEntry.date.format(
                                DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale("sv"))
                            ),
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Stäng",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Work times section
                if (timeEntry.startTime != null && timeEntry.endTime != null) {
                    DetailRow(
                        icon = Icons.Default.AccessTime,
                        label = "Starttid",
                        value = String.format("%02d:%02d", timeEntry.startTime!!.hour, timeEntry.startTime!!.minute),
                        color = Color(0xFF4CAF50)
                    )
                    
                    DetailRow(
                        icon = Icons.Default.Schedule,
                        label = "Sluttid", 
                        value = String.format("%02d:%02d", timeEntry.endTime!!.hour, timeEntry.endTime!!.minute),
                        color = Color(0xFFF44336)
                    )
                } else {
                    DetailRow(
                        icon = Icons.Default.WorkOff,
                        label = "Arbetstid",
                        value = "Ej registrerad",
                        color = Color(0xFF9E9E9E)
                    )
                }
                
                // Break duration
                if (timeEntry.breakMinutes > 0) {
                    DetailRow(
                        icon = Icons.Default.Coffee,
                        label = "Rast",
                        value = "${timeEntry.breakMinutes} min",
                        color = Color(0xFF795548)
                    )
                } else {
                    DetailRow(
                        icon = Icons.Default.Coffee,
                        label = "Rast",
                        value = "Ingen rast",
                        color = Color(0xFF9E9E9E)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))
                
                // Calculated values
                DetailRow(
                    icon = Icons.Default.Timer,
                    label = "Jobbade timmar",
                    value = String.format("%.2f h", timeEntry.workHours),
                    color = Color(0xFF2196F3),
                    isHighlighted = true
                )
                
                DetailRow(
                    icon = Icons.Default.AttachMoney,
                    label = "Intjänade pengar",
                    value = String.format("%.0f kr", timeEntry.totalPay),
                    color = Color(0xFF4CAF50),
                    isHighlighted = true
                )
                
                // Additional info if present
                if (timeEntry.isRedDay) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Helgdag",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                if (timeEntry.isSickDay) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sjukdag",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFFFF9800),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(
                if (isHighlighted) {
                    Modifier.background(
                        color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ).padding(8.dp)
                } else {
                    Modifier.padding(4.dp)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) color else MaterialTheme.colors.onSurface,
            fontSize = if (isHighlighted) 16.sp else 14.sp
        )
    }
}