package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.data.Settings
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.time.DayOfWeek
import java.util.*

enum class CalendarViewType {
    DAY, WEEK, MONTH
}

enum class MonthViewType {
    LIST, TIMELINE  // List = current list view, Timeline = week-view style
}

@Composable
fun CalendarView(
    timeEntries: List<TimeEntry>,
    settings: Settings = Settings(),
    modifier: Modifier = Modifier
) {
    var currentViewType by remember { 
        mutableStateOf(
            if (settings.calendarSettings.monthViewAsDefault) 
                CalendarViewType.MONTH 
            else 
                CalendarViewType.WEEK
        ) 
    }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var monthViewType by remember { mutableStateOf(MonthViewType.LIST) }
    
    Column(modifier = modifier.fillMaxSize()) {
        // View Type Selector
        CalendarViewTypeSelector(
            currentViewType = currentViewType,
            onViewTypeChanged = { currentViewType = it },
            modifier = Modifier.padding(16.dp)
        )
        
        // Month View Type Selector (only visible when MONTH is selected)
        if (currentViewType == CalendarViewType.MONTH) {
            MonthViewTypeSelector(
                currentMonthViewType = monthViewType,
                onMonthViewTypeChanged = { monthViewType = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        // Date Navigation
        DateNavigationHeader(
            currentDate = currentDate,
            viewType = currentViewType,
            settings = settings,
            onDateChanged = { currentDate = it },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Calendar Content
        when (currentViewType) {
            CalendarViewType.DAY -> DayView(currentDate, timeEntries, settings)
            CalendarViewType.WEEK -> WeekView(currentDate, timeEntries, settings)
            CalendarViewType.MONTH -> {
                when (monthViewType) {
                    MonthViewType.LIST -> MonthView(currentDate, timeEntries, settings)
                    MonthViewType.TIMELINE -> TimelineMonthView(currentDate, timeEntries, settings)
                }
            }
        }
    }
}

@Composable
private fun MonthViewTypeSelector(
    currentMonthViewType: MonthViewType,
    onMonthViewTypeChanged: (MonthViewType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MonthViewTypeButton(
            viewType = MonthViewType.LIST,
            currentViewType = currentMonthViewType,
            onViewTypeChanged = onMonthViewTypeChanged,
            icon = Icons.Default.ViewList,
            text = "Lista"
        )
        
        MonthViewTypeButton(
            viewType = MonthViewType.TIMELINE,
            currentViewType = currentMonthViewType,
            onViewTypeChanged = onMonthViewTypeChanged,
            icon = Icons.Default.Timeline,
            text = "Tidslinje"
        )
    }
}

@Composable
private fun MonthViewTypeButton(
    viewType: MonthViewType,
    currentViewType: MonthViewType,
    onViewTypeChanged: (MonthViewType) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    val isSelected = viewType == currentViewType
    
    Button(
        onClick = { onViewTypeChanged(viewType) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
            contentColor = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        modifier = Modifier
            .height(40.dp)
            .width(120.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun CalendarViewTypeSelector(
    currentViewType: CalendarViewType,
    onViewTypeChanged: (CalendarViewType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CalendarViewTypeButton(
            viewType = CalendarViewType.DAY,
            currentViewType = currentViewType,
            onViewTypeChanged = onViewTypeChanged,
            icon = Icons.Default.Today,
            text = "Dag"
        )
        
        CalendarViewTypeButton(
            viewType = CalendarViewType.WEEK,
            currentViewType = currentViewType,
            onViewTypeChanged = onViewTypeChanged,
            icon = Icons.Default.DateRange,
            text = "Vecka"
        )
        
        CalendarViewTypeButton(
            viewType = CalendarViewType.MONTH,
            currentViewType = currentViewType,
            onViewTypeChanged = onViewTypeChanged,
            icon = Icons.Default.CalendarMonth,
            text = "Månad"
        )
    }
}

@Composable
private fun CalendarViewTypeButton(
    viewType: CalendarViewType,
    currentViewType: CalendarViewType,
    onViewTypeChanged: (CalendarViewType) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    val isSelected = viewType == currentViewType
    
    Button(
        onClick = { onViewTypeChanged(viewType) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        modifier = Modifier
            .height(48.dp)
            .width(100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun DateNavigationHeader(
    currentDate: LocalDate,
    viewType: CalendarViewType,
    settings: Settings,
    onDateChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekFields = if (settings.calendarSettings.weekStartsOnMonday) {
        WeekFields.of(DayOfWeek.MONDAY, 1)
    } else {
        WeekFields.of(DayOfWeek.SUNDAY, 1)
    }
    
    val weekNumber = currentDate.get(weekFields.weekOfWeekBasedYear())
    
    val dateFormatter = when (viewType) {
        CalendarViewType.DAY -> DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("sv"))
        CalendarViewType.WEEK -> if (settings.calendarSettings.showWeekNumbers) {
            DateTimeFormatter.ofPattern("'Vecka' $weekNumber, MMMM yyyy", Locale("sv"))
        } else {
            DateTimeFormatter.ofPattern("'Vecka' w, MMMM yyyy", Locale("sv"))
        }
        CalendarViewType.MONTH -> DateTimeFormatter.ofPattern("MMMM yyyy", Locale("sv"))
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onDateChanged(when (viewType) {
                    CalendarViewType.DAY -> currentDate.minusDays(1)
                    CalendarViewType.WEEK -> currentDate.minusWeeks(1)
                    CalendarViewType.MONTH -> currentDate.minusMonths(1)
                })
            }
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Föregående")
        }
        
        Text(
            text = currentDate.format(dateFormatter),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        
        IconButton(
            onClick = {
                onDateChanged(when (viewType) {
                    CalendarViewType.DAY -> currentDate.plusDays(1)
                    CalendarViewType.WEEK -> currentDate.plusWeeks(1)
                    CalendarViewType.MONTH -> currentDate.plusMonths(1)
                })
            }
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Nästa")
        }
    }
}

@Composable
private fun DayView(currentDate: LocalDate, timeEntries: List<TimeEntry>, settings: Settings) {
    val dayEntries = timeEntries.filter { it.date == currentDate }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (dayEntries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "Inga arbetstider registrerade för denna dag",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            items(dayEntries) { entry ->
                TimeEntryCard(entry)
            }
        }
    }
}

@Composable
private fun WeekView(currentDate: LocalDate, timeEntries: List<TimeEntry>, settings: Settings) {
    val weekFields = if (settings.calendarSettings.weekStartsOnMonday) {
        WeekFields.of(DayOfWeek.MONDAY, 1)
    } else {
        WeekFields.of(DayOfWeek.SUNDAY, 1)
    }
    val startOfWeek = currentDate.with(weekFields.dayOfWeek(), 1)
    val weekDays = (0..6).map { startOfWeek.plusDays(it.toLong()) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weekDays) { day ->
            val dayEntries = timeEntries.filter { it.date == day }
            WeekDayCard(day, dayEntries, settings)
        }
    }
}

@Composable
private fun MonthView(currentDate: LocalDate, timeEntries: List<TimeEntry>, settings: Settings) {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
    val monthEntries = timeEntries.filter { 
        !it.date.isBefore(firstDayOfMonth) && !it.date.isAfter(lastDayOfMonth) 
    }
    
    val entriesByDate = monthEntries.groupBy { it.date }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(entriesByDate.toList()) { (date, entries) ->
            MonthDayCard(date, entries)
        }
        
        if (entriesByDate.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "Inga arbetstider registrerade för denna månad",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeEntryCard(entry: TimeEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${entry.startTime?.toString() ?: "??:??"} - ${entry.endTime?.toString() ?: "??:??"}",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.1f", entry.workHours)}h",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary
                )
            }
            
            // Note field is not available in TimeEntry, can be added later if needed
            
            if (entry.totalPay > 0) {
                Text(
                    text = "Bruttolön: ${String.format("%.0f", entry.totalPay)} kr",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
private fun WeekDayCard(date: LocalDate, entries: List<TimeEntry>, settings: Settings) {
    val weekFields = if (settings.calendarSettings.weekStartsOnMonday) {
        WeekFields.of(DayOfWeek.MONDAY, 1)
    } else {
        WeekFields.of(DayOfWeek.SUNDAY, 1)
    }
    
    val dayFormatter = if (settings.calendarSettings.showWeekNumbers && date.dayOfWeek == DayOfWeek.MONDAY) {
        val weekNumber = date.get(weekFields.weekOfWeekBasedYear())
        DateTimeFormatter.ofPattern("EEEE d/M (v$weekNumber)", Locale("sv"))
    } else {
        DateTimeFormatter.ofPattern("EEEE d/M", Locale("sv"))
    }
    
    val totalHours = entries.sumOf { it.workHours }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = if (entries.isNotEmpty()) MaterialTheme.colors.primary.copy(alpha = 0.05f) else MaterialTheme.colors.surface
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
                    text = date.format(dayFormatter),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                
                if (totalHours > 0) {
                    Text(
                        text = "${String.format("%.1f", totalHours)}h",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            if (entries.isNotEmpty()) {
                entries.forEach { entry ->
                    Text(
                        text = "${entry.startTime?.toString() ?: "??:??"}-${entry.endTime?.toString() ?: "??:??"}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "Ingen arbetstid",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun MonthDayCard(date: LocalDate, entries: List<TimeEntry>) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale("sv"))
    val totalHours = entries.sumOf { it.workHours }
    val totalPay = entries.sumOf { it.totalPay }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.08f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date.format(dayFormatter),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${String.format("%.1f", totalHours)}h",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (totalPay > 0) {
                        Text(
                            text = "${String.format("%.0f", totalPay)} kr",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
            
            entries.forEach { entry ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${entry.startTime?.toString() ?: "??:??"} - ${entry.endTime?.toString() ?: "??:??"}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                    )
                    // Note field not available in current TimeEntry structure
                }
            }
        }
    }
}

@Composable
private fun TimelineMonthView(currentDate: LocalDate, timeEntries: List<TimeEntry>, settings: Settings) {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
    val monthEntries = timeEntries.filter { 
        !it.date.isBefore(firstDayOfMonth) && !it.date.isAfter(lastDayOfMonth) 
    }
    
    if (monthEntries.isEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "Inga arbetstider registrerade för denna månad",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        return
    }
    
    // Create timeline grid
    TimelineGrid(
        monthEntries = monthEntries,
        currentDate = currentDate
    )
}

@Composable
private fun TimelineGrid(
    monthEntries: List<TimeEntry>,
    currentDate: LocalDate
) {
    // Group entries by date for easier processing
    val entriesByDate = monthEntries.groupBy { it.date }
    
    // Create time slots (hours) from 06:00 to 22:00 (work hours)
    val timeSlots = (6..22).toList()
    
    // Get unique dates in the month with entries
    val datesWithEntries = entriesByDate.keys.sorted()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Header with dates
        item {
            TimelineHeader(datesWithEntries)
        }
        
        // Timeline rows for each hour
        items(timeSlots) { hour ->
            TimelineHourRow(
                hour = hour,
                datesWithEntries = datesWithEntries,
                entriesByDate = entriesByDate
            )
        }
    }
}

@Composable
private fun TimelineHeader(datesWithEntries: List<LocalDate>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Time column header
        Box(
            modifier = Modifier.width(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tid",
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Date headers
        datesWithEntries.forEach { date ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("EEE", Locale("sv"))),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineHourRow(
    hour: Int,
    datesWithEntries: List<LocalDate>,
    entriesByDate: Map<LocalDate, List<TimeEntry>>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time label
        Box(
            modifier = Modifier.width(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%02d:00", hour),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        
        // Timeline blocks for each date
        datesWithEntries.forEach { date ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 1.dp)
            ) {
                // Check if this hour has any work time for this date
                val dayEntries = entriesByDate[date] ?: emptyList()
                val hasWorkInThisHour = dayEntries.any { entry ->
                    val startHour = entry.startTime?.hour ?: 0
                    val endHour = entry.endTime?.hour ?: 0
                    hour >= startHour && hour < endHour
                }
                
                if (hasWorkInThisHour) {
                    // Find the entry that covers this hour
                    val entryForHour = dayEntries.firstOrNull { entry ->
                        val startHour = entry.startTime?.hour ?: 0
                        val endHour = entry.endTime?.hour ?: 0
                        hour >= startHour && hour < endHour
                    }
                    
                    if (entryForHour != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(2.dp)),
                            backgroundColor = getWorkTimeColor(entryForHour),
                            elevation = 1.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (hour == (entryForHour.startTime?.hour ?: 0)) {
                                    // Show start time only on first hour
                                    Text(
                                        text = "${entryForHour.startTime?.toString()?.substring(0, 5) ?: ""}",
                                        style = MaterialTheme.typography.caption,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Empty time slot
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colors.surface.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun getWorkTimeColor(entry: TimeEntry): Color {
    // Different colors for different times of day or work types
    val startHour = entry.startTime?.hour ?: 0
    return when {
        startHour < 8 -> Color(0xFFE91E63) // Early morning - Pink
        startHour < 12 -> Color(0xFF2196F3) // Morning - Blue  
        startHour < 17 -> Color(0xFFFF9800) // Afternoon - Orange
        startHour < 20 -> Color(0xFF4CAF50) // Evening - Green
        else -> Color(0xFF9C27B0) // Night - Purple
    }
}