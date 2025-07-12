package com.example.timereportcalculator.data

import java.time.LocalDate
import java.time.Month
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

data class Holiday(
    val date: LocalDate,
    val name: String,
    val isCustom: Boolean = false
)

data class WorkTimeSettings(
    val defaultStartTime: String = "08:00", // HH:mm format
    val defaultEndTime: String = "17:00",   // HH:mm format
    val automaticBreaks: Boolean = true,
    // Tre olika rastnivåer enligt svenska arbetstidsregler
    val firstBreakThreshold: Double = 4.0,   // Första rast vid (timmar)
    val secondBreakThreshold: Double = 6.0,  // Andra rast vid (timmar)  
    val thirdBreakThreshold: Double = 8.0,   // Tredje rast vid (timmar)
    val firstBreakMinutes: Int = 15,         // Första rastens längd
    val secondBreakMinutes: Int = 30,        // Andra rastens längd
    val thirdBreakMinutes: Int = 60          // Tredje rastens längd (60 min vid 8h)
)

data class CalendarSettings(
    val weekStartsOnMonday: Boolean = true,
    val monthViewAsDefault: Boolean = true,
    val showWeekNumbers: Boolean = false
)

object SwedishHolidayCalculator {
    
    fun getSwedishHolidays(year: Int): List<Holiday> {
        val holidays = mutableListOf<Holiday>()
        
        // Fixed holidays
        holidays.add(Holiday(LocalDate.of(year, Month.JANUARY, 1), "Nyårsdagen"))
        holidays.add(Holiday(LocalDate.of(year, Month.JANUARY, 6), "Trettondedag jul"))
        holidays.add(Holiday(LocalDate.of(year, Month.MAY, 1), "Första maj"))
        holidays.add(Holiday(LocalDate.of(year, Month.JUNE, 6), "Sveriges nationaldag"))
        holidays.add(Holiday(LocalDate.of(year, Month.DECEMBER, 24), "Julafton"))
        holidays.add(Holiday(LocalDate.of(year, Month.DECEMBER, 25), "Juldagen"))
        holidays.add(Holiday(LocalDate.of(year, Month.DECEMBER, 26), "Annandag jul"))
        holidays.add(Holiday(LocalDate.of(year, Month.DECEMBER, 31), "Nyårsafton"))
        
        // Easter-dependent holidays
        val easter = calculateEaster(year)
        holidays.add(Holiday(easter.minusDays(2), "Långfredag"))
        holidays.add(Holiday(easter, "Påskdagen"))
        holidays.add(Holiday(easter.plusDays(1), "Annandag påsk"))
        holidays.add(Holiday(easter.plusDays(39), "Kristi himmelsfärdsdag"))
        holidays.add(Holiday(easter.plusDays(49), "Pingstdagen"))
        
        // Midsummer (Friday between June 19-25)
        val midsummerFriday = LocalDate.of(year, Month.JUNE, 19)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY))
        if (midsummerFriday.dayOfMonth > 25) {
            // If June 19 is Saturday, midsummer is June 25
            holidays.add(Holiday(LocalDate.of(year, Month.JUNE, 25), "Midsommarafton"))
        } else {
            holidays.add(Holiday(midsummerFriday, "Midsommarafton"))
        }
        holidays.add(Holiday(midsummerFriday.plusDays(1), "Midsommardagen"))
        
        // All Saints' Day (Saturday between October 31 - November 6)
        val allSaintsDay = LocalDate.of(year, Month.OCTOBER, 31)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        holidays.add(Holiday(allSaintsDay, "Alla helgons dag"))
        
        return holidays.sortedBy { it.date }
    }
    
    private fun calculateEaster(year: Int): LocalDate {
        // Using the algorithm for calculating Easter Sunday
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
    
    fun isSwedishHoliday(date: LocalDate): Boolean {
        val holidays = getSwedishHolidays(date.year)
        return holidays.any { it.date == date }
    }
    
    fun getHolidayName(date: LocalDate): String? {
        val holidays = getSwedishHolidays(date.year)
        return holidays.find { it.date == date }?.name
    }
}

class HolidayManager(private val customHolidays: List<Holiday> = emptyList()) {
    
    fun isHoliday(date: LocalDate): Boolean {
        return SwedishHolidayCalculator.isSwedishHoliday(date) ||
                customHolidays.any { it.date == date }
    }
    
    fun getHolidayName(date: LocalDate): String? {
        // Check Swedish holidays first
        SwedishHolidayCalculator.getHolidayName(date)?.let { return it }
        
        // Then check custom holidays
        return customHolidays.find { it.date == date }?.name
    }
    
    fun getAllHolidays(year: Int): List<Holiday> {
        val swedishHolidays = SwedishHolidayCalculator.getSwedishHolidays(year)
        val yearCustomHolidays = customHolidays.filter { it.date.year == year }
        return (swedishHolidays + yearCustomHolidays).sortedBy { it.date }
    }
    
    fun addCustomHoliday(date: LocalDate, name: String): List<Holiday> {
        val newHoliday = Holiday(date, name, isCustom = true)
        return customHolidays + newHoliday
    }
    
    fun removeCustomHoliday(date: LocalDate): List<Holiday> {
        return customHolidays.filter { it.date != date || !it.isCustom }
    }
}