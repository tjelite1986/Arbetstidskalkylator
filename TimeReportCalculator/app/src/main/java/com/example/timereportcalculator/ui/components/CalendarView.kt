package com.example.timereportcalculator.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Enhanced View Type Selector with Material 2
        ModernCalendarViewTypeSelector(
            currentViewType = currentViewType,
            onViewTypeChanged = { currentViewType = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        
        // Enhanced Month View Type Selector
        if (currentViewType == CalendarViewType.MONTH) {
            ModernMonthViewTypeSelector(
                currentMonthViewType = monthViewType,
                onMonthViewTypeChanged = { monthViewType = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        // Enhanced Date Navigation
        ModernDateNavigationHeader(
            currentDate = currentDate,
            viewType = currentViewType,
            settings = settings,
            onDateChanged = { currentDate = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Calendar Content with enhanced styling
        when (currentViewType) {
            CalendarViewType.DAY -> ModernDayView(currentDate, timeEntries)
            CalendarViewType.WEEK -> ModernWeekView(currentDate, timeEntries, settings)
            CalendarViewType.MONTH -> {
                when (monthViewType) {
                    MonthViewType.LIST -> ModernMonthView(currentDate, timeEntries)
                    MonthViewType.TIMELINE -> ModernTimelineMonthView(currentDate, timeEntries)
                }
            }
        }
    }
}

@Composable
private fun ModernMonthViewTypeSelector(
    currentMonthViewType: MonthViewType,
    onMonthViewTypeChanged: (MonthViewType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ModernMonthViewTypeButton(
                viewType = MonthViewType.LIST,
                currentViewType = currentMonthViewType,
                onViewTypeChanged = onMonthViewTypeChanged,
                icon = Icons.AutoMirrored.Outlined.List,
                selectedIcon = Icons.AutoMirrored.Filled.List,
                text = "Lista",
                modifier = Modifier.weight(1f)
            )
            
            ModernMonthViewTypeButton(
                viewType = MonthViewType.TIMELINE,
                currentViewType = currentMonthViewType,
                onViewTypeChanged = onMonthViewTypeChanged,
                icon = Icons.Default.Timeline,
                selectedIcon = Icons.Filled.Timeline,
                text = "Tidslinje",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ModernMonthViewTypeButton(
    viewType: MonthViewType,
    currentViewType: MonthViewType,
    onViewTypeChanged: (MonthViewType) -> Unit,
    icon: ImageVector,
    selectedIcon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    val isSelected = viewType == currentViewType
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "button_scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colors.primary 
        else 
            MaterialTheme.colors.surface,
        animationSpec = tween(250),
        label = "background_color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colors.onPrimary 
        else 
            MaterialTheme.colors.onSurface,
        animationSpec = tween(250),
        label = "content_color"
    )
    
    Button(
        onClick = { onViewTypeChanged(viewType) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        modifier = modifier
            .height(44.dp)
            .scale(scale)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else icon,
                contentDescription = text,
                modifier = Modifier.size(18.dp)
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
private fun ModernCalendarViewTypeSelector(
    currentViewType: CalendarViewType,
    onViewTypeChanged: (CalendarViewType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.15f),
        elevation = 6.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ModernCalendarViewTypeButton(
                viewType = CalendarViewType.DAY,
                currentViewType = currentViewType,
                onViewTypeChanged = onViewTypeChanged,
                icon = Icons.Outlined.Today,
                selectedIcon = Icons.Filled.Today,
                text = "Dag",
                modifier = Modifier.weight(1f)
            )
            
            ModernCalendarViewTypeButton(
                viewType = CalendarViewType.WEEK,
                currentViewType = currentViewType,
                onViewTypeChanged = onViewTypeChanged,
                icon = Icons.Outlined.DateRange,
                selectedIcon = Icons.Filled.DateRange,
                text = "Vecka",
                modifier = Modifier.weight(1f)
            )
            
            ModernCalendarViewTypeButton(
                viewType = CalendarViewType.MONTH,
                currentViewType = currentViewType,
                onViewTypeChanged = onViewTypeChanged,
                icon = Icons.Outlined.CalendarMonth,
                selectedIcon = Icons.Filled.CalendarMonth,
                text = "Månad",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ModernCalendarViewTypeButton(
    viewType: CalendarViewType,
    currentViewType: CalendarViewType,
    onViewTypeChanged: (CalendarViewType) -> Unit,
    icon: ImageVector,
    selectedIcon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    val isSelected = viewType == currentViewType
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "button_scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colors.primary 
        else 
            MaterialTheme.colors.surface,
        animationSpec = tween(300),
        label = "background_color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) 
            MaterialTheme.colors.onPrimary 
        else 
            MaterialTheme.colors.onSurface,
        animationSpec = tween(300),
        label = "content_color"
    )
    
    Button(
        onClick = { onViewTypeChanged(viewType) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = if (isSelected) 8.dp else 3.dp
        ),
        modifier = modifier
            .height(52.dp)
            .scale(scale)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else icon,
                contentDescription = text,
                modifier = Modifier.size(22.dp)
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
private fun ModernDateNavigationHeader(
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
    
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.2f),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    onDateChanged(when (viewType) {
                        CalendarViewType.DAY -> currentDate.minusDays(1)
                        CalendarViewType.WEEK -> currentDate.minusWeeks(1)
                        CalendarViewType.MONTH -> currentDate.minusMonths(1)
                    })
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Default.ChevronLeft, 
                    contentDescription = "Föregående",
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentDate.format(dateFormatter),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
                if (viewType == CalendarViewType.DAY) {
                    val dayOfWeek = currentDate.format(DateTimeFormatter.ofPattern("EEEE", Locale("sv")))
                    Text(
                        text = dayOfWeek.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            OutlinedButton(
                onClick = {
                    onDateChanged(when (viewType) {
                        CalendarViewType.DAY -> currentDate.plusDays(1)
                        CalendarViewType.WEEK -> currentDate.plusWeeks(1)
                        CalendarViewType.MONTH -> currentDate.plusMonths(1)
                    })
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Default.ChevronRight, 
                    contentDescription = "Nästa",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ModernDayView(currentDate: LocalDate, timeEntries: List<TimeEntry>) {
    val dayEntries = timeEntries.filter { it.date == currentDate }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (dayEntries.isEmpty()) {
            item {
                ModernEmptyStateCard(
                    message = "Inga arbetstider registrerade för denna dag",
                    icon = Icons.Outlined.EventBusy
                )
            }
        } else {
            items(dayEntries) { entry ->
                ModernTimeEntryCard(entry)
            }
        }
    }
}

@Composable
private fun ModernWeekView(currentDate: LocalDate, timeEntries: List<TimeEntry>, settings: Settings) {
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(weekDays) { day ->
            val dayEntries = timeEntries.filter { it.date == day }
            ModernWeekDayCard(day, dayEntries, settings)
        }
    }
}

@Composable
private fun ModernMonthView(currentDate: LocalDate, timeEntries: List<TimeEntry>) {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
    val monthEntries = timeEntries.filter { 
        !it.date.isBefore(firstDayOfMonth) && !it.date.isAfter(lastDayOfMonth) 
    }
    
    val entriesByDate = monthEntries.groupBy { it.date }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (entriesByDate.isEmpty()) {
            item {
                ModernEmptyStateCard(
                    message = "Inga arbetstider registrerade för denna månad",
                    icon = Icons.Outlined.EventAvailable
                )
            }
        } else {
            items(entriesByDate.toList()) { (date, entries) ->
                ModernMonthDayCard(date, entries)
            }
        }
    }
}

@Composable
private fun ModernTimeEntryCard(entry: TimeEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "Arbetstid",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${entry.startTime?.toString() ?: "??:??"} - ${entry.endTime?.toString() ?: "??:??"}",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(12.dp),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "${String.format("%.1f", entry.workHours)}h",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            if (entry.totalPay > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = "Lön",
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Bruttolön: ${String.format("%.0f", entry.totalPay)} kr",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernWeekDayCard(date: LocalDate, entries: List<TimeEntry>, settings: Settings) {
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
    val isToday = date == LocalDate.now()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = when {
            isToday && entries.isNotEmpty() -> MaterialTheme.colors.primary.copy(alpha = 0.15f)
            isToday -> MaterialTheme.colors.secondary.copy(alpha = 0.1f)
            entries.isNotEmpty() -> MaterialTheme.colors.surface
            else -> MaterialTheme.colors.surface.copy(alpha = 0.7f)
        },
        elevation = if (isToday) 8.dp else 4.dp,
        shape = RoundedCornerShape(14.dp),
        border = if (isToday) {
            androidx.compose.foundation.BorderStroke(
                2.dp, 
                MaterialTheme.colors.primary.copy(alpha = 0.3f)
            )
        } else null
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isToday) {
                        Icon(
                            imageVector = Icons.Filled.Today,
                            contentDescription = "Idag",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = date.format(dayFormatter),
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                        color = if (isToday) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                    )
                }
                
                if (totalHours > 0) {
                    Card(
                        backgroundColor = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(10.dp),
                        elevation = 2.dp
                    ) {
                        Text(
                            text = "${String.format("%.1f", totalHours)}h",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            if (entries.isNotEmpty()) {
                entries.forEach { entry ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Tid",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${entry.startTime?.toString() ?: "??:??"}-${entry.endTime?.toString() ?: "??:??"}",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EventBusy,
                        contentDescription = "Ingen arbetstid",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Ingen arbetstid",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernMonthDayCard(date: LocalDate, entries: List<TimeEntry>) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale("sv"))
    val totalHours = entries.sumOf { it.workHours }
    val totalPay = entries.sumOf { it.totalPay }
    val isToday = date == LocalDate.now()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isToday) 
            MaterialTheme.colors.primary.copy(alpha = 0.12f) 
        else 
            MaterialTheme.colors.primary.copy(alpha = 0.05f),
        elevation = if (isToday) 8.dp else 6.dp,
        shape = RoundedCornerShape(16.dp),
        border = if (isToday) {
            androidx.compose.foundation.BorderStroke(
                2.dp, 
                MaterialTheme.colors.primary.copy(alpha = 0.4f)
            )
        } else null
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isToday) {
                        Icon(
                            imageVector = Icons.Filled.Today,
                            contentDescription = "Idag",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = date.format(dayFormatter),
                        style = MaterialTheme.typography.h6,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                        color = if (isToday) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Card(
                        backgroundColor = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(12.dp),
                        elevation = 2.dp
                    ) {
                        Text(
                            text = "${String.format("%.1f", totalHours)}h",
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    
                    if (totalPay > 0) {
                        Card(
                            backgroundColor = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(10.dp),
                            elevation = 2.dp
                        ) {
                            Text(
                                text = "${String.format("%.0f", totalPay)} kr",
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colors.onSecondary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            
            entries.forEach { entry ->
                Card(
                    backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = "Arbetstid",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${entry.startTime?.toString() ?: "??:??"} - ${entry.endTime?.toString() ?: "??:??"}",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernTimelineMonthView(currentDate: LocalDate, timeEntries: List<TimeEntry>) {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
    val monthEntries = timeEntries.filter { 
        !it.date.isBefore(firstDayOfMonth) && !it.date.isAfter(lastDayOfMonth) 
    }
    
    if (monthEntries.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            ModernEmptyStateCard(
                message = "Inga arbetstider registrerade för denna månad",
                icon = Icons.Outlined.Timeline
            )
        }
        return
    }
    
    // Create enhanced timeline grid
    ModernTimelineGrid(
        monthEntries = monthEntries
    )
}

@Composable
private fun ModernTimelineGrid(
    monthEntries: List<TimeEntry>
) {
    // Group entries by date for easier processing
    val entriesByDate = monthEntries.groupBy { it.date }
    
    // Create time slots (hours) from 06:00 to 22:00 (work hours)
    val timeSlots = (6..22).toList()
    
    // Get unique dates in the month with entries
    val datesWithEntries = entriesByDate.keys.sorted()
    
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Enhanced header with dates
            item {
                ModernTimelineHeader(datesWithEntries)
            }
            
            // Enhanced timeline rows for each hour
            items(timeSlots) { hour ->
                ModernTimelineHourRow(
                    hour = hour,
                    datesWithEntries = datesWithEntries,
                    entriesByDate = entriesByDate
                )
            }
        }
    }
}

@Composable
private fun ModernTimelineHeader(datesWithEntries: List<LocalDate>) {
    Card(
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Time column header
            Box(
                modifier = Modifier.width(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tid",
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            }
            
            // Date headers
            datesWithEntries.forEach { date ->
                val isToday = date == LocalDate.now()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        backgroundColor = if (isToday) 
                            MaterialTheme.colors.primary 
                        else 
                            MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(8.dp),
                        elevation = if (isToday) 4.dp else 2.dp
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(
                                text = date.format(DateTimeFormatter.ofPattern("EEE", Locale("sv"))),
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold,
                                color = if (isToday) 
                                    MaterialTheme.colors.onPrimary 
                                else 
                                    MaterialTheme.colors.primary
                            )
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Bold,
                                color = if (isToday) 
                                    MaterialTheme.colors.onPrimary 
                                else 
                                    MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernTimelineHourRow(
    hour: Int,
    datesWithEntries: List<LocalDate>,
    entriesByDate: Map<LocalDate, List<TimeEntry>>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Enhanced time label
        Card(
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.8f),
            shape = RoundedCornerShape(8.dp),
            elevation = 1.dp,
            modifier = Modifier.width(56.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%02d:00", hour),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(4.dp))
        
        // Enhanced timeline blocks for each date
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
                                .clip(RoundedCornerShape(6.dp)),
                            backgroundColor = getWorkTimeColor(entryForHour),
                            elevation = 3.dp
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
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Enhanced empty time slot
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(6.dp),
                        elevation = 0.dp
                    ) {
                        Box(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernEmptyStateCard(
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.8f),
        elevation = 2.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun getWorkTimeColor(entry: TimeEntry): Color {
    // Different colors for different times of day or work types using Material 2 colors
    val startHour = entry.startTime?.hour ?: 0
    return when {
        startHour < 8 -> Color(0xFFE91E63) // Early morning - Pink
        startHour < 12 -> Color(0xFF2196F3) // Morning - Blue  
        startHour < 17 -> Color(0xFFFF9800) // Afternoon - Orange
        startHour < 20 -> Color(0xFF4CAF50) // Evening - Green
        else -> Color(0xFF9C27B0) // Night - Purple
    }
}