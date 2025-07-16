package com.example.timereportcalculator.calculator

import com.example.timereportcalculator.data.OBRates
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.data.WorkplaceType
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.max

class PayCalculator {
    
    fun calculatePay(entry: TimeEntry, settings: Settings): TimeEntry {
        // Validate input parameters
        require(settings.basePay >= 0) { "Base pay cannot be negative" }
        require(settings.taxRate in 0.0..100.0) { "Tax rate must be between 0 and 100 percent" }
        
        if (entry.isSickDay) {
            return calculateSickPay(entry, settings)
        }
        
        val workHours = calculateWorkHoursWithAutomaticBreaks(entry, settings)
        if (workHours < 0) {
            throw IllegalArgumentException("Work hours cannot be negative. Check start/end times.")
        }
        
        val basePay = workHours * settings.basePay
        val (obPay, obBreakdown) = calculateOBPay(entry, settings, workHours)
        val grossPay = basePay + obPay
        val vacationPay = grossPay * (settings.vacationRate / 100.0)
        val totalPay = grossPay + vacationPay
        val taxAmount = totalPay * (settings.taxRate / 100.0)
        val netPay = totalPay - taxAmount
        
        // Beräkna automatiska rastminuter om de används
        val automaticBreakMinutes = if (entry.breakStart == null && entry.breakEnd == null && entry.breakMinutes == 0 && settings.workTimeSettings.automaticBreaks) {
            val totalMinutes = if (entry.startTime != null && entry.endTime != null) {
                ChronoUnit.MINUTES.between(entry.startTime, entry.endTime)
            } else 0
            val initialWorkHours = totalMinutes / 60.0
            calculateAutomaticBreaks(initialWorkHours, settings)
        } else {
            entry.breakMinutes
        }
        
        return entry.copy(
            workHours = workHours,
            basePay = basePay,
            obPay = obPay,
            grossPay = grossPay,
            vacationPay = vacationPay,
            totalPay = totalPay,
            taxAmount = taxAmount,
            netPay = netPay,
            breakMinutes = automaticBreakMinutes,
            obBreakdown = obBreakdown
        )
    }
    
    private fun calculateSickPay(entry: TimeEntry, settings: Settings): TimeEntry {
        // Svenska sjuklöneregler för detaljhandeln:
        // Dag 1: Karensdag - ingen lön (0 kr) 
        // Dag 2+: 80% av grundlön för angivna arbetstimmar (inte automatiska 8h)
        
        val sickPayHours = if (entry.sickDayNumber == 1) {
            0.0 // Karensdag - inga timmar, ingen lön
        } else {
            // Använd angivna arbetstimmar från TimeEntry
            entry.workHours
        }
        
        val grossPay = if (entry.sickDayNumber == 1) {
            // Karensdag - ingen lön
            0.0
        } else {
            // Dag 2+: 80% av grundlön för angivna timmar
            val sickPayRate = settings.basePay * 0.8
            sickPayHours * sickPayRate
        }
        
        val vacationPay = grossPay * (settings.vacationRate / 100.0)
        val totalPay = grossPay + vacationPay
        val taxAmount = totalPay * (settings.taxRate / 100.0)
        val netPay = totalPay - taxAmount
        
        return entry.copy(
            workHours = sickPayHours,
            basePay = grossPay,
            obPay = 0.0,
            grossPay = grossPay,
            vacationPay = vacationPay,
            totalPay = totalPay,
            taxAmount = taxAmount,
            netPay = netPay,
            obBreakdown = emptyMap()
        )
    }
    
    private fun calculateWorkHours(entry: TimeEntry): Double {
        if (entry.startTime == null || entry.endTime == null) return 0.0
        
        // Validate time range
        if (entry.endTime.isBefore(entry.startTime)) {
            throw IllegalArgumentException("End time cannot be before start time")
        }
        
        val totalMinutes = ChronoUnit.MINUTES.between(entry.startTime, entry.endTime)
        val breakMinutes = getBreakMinutes(entry)
        
        // Validate break time doesn't exceed work time
        if (breakMinutes > totalMinutes) {
            throw IllegalArgumentException("Break time cannot exceed total work time")
        }
        
        val workMinutes = max(0, totalMinutes - breakMinutes)
        
        return workMinutes / 60.0
    }
    
