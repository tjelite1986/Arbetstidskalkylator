package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timereportcalculator.data.TimeEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import java.util.*

@Composable
fun CalendarView(
    timeEntries: List<TimeEntry>,
    modifier: Modifier = Modifier
) {
    var selectedTimeEntry by remember { mutableStateOf<TimeEntry?>(null) }
    
    ScrollableWeeklyCalendar(
        timeEntries = timeEntries,
        onTimeEntryClick = { timeEntry ->
            selectedTimeEntry = timeEntry
        },
        modifier = modifier
    )
    
    // Show details dialog when a time entry is selected
    selectedTimeEntry?.let { timeEntry ->
        TimeEntryDetailsDialog(
            timeEntry = timeEntry,
            onDismiss = { selectedTimeEntry = null }
        )
    }
}


@Composable
private fun ScrollableWeeklyCalendar(
    timeEntries: List<TimeEntry>,
    onTimeEntryClick: (TimeEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val startOfYear = today.withDayOfYear(1)
    val endOfYear = today.withDayOfYear(today.lengthOfYear())
    
    // Generate all weeks for the year
    val weeks = generateWeeksForYear(startOfYear, endOfYear)
    val entriesByDate = timeEntries.groupBy { it.date }
    
    val listState = rememberLazyListState()
    
    // Find current week index and scroll to it initially
    val currentWeekIndex = weeks.indexOfFirst { week ->
        week.any { it == today }
    }.takeIf { it >= 0 } ?: 0
    
    LaunchedEffect(currentWeekIndex) {
        if (currentWeekIndex > 0) {
            listState.scrollToItem(maxOf(0, currentWeekIndex - 2))
        }
    }
    
    // Calculate the focused month based on the center of the screen
    val focusedMonth by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
            val viewportCenter = layoutInfo.viewportStartOffset + viewportHeight / 2
            
            // Find the item that's closest to the center of the viewport
            val centerItem = layoutInfo.visibleItemsInfo.minByOrNull { itemInfo ->
                val itemCenter = itemInfo.offset + itemInfo.size / 2
                kotlin.math.abs(itemCenter - viewportCenter)
            }
            
            if (centerItem != null && centerItem.index < weeks.size) {
                val centerWeek = weeks[centerItem.index]
                centerWeek[3].month // Wednesday (middle of week)
            } else {
                today.month
            }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Month header showing current focused month
        CalendarHeader(focusedMonth = focusedMonth)
        
        // Weekday headers (mån, tis, ons, etc.)
        WeekdayHeaders()
        
        // Scrollable weekly calendar
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(weeks) { index, week ->
                WeekRow(
                    week = week,
                    entriesByDate = entriesByDate,
                    today = today,
                    focusedMonth = focusedMonth,
                    onTimeEntryClick = onTimeEntryClick
                )
            }
        }
    }
}

private fun generateWeeksForYear(startOfYear: LocalDate, endOfYear: LocalDate): List<List<LocalDate>> {
    val weeks = mutableListOf<List<LocalDate>>()
    
    // Start from the first Monday of the year (or earlier if needed to show full weeks)
    var currentDate = startOfYear.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    
    // Continue until we've covered the entire year plus a few weeks ahead
    val endDate = endOfYear.plusWeeks(4)
    
    while (currentDate.isBefore(endDate)) {
        val week = (0..6).map { currentDate.plusDays(it.toLong()) }
        weeks.add(week)
        currentDate = currentDate.plusWeeks(1)
    }
    
    return weeks
}

