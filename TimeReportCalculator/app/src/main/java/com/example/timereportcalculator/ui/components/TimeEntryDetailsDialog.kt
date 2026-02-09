package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    onDismiss: () -> Unit,
    onEdit: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f) // Limit height to 90% of screen
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize()
            ) {
                // Fixed header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
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
                
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
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
                    
                    // Detailed pay breakdown
                    if (timeEntry.totalPay > 0) {
                        PayBreakdownSection(timeEntry = timeEntry)
                    }
                    
                    DetailRow(
                        icon = Icons.Default.AttachMoney,
                        label = "Total lön",
                        value = String.format("%.0f kr", timeEntry.totalPay),
                        color = Color(0xFF4CAF50),
                        isHighlighted = true
                    )
                    
                    // Additional info badges
                    EntryStatusBadges(timeEntry = timeEntry)
                    
                    // Work period summary
                    if (timeEntry.startTime != null && timeEntry.endTime != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        WorkPeriodSummary(timeEntry = timeEntry)
                    }
                    
                    // Add some bottom padding for better scrolling
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Divider line
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                
                // Fixed action buttons at bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Stäng")
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Button(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Redigera")
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

@Composable
private fun PayBreakdownSection(timeEntry: TimeEntry) {
    if (timeEntry.totalPay > 0) {
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.05f),
            shape = RoundedCornerShape(8.dp),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Löneuppdelning",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Base pay
                val basePay = timeEntry.workHours * 150 // Approximate base calculation
                DetailRow(
                    icon = Icons.Default.WorkOutline,
                    label = "Grund (${String.format("%.1f", timeEntry.workHours)}h)",
                    value = String.format("%.0f kr", basePay),
                    color = Color(0xFF757575)
                )
                
                // OB additions if applicable
                if (timeEntry.totalPay > basePay) {
                    val obPay = timeEntry.totalPay - basePay
                    DetailRow(
                        icon = Icons.Default.NightlightRound,
                        label = "OB-tillägg",
                        value = String.format("%.0f kr", obPay),
                        color = Color(0xFF9C27B0)
                    )
                }
                
                // Vacation pay - use the value calculated by PayCalculator
                if (timeEntry.vacationPay > 0) {
                    DetailRow(
                        icon = Icons.Default.BeachAccess,
                        label = "Semesterersättning",
                        value = String.format("%.0f kr", timeEntry.vacationPay),
                        color = Color(0xFF00BCD4)
                    )
                }
            }
        }
    }
}

@Composable
private fun EntryStatusBadges(timeEntry: TimeEntry) {
    val badges = mutableListOf<BadgeInfo>()
    
    if (timeEntry.isRedDay) {
        badges.add(
            BadgeInfo(
                text = if (timeEntry.isHalfDayHoliday) "Aftonshelgdag" else "Helgdag",
                icon = Icons.Default.Celebration,
                backgroundColor = Color(0xFFFFEBEE),
                textColor = Color(0xFFE91E63)
            )
        )
    }
    
    if (timeEntry.isSickDay) {
        badges.add(
            BadgeInfo(
                text = "Sjukdag",
                icon = Icons.Default.LocalHospital,
                backgroundColor = Color(0xFFFFF3E0),
                textColor = Color(0xFFFF9800)
            )
        )
    }
    
    // Weekend badge
    if (timeEntry.date.dayOfWeek.value >= 6) {
        badges.add(
            BadgeInfo(
                text = if (timeEntry.date.dayOfWeek.value == 7) "Söndag" else "Lördag",
                icon = Icons.Default.Weekend,
                backgroundColor = Color(0xFFF3E5F5),
                textColor = Color(0xFF9C27B0)
            )
        )
    }
    
    // OB time badges
    if (timeEntry.startTime != null && timeEntry.endTime != null) {
        if (timeEntry.startTime!!.hour < 7 || timeEntry.endTime!!.hour > 18) {
            badges.add(
                BadgeInfo(
                    text = "OB-tid",
                    icon = Icons.Default.NightlightRound,
                    backgroundColor = Color(0xFFE8F5E8),
                    textColor = Color(0xFF4CAF50)
                )
            )
        }
    }
    
    if (badges.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            badges.forEach { badge ->
                StatusBadge(badge = badge)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun StatusBadge(badge: BadgeInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                badge.backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Icon(
            imageVector = badge.icon,
            contentDescription = null,
            tint = badge.textColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = badge.text,
            style = MaterialTheme.typography.body2,
            color = badge.textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun WorkPeriodSummary(timeEntry: TimeEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Arbetspass-sammanfattning",
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Working period - with null safety check
            if (timeEntry.startTime != null && timeEntry.endTime != null) {
                val workDuration = java.time.Duration.between(timeEntry.startTime, timeEntry.endTime)
                val totalMinutes = workDuration.toMinutes()
                val workingMinutes = totalMinutes - timeEntry.breakMinutes
            
            DetailRow(
                icon = Icons.Default.Schedule,
                label = "Total tid på plats",
                value = String.format("%d tim %d min", totalMinutes / 60, totalMinutes % 60),
                color = Color(0xFF9E9E9E)
            )
            
            DetailRow(
                icon = Icons.Default.Work,
                label = "Effektiv arbetstid",
                value = String.format("%d tim %d min", workingMinutes / 60, workingMinutes % 60),
                color = Color(0xFF2196F3)
            )
            
                // Hourly rate estimate - with safety checks
                if (timeEntry.workHours > 0.0 && timeEntry.totalPay >= 0.0) {
                    val hourlyRate = timeEntry.totalPay / timeEntry.workHours
                    DetailRow(
                        icon = Icons.Default.Paid,
                        label = "Genomsnittlig timlön",
                        value = String.format("%.0f kr/h", hourlyRate),
                        color = Color(0xFF4CAF50)
                    )
                }
            } else {
                // Show message when time data is not available
                Text(
                    text = "Ingen tidsdata tillgänglig för detaljerad sammanfattning",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

private data class BadgeInfo(
    val text: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val textColor: Color
)