    private fun calculateWorkHoursWithAutomaticBreaks(entry: TimeEntry, settings: Settings): Double {
        if (entry.startTime == null || entry.endTime == null) return 0.0
        
        // Validate time range
        if (entry.endTime.isBefore(entry.startTime)) {
            throw IllegalArgumentException("End time cannot be before start time")
        }
        
        val totalMinutes = ChronoUnit.MINUTES.between(entry.startTime, entry.endTime)
        
        // Använd manuella raster om de är angivna, annars automatiska
        val breakMinutes = if (entry.breakStart != null && entry.breakEnd != null) {
            // Manuella raster
            if (entry.breakEnd.isBefore(entry.breakStart)) {
                throw IllegalArgumentException("Break end time cannot be before break start time")
            }
            ChronoUnit.MINUTES.between(entry.breakStart, entry.breakEnd)
        } else if (entry.breakMinutes > 0) {
            // Manuella rastminuter
            entry.breakMinutes.toLong()
        } else {
            // Automatiska raster - beräkna baserat på total arbetstid först
            val initialWorkHours = totalMinutes / 60.0
            calculateAutomaticBreaks(initialWorkHours, settings).toLong()
        }
        
        // Validate break time doesn't exceed work time
        if (breakMinutes > totalMinutes) {
            throw IllegalArgumentException("Break time cannot exceed total work time")
        }
        
        val workMinutes = max(0, totalMinutes - breakMinutes)
        
        return workMinutes / 60.0
    }
    
    private fun getBreakMinutes(entry: TimeEntry): Long {
        return if (entry.breakStart != null && entry.breakEnd != null) {
            // Validate break times
            if (entry.breakEnd.isBefore(entry.breakStart)) {
                throw IllegalArgumentException("Break end time cannot be before break start time")
            }
            ChronoUnit.MINUTES.between(entry.breakStart, entry.breakEnd)
        } else {
            // Validate break minutes are not negative
            if (entry.breakMinutes < 0) {
                throw IllegalArgumentException("Break minutes cannot be negative")
            }
            entry.breakMinutes.toLong()
        }
    }
    
    private fun calculateAutomaticBreaks(workHours: Double, settings: Settings): Int {
        if (!settings.workTimeSettings.automaticBreaks) {
            return 0
        }
        
        // Välj rätt rast baserat på arbetstid - högsta tröskelvärdet som uppfylls
        return when {
            workHours >= settings.workTimeSettings.thirdBreakThreshold -> settings.workTimeSettings.thirdBreakMinutes
            workHours >= settings.workTimeSettings.secondBreakThreshold -> settings.workTimeSettings.secondBreakMinutes
            workHours >= settings.workTimeSettings.firstBreakThreshold -> settings.workTimeSettings.firstBreakMinutes
            else -> 0
        }
    }
    
    private fun calculateOBPay(entry: TimeEntry, settings: Settings, workHours: Double): Pair<Double, Map<String, Double>> {
        if (entry.startTime == null || entry.endTime == null || workHours <= 0) return Pair(0.0, emptyMap())
        
        val dayOfWeek = entry.date.dayOfWeek
        val obRates = settings.obRates
        
        return when (obRates.workplaceType) {
            WorkplaceType.BUTIK -> {
                calculateButikOBPay(entry, settings, workHours, dayOfWeek)
            }
            WorkplaceType.LAGER -> {
                calculateLagerOBPay(entry, settings, workHours, dayOfWeek)
            }
        }
    }
    
