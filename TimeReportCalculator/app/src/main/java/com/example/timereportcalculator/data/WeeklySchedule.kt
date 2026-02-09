package com.example.timereportcalculator.data

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class WeeklyScheduleEntry(
    val id: String = UUID.randomUUID().toString(),
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val breakMinutes: Int = 0,
    val isEnabled: Boolean = true,
    val template: WorkShiftTemplate? = null
) {
    fun getWorkHours(): Double {
        if (startTime == null || endTime == null || !isEnabled) return 0.0
        val totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes()
        val workMinutes = totalMinutes - breakMinutes
        return workMinutes / 60.0
    }
    
    fun getDisplayText(): String {
        if (!isEnabled || startTime == null || endTime == null) return "Ledig"
        val breakText = if (breakMinutes > 0) " ($breakMinutes min)" else ""
        return "${startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}-${endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}$breakText"
    }
    
    fun getDayName(): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Måndag"
            DayOfWeek.TUESDAY -> "Tisdag"
            DayOfWeek.WEDNESDAY -> "Onsdag"
            DayOfWeek.THURSDAY -> "Torsdag"
            DayOfWeek.FRIDAY -> "Fredag"
            DayOfWeek.SATURDAY -> "Lördag"
            DayOfWeek.SUNDAY -> "Söndag"
        }
    }
    
    fun getShortDayName(): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Mån"
            DayOfWeek.TUESDAY -> "Tis"
            DayOfWeek.WEDNESDAY -> "Ons"
            DayOfWeek.THURSDAY -> "Tor"
            DayOfWeek.FRIDAY -> "Fre"
            DayOfWeek.SATURDAY -> "Lör"
            DayOfWeek.SUNDAY -> "Sön"
        }
    }
}

data class WeeklySchedule(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Mitt Schema",
    val entries: List<WeeklyScheduleEntry> = getDefaultWeeklyEntries(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getTotalWeeklyHours(): Double {
        return entries.sumOf { it.getWorkHours() }
    }
    
    fun getEntryForDay(dayOfWeek: DayOfWeek): WeeklyScheduleEntry? {
        return entries.find { it.dayOfWeek == dayOfWeek }
    }
    
    fun updateEntry(updatedEntry: WeeklyScheduleEntry): WeeklySchedule {
        val updatedEntries = entries.map { entry ->
            if (entry.dayOfWeek == updatedEntry.dayOfWeek) {
                updatedEntry
            } else {
                entry
            }
        }
        return copy(entries = updatedEntries)
    }
    
    fun getWorkingDays(): List<WeeklyScheduleEntry> {
        return entries.filter { it.isEnabled && it.startTime != null && it.endTime != null }
    }
    
    fun createTimeEntryFromSchedule(date: LocalDate, scheduleEntry: WeeklyScheduleEntry): TimeEntry? {
        if (!scheduleEntry.isEnabled || scheduleEntry.startTime == null || scheduleEntry.endTime == null) {
            return null
        }
        
        return TimeEntry(
            date = date,
            startTime = scheduleEntry.startTime,
            endTime = scheduleEntry.endTime,
            breakMinutes = scheduleEntry.breakMinutes
        )
    }
    
    fun getScheduleForDateRange(startDate: LocalDate, endDate: LocalDate): List<TimeEntry> {
        val timeEntries = mutableListOf<TimeEntry>()
        var currentDate = startDate
        
        while (!currentDate.isAfter(endDate)) {
            val dayOfWeek = currentDate.dayOfWeek
            val scheduleEntry = getEntryForDay(dayOfWeek)
            
            scheduleEntry?.let { entry ->
                createTimeEntryFromSchedule(currentDate, entry)?.let { timeEntry ->
                    timeEntries.add(timeEntry)
                }
            }
            
            currentDate = currentDate.plusDays(1)
        }
        
        return timeEntries
    }
    
    companion object {
        fun getDefaultWeeklyEntries(): List<WeeklyScheduleEntry> {
            return DayOfWeek.values().map { dayOfWeek ->
                WeeklyScheduleEntry(
                    dayOfWeek = dayOfWeek,
                    startTime = if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        null
                    } else {
                        LocalTime.of(8, 0)
                    },
                    endTime = if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        null
                    } else {
                        LocalTime.of(17, 0)
                    },
                    breakMinutes = if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        0
                    } else {
                        60
                    },
                    isEnabled = dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY
                )
            }
        }
        
        fun createFromTemplate(template: WorkShiftTemplate, workingDays: List<DayOfWeek> = listOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        )): WeeklySchedule {
            val entries = DayOfWeek.values().map { dayOfWeek ->
                WeeklyScheduleEntry(
                    dayOfWeek = dayOfWeek,
                    startTime = if (workingDays.contains(dayOfWeek)) template.startTime else null,
                    endTime = if (workingDays.contains(dayOfWeek)) template.endTime else null,
                    breakMinutes = if (workingDays.contains(dayOfWeek)) template.breakMinutes else 0,
                    isEnabled = workingDays.contains(dayOfWeek),
                    template = template
                )
            }
            return WeeklySchedule(
                name = "${template.name} Schema",
                entries = entries
            )
        }
    }
}