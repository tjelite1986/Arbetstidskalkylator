package com.example.timereportcalculator.data

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class TimeEntry(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val breakStart: LocalTime? = null,
    val breakEnd: LocalTime? = null,
    val breakMinutes: Int = 0,
    val isRedDay: Boolean = false,
    val isSickDay: Boolean = false,
    val sickDayNumber: Int = 1, // 1 = karensdag (0 kr), 2-14 = 80% av grundlön
    val workHours: Double = 0.0,
    val basePay: Double = 0.0,
    val obPay: Double = 0.0,
    val totalPay: Double = 0.0,
    val taxAmount: Double = 0.0,
    val netPay: Double = 0.0,
    val obBreakdown: Map<String, Double> = emptyMap()
)

data class Settings(
    val basePay: Double = 162.98,
    val taxRate: Double = 30.0,
    val obRates: OBRates = OBRates(),
    val contractLevel: ContractLevel = ContractLevel.EXPERIENCE_2_YEARS,
    val vacationRate: Double = 12.0, // Semesterersättning i procent
    val workTimeSettings: WorkTimeSettings = WorkTimeSettings(),
    val calendarSettings: CalendarSettings = CalendarSettings(),
    val customHolidays: List<Holiday> = emptyList(),
    val automaticHolidayDetection: Boolean = true
)

data class OBRates(
    val workplaceType: WorkplaceType = WorkplaceType.BUTIK,
    // Butik OB-satser
    val butikWeekday1815to2000: Double = 0.5,  // 50% måndag-fredag 18:15-20:00
    val butikWeekdayAfter2000: Double = 0.7,   // 70% måndag-fredag efter 20:00
    val butikSaturdayAfter1200: Double = 1.0,  // 100% lördag efter 12:00
    val butikSundayAllDay: Double = 1.0,       // 100% söndag hela dagen
    val butikRedDayAllDay: Double = 1.0,       // 100% helgdagar hela dagen
    val butikCoopMorning0500to0600: Double = 0.5, // 50% måndag-lördag 05:00-06:00 (Coop)
    
    // Lager OB-satser
    val lagerMondayNight0000to0600: Double = 0.7,  // 70% måndag 00:00-06:00
    val lagerWeekdayMorning0600to0700: Double = 0.4, // 40% måndag-fredag 06:00-07:00
    val lagerWeekdayEvening1800to2300: Double = 0.4, // 40% måndag-fredag 18:00-23:00
    val lagerWeekdayNight2300to0600: Double = 0.7,   // 70% måndag-fredag 23:00-06:00
    val lagerSaturdayNight0000to0600: Double = 0.7,  // 70% lördag 00:00-06:00
    val lagerSaturdayDay0600to2300: Double = 0.4,    // 40% lördag 06:00-23:00
    val lagerSaturdayNight2300to2400: Double = 0.7,  // 70% lördag 23:00-24:00
    val lagerSundayAllDay: Double = 1.0,             // 100% söndag hela dagen
    val lagerRedDayAllDay: Double = 1.0              // 100% helgdagar hela dagen
)

enum class WorkplaceType(
    val displayName: String,
    val description: String
) {
    BUTIK("Butik", "Detaljhandel enligt Handelsavtalet"),
    LAGER("Lager", "Lager/E-handel enligt Handelsavtalet")
}

enum class ContractLevel(
    val displayName: String,
    val minimumWage: Double,
    val description: String
) {
    AGE_16("16 år", 101.48, "Minimilön för 16-åringar enligt Detaljhandelsavtalet 2025-2026"),
    AGE_17("17 år", 103.95, "Minimilön för 17-åringar enligt Detaljhandelsavtalet 2025-2026"),
    AGE_18("18 år", 155.51, "Minimilön för 18-åringar enligt Detaljhandelsavtalet 2025-2026"),
    AGE_19("19 år", 157.44, "Minimilön för 19-åringar enligt Detaljhandelsavtalet 2025-2026"),
    EXPERIENCE_1_YEAR("1 års erfarenhet", 160.95, "Minimilön efter 1 års branschexperience"),
    EXPERIENCE_2_YEARS("2 års erfarenhet", 162.98, "Minimilön efter 2 års branschexperience"),
    EXPERIENCE_3_PLUS_YEARS("3+ års erfarenhet", 165.84, "Minimilön efter 3+ års branschexperience"),
    CUSTOM("Anpassad", 162.98, "Anpassad lönenivå")
}