    private fun calculateButikOBPay(entry: TimeEntry, settings: Settings, workHours: Double, dayOfWeek: DayOfWeek): Pair<Double, Map<String, Double>> {
        val obRates = settings.obRates
        var obPay = 0.0
        val obBreakdown = mutableMapOf<String, Double>()
        
        // Red day gets 100% bonus for all hours
        if (entry.isRedDay) {
            val redDayPay = workHours * settings.basePay * obRates.butikRedDayAllDay
            obPay += redDayPay
            if (redDayPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.butikRedDayAllDay * 100)}% (röd dag)"] = redDayPay
            }
        }
        // Sunday gets 100% bonus for all hours
        else if (dayOfWeek == DayOfWeek.SUNDAY) {
            val sundayPay = workHours * settings.basePay * obRates.butikSundayAllDay
            obPay += sundayPay
            if (sundayPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.butikSundayAllDay * 100)}% (söndag)"] = sundayPay
            }
        }
        // Saturday gets 100% bonus after 12:00
        else if (dayOfWeek == DayOfWeek.SATURDAY) {
            val saturdayOBTime = LocalTime.of(12, 0)
            if (entry.endTime!!.isAfter(saturdayOBTime)) {
                val obStart = maxOf(entry.startTime!!, saturdayOBTime)
                val obMinutes = ChronoUnit.MINUTES.between(obStart, entry.endTime)
                val obHours = obMinutes / 60.0
                val saturdayPay = obHours * settings.basePay * obRates.butikSaturdayAfter1200
                obPay += saturdayPay
                if (saturdayPay > 0) {
                    obBreakdown["OB-tillägg ${String.format("%.0f", obRates.butikSaturdayAfter1200 * 100)}% (lördag efter 12:00)"] = saturdayPay
                }
            }
        }
        // Weekdays (Monday-Friday)
        else if (dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY) {
            val weekday1815 = LocalTime.of(18, 15)
            val weekday2000 = LocalTime.of(20, 0)
            
            // 50% bonus 18:15-20:00
            if (entry.endTime!!.isAfter(weekday1815)) {
                val obStart1815 = maxOf(entry.startTime!!, weekday1815)
                val obEnd1815 = minOf(entry.endTime, weekday2000)
                val obMinutes1815 = ChronoUnit.MINUTES.between(obStart1815, obEnd1815)
                val obHours1815 = max(0, obMinutes1815) / 60.0
                val weekdayPay1815 = obHours1815 * settings.basePay * obRates.butikWeekday1815to2000
                obPay += weekdayPay1815
                if (weekdayPay1815 > 0) {
                    obBreakdown["OB-tillägg ${String.format("%.0f", obRates.butikWeekday1815to2000 * 100)}% (18:15-20:00)"] = weekdayPay1815
                }
            }
            
            // 70% bonus after 20:00
            if (entry.endTime.isAfter(weekday2000)) {
                val obStart2000 = maxOf(entry.startTime!!, weekday2000)
                val obMinutes2000 = ChronoUnit.MINUTES.between(obStart2000, entry.endTime)
                val obHours2000 = obMinutes2000 / 60.0
                val weekdayPay2000 = obHours2000 * settings.basePay * obRates.butikWeekdayAfter2000
                obPay += weekdayPay2000
                if (weekdayPay2000 > 0) {
                    obBreakdown["OB-tillägg ${String.format("%.0f", obRates.butikWeekdayAfter2000 * 100)}% (efter 20:00)"] = weekdayPay2000
                }
            }
        }
        
        return Pair(obPay, obBreakdown)
    }
    
    private fun calculateLagerOBPay(entry: TimeEntry, settings: Settings, workHours: Double, dayOfWeek: DayOfWeek): Pair<Double, Map<String, Double>> {
        val obRates = settings.obRates
        var obPay = 0.0
        val obBreakdown = mutableMapOf<String, Double>()
        
        // Red day gets 100% bonus for all hours
        if (entry.isRedDay) {
            val redDayPay = workHours * settings.basePay * obRates.lagerRedDayAllDay
            obPay += redDayPay
            if (redDayPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerRedDayAllDay * 100)}% (röd dag)"] = redDayPay
            }
        }
        // Sunday gets 100% bonus for all hours
        else if (dayOfWeek == DayOfWeek.SUNDAY) {
            val sundayPay = workHours * settings.basePay * obRates.lagerSundayAllDay
            obPay += sundayPay
            if (sundayPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerSundayAllDay * 100)}% (söndag)"] = sundayPay
            }
        }
        // Monday special night rate 00:00-06:00
        else if (dayOfWeek == DayOfWeek.MONDAY) {
            val mondayNightStart = LocalTime.of(0, 0)
            val mondayNightEnd = LocalTime.of(6, 0)
            val mondayNightPay = calculateOBForTimeRange(entry, settings, mondayNightStart, mondayNightEnd, obRates.lagerMondayNight0000to0600)
            obPay += mondayNightPay
            if (mondayNightPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerMondayNight0000to0600 * 100)}% (måndag natt)"] = mondayNightPay
            }
        }
        // Saturday has different rates
        else if (dayOfWeek == DayOfWeek.SATURDAY) {
            val satNight1Start = LocalTime.of(0, 0)
            val satNight1End = LocalTime.of(6, 0)
            val satNight1Pay = calculateOBForTimeRange(entry, settings, satNight1Start, satNight1End, obRates.lagerSaturdayNight0000to0600)
            obPay += satNight1Pay
            if (satNight1Pay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerSaturdayNight0000to0600 * 100)}% (lördag natt)"] = satNight1Pay
            }
            
            val satDayStart = LocalTime.of(6, 0)
            val satDayEnd = LocalTime.of(23, 0)
            val satDayPay = calculateOBForTimeRange(entry, settings, satDayStart, satDayEnd, obRates.lagerSaturdayDay0600to2300)
            obPay += satDayPay
            if (satDayPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerSaturdayDay0600to2300 * 100)}% (lördag dag)"] = satDayPay
            }
            
            val satNight2Start = LocalTime.of(23, 0)
            val satNight2End = LocalTime.of(23, 59)
            val satNight2Pay = calculateOBForTimeRange(entry, settings, satNight2Start, satNight2End, obRates.lagerSaturdayNight2300to2400)
            obPay += satNight2Pay
            if (satNight2Pay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerSaturdayNight2300to2400 * 100)}% (lördag sen kväll)"] = satNight2Pay
            }
        }
        // Weekdays (Tuesday-Friday for lager)
        else if (dayOfWeek in DayOfWeek.TUESDAY..DayOfWeek.FRIDAY) {
            val morningStart = LocalTime.of(6, 0)
            val morningEnd = LocalTime.of(7, 0)
            val morningPay = calculateOBForTimeRange(entry, settings, morningStart, morningEnd, obRates.lagerWeekdayMorning0600to0700)
            obPay += morningPay
            if (morningPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerWeekdayMorning0600to0700 * 100)}% (morgon)"] = morningPay
            }
            
            val eveningStart = LocalTime.of(18, 0)
            val eveningEnd = LocalTime.of(23, 0)
            val eveningPay = calculateOBForTimeRange(entry, settings, eveningStart, eveningEnd, obRates.lagerWeekdayEvening1800to2300)
            obPay += eveningPay
            if (eveningPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerWeekdayEvening1800to2300 * 100)}% (kväll)"] = eveningPay
            }
            
            val nightStart = LocalTime.of(23, 0)
            val nightEnd = LocalTime.of(6, 0) // Next day
            val nightPay = calculateOBForTimeRange(entry, settings, nightStart, nightEnd, obRates.lagerWeekdayNight2300to0600)
            obPay += nightPay
            if (nightPay > 0) {
                obBreakdown["OB-tillägg ${String.format("%.0f", obRates.lagerWeekdayNight2300to0600 * 100)}% (natt)"] = nightPay
            }
        }
        
        return Pair(obPay, obBreakdown)
    }
    
    private fun calculateOBForTimeRange(entry: TimeEntry, settings: Settings, rangeStart: LocalTime, rangeEnd: LocalTime, rate: Double): Double {
        if (entry.startTime == null || entry.endTime == null) return 0.0
        
        val workStart = entry.startTime
        val workEnd = entry.endTime
        
        // Handle overnight shifts (when rangeEnd is before rangeStart, e.g., 23:00-06:00)
        if (rangeEnd.isBefore(rangeStart)) {
            // Split into two periods: rangeStart-24:00 and 00:00-rangeEnd
            val midnightEnd = LocalTime.of(23, 59)
            val midnightStart = LocalTime.of(0, 0)
            
            val obPay1 = if (workEnd.isAfter(rangeStart)) {
                val obStart = maxOf(workStart, rangeStart)
                val obEnd = minOf(workEnd, midnightEnd)
                val obMinutes = ChronoUnit.MINUTES.between(obStart, obEnd)
                max(0, obMinutes) / 60.0 * settings.basePay * rate
            } else 0.0
            
            val obPay2 = if (workStart.isBefore(rangeEnd)) {
                val obStart = maxOf(workStart, midnightStart)
                val obEnd = minOf(workEnd, rangeEnd)
                val obMinutes = ChronoUnit.MINUTES.between(obStart, obEnd)
                max(0, obMinutes) / 60.0 * settings.basePay * rate
            } else 0.0
            
            return obPay1 + obPay2
        } else {
            // Normal time range
            if (workEnd.isAfter(rangeStart) && workStart.isBefore(rangeEnd)) {
                val obStart = maxOf(workStart, rangeStart)
                val obEnd = minOf(workEnd, rangeEnd)
                val obMinutes = ChronoUnit.MINUTES.between(obStart, obEnd)
                val obHours = max(0, obMinutes) / 60.0
                return obHours * settings.basePay * rate
            }
        }
        
        return 0.0
    }
    
    private fun maxOf(time1: LocalTime, time2: LocalTime): LocalTime {
        return if (time1.isAfter(time2)) time1 else time2
    }
    
    private fun minOf(time1: LocalTime, time2: LocalTime): LocalTime {
        return if (time1.isBefore(time2)) time1 else time2
    }
}