@Composable
private fun CalendarHeader(focusedMonth: java.time.Month) {
    val currentYear = LocalDate.now().year
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("sv"))
    val monthDate = LocalDate.of(currentYear, focusedMonth, 1)
    
    Card(
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.08f),
        shape = RoundedCornerShape(12.dp),
        elevation = 1.dp,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = monthDate.format(dateFormatter).replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun WeekdayHeaders() {
    val weekdays = listOf("mån", "ti", "on", "to", "fr", "lö", "sö")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(MaterialTheme.colors.background)
    ) {
        // Empty space for week number column
        Box(modifier = Modifier.width(40.dp))
        
        weekdays.forEach { weekday ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = weekday,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun WeekRow(
    week: List<LocalDate>,
    entriesByDate: Map<LocalDate, List<TimeEntry>>,
    today: LocalDate,
    focusedMonth: java.time.Month,
    onTimeEntryClick: (TimeEntry) -> Unit = {}
) {
    val weekNumber = week[0].get(WeekFields.of(Locale.getDefault()).weekOfYear())
    
    // Calculate the primary month for this week (month that appears most)
    val weekMonth = week[3].month // Use Wednesday as representative day
    
    // Calculate alpha based on distance from focused month
    val monthDistance = kotlin.math.abs(weekMonth.value - focusedMonth.value)
    val alpha = when {
        weekMonth == focusedMonth -> 1.0f // Current month - fully visible
        monthDistance == 1 -> 0.6f // Adjacent months - semi-transparent
        else -> 0.3f // Distant months - very transparent
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 12.dp, vertical = 1.dp)
            .background(
                color = MaterialTheme.colors.background.copy(alpha = alpha),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        // Week number column
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weekNumber.toString(),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f * alpha),
                fontSize = 16.sp
            )
        }
        
        // Days in the week
        week.forEach { date ->
            DayCell(
                date = date,
                entries = entriesByDate[date] ?: emptyList(),
                isToday = date == today,
                alpha = alpha,
                onTimeEntryClick = onTimeEntryClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    entries: List<TimeEntry>,
    isToday: Boolean,
    alpha: Float = 1.0f,
    onTimeEntryClick: (TimeEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val hasEntries = entries.isNotEmpty()
    val isSaturday = date.dayOfWeek.value == 6 // Saturday = 6
    val isSunday = date.dayOfWeek.value == 7 // Sunday = 7
    val isSwedishRedDay = isSwedishRedDay(date)
    val currentMonth = LocalDate.now().month
    val isCurrentMonth = date.month == currentMonth
    
    // Background color logic - Sundays and Swedish red days
    val baseBackgroundColor = when {
        isToday -> Color(0xFFFF6B6B) // Dark red for today (highest priority)
        isSunday || isSwedishRedDay -> Color(0xFFFFCDD2) // Light red for red days (Sundays + official red days)
        isSaturday -> Color(0xFFE8E8E8) // Light gray for Saturdays
        else -> Color(0xFFD0D0D0) // Gray for regular weekdays
    }
    val backgroundColor = baseBackgroundColor.copy(alpha = alpha)
    
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(1.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(2.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Day number - always show clearly, like in the reference screenshots
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.body2,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    isToday -> Color.White // White for today's date for better contrast on red background
                    isCurrentMonth -> Color.Black // Always visible black for current month
                    else -> Color.DarkGray // Dark gray for other months - still clearly visible
                },
                fontSize = 14.sp
            )
            
            // Work time display (start-end times) - clickable when entries exist
            if (hasEntries && entries.isNotEmpty()) {
                val firstEntry = entries.first()
                if (firstEntry.startTime != null && firstEntry.endTime != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                color = Color(0xFF757575).copy(alpha = alpha), // Dark gray background with alpha
                                shape = RoundedCornerShape(2.dp)
                            )
                            .clickable {
                                onTimeEntryClick(firstEntry)
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = String.format("%02d:%02d", firstEntry.startTime!!.hour, firstEntry.startTime!!.minute),
                            style = MaterialTheme.typography.caption,
                            color = Color.White, // Always white text for good readability
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = String.format("%02d:%02d", firstEntry.endTime!!.hour, firstEntry.endTime!!.minute),
                            style = MaterialTheme.typography.caption,
                            color = Color.White, // Always white text for good readability
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// Function to check if a date is an official Swedish "red day" (röd dag)
// Based on the official list for 2025
private fun isSwedishRedDay(date: LocalDate): Boolean {
    val year = date.year
    
    // Fixed red days
    when {
        date == LocalDate.of(year, 1, 1) -> return true // Nyårsdagen
        date == LocalDate.of(year, 1, 6) -> return true // Trettondagen  
        date == LocalDate.of(year, 5, 1) -> return true // Första maj
        date == LocalDate.of(year, 6, 6) -> return true // Nationaldagen
        date == LocalDate.of(year, 12, 25) -> return true // Juldagen
        date == LocalDate.of(year, 12, 26) -> return true // Annandag jul
    }
    
    // Easter-dependent red days
    val easter = calculateEaster(year)
    when (date) {
        easter.minusDays(2) -> return true // Långfredagen
        easter -> return true // Påskdagen  
        easter.plusDays(1) -> return true // Annandag påsk
        easter.plusDays(39) -> return true // Kristi himmelsfärds dag
    }
    
    // Midsommardagen (Saturday between June 20-26)
    val midsummerSaturday = LocalDate.of(year, 6, 20)
        .with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY))
    if (date == midsummerSaturday) return true
    
    // Alla helgons dag (Saturday between October 31 - November 6)  
    val allSaintsDay = LocalDate.of(year, 10, 31)
        .with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY))
    if (date == allSaintsDay) return true
    
    return false
}

// Calculate Easter Sunday using the standard algorithm
private fun calculateEaster(year: Int): LocalDate {
    val a = year % 19
    val b = year / 100
    val c = year % 100
    val d = b / 4
    val e = b % 4
    val f = (b + 8) / 25
    val g = (b - f + 1) / 3
    val h = (19 * a + b - d - g + 15) % 30
    val i = c / 4
    val k = c % 4
    val l = (32 + 2 * e + 2 * i - h - k) % 7
    val m = (a + 11 * h + 22 * l) / 451
    val month = (h + l - 7 * m + 114) / 31
    val day = ((h + l - 7 * m + 114) % 31) + 1
    
    return LocalDate.of(year, month, day)
}




