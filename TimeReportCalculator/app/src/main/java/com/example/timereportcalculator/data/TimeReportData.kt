package com.example.timereportcalculator.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalTime

data class TimeReportData(
    val entries: List<TimeEntryJson>,
    val settings: SettingsJson,
    val lastModified: String = java.time.LocalDateTime.now().toString()
)

data class TimeEntryJson(
    val id: String,
    val date: String, // ISO format: yyyy-MM-dd
    val startTime: String?, // HH:mm format
    val endTime: String?, // HH:mm format
    val breakStart: String?, // HH:mm format
    val breakEnd: String?, // HH:mm format
    val breakMinutes: Int,
    val isRedDay: Boolean,
    val isSickDay: Boolean,
    val sickDayNumber: Int? = null,
    val workHours: Double,
    val basePay: Double,
    val obPay: Double,
    val grossPay: Double? = null, // Nullable för bakåtkompatibilitet
    val vacationPay: Double? = null, // Nullable för bakåtkompatibilitet
    val totalPay: Double,
    val taxAmount: Double,
    val netPay: Double,
    val obBreakdown: Map<String, Double>
)

data class SettingsJson(
    val basePay: Double,
    val taxRate: Double,
    val obRates: OBRatesJson? = null,
    val contractLevel: String? = null,
    val vacationRate: Double? = null,
    val workTimeSettings: WorkTimeSettingsJson? = null,
    val calendarSettings: CalendarSettingsJson? = null,
    val customHolidays: List<HolidayJson>? = null,
    val automaticHolidayDetection: Boolean? = null
)

data class WorkTimeSettingsJson(
    val defaultStartTime: String,
    val defaultEndTime: String,
    val automaticBreaks: Boolean,
    // Nya fält för tre rastnivåer
    val firstBreakThreshold: Double? = null,
    val secondBreakThreshold: Double? = null,
    val thirdBreakThreshold: Double? = null,
    val firstBreakMinutes: Int? = null,
    val secondBreakMinutes: Int? = null,
    val thirdBreakMinutes: Int? = null,
    // Gamla fält för bakåtkompatibilitet
    val shortBreakThreshold: Double? = null,
    val longBreakThreshold: Double? = null,
    val shortBreakMinutes: Int? = null,
    val longBreakMinutes: Int? = null
)

data class CalendarSettingsJson(
    val weekStartsOnMonday: Boolean,
    val monthViewAsDefault: Boolean,
    val showWeekNumbers: Boolean
)

data class HolidayJson(
    val date: String, // ISO format
    val name: String,
    val isCustom: Boolean
)

data class OBRatesJson(
    val workplaceType: String? = null,
    // Butik OB-satser
    val butikWeekday1815to2000: Double? = null,
    val butikWeekdayAfter2000: Double? = null,
    val butikSaturdayAfter1200: Double? = null,
    val butikSundayAllDay: Double? = null,
    val butikRedDayAllDay: Double? = null,
    val butikCoopMorning0500to0600: Double? = null,
    // Lager OB-satser
    val lagerMondayNight0000to0600: Double? = null,
    val lagerWeekdayMorning0600to0700: Double? = null,
    val lagerWeekdayEvening1800to2300: Double? = null,
    val lagerWeekdayNight2300to0600: Double? = null,
    val lagerSaturdayNight0000to0600: Double? = null,
    val lagerSaturdayDay0600to2300: Double? = null,
    val lagerSaturdayNight2300to2400: Double? = null,
    val lagerSundayAllDay: Double? = null,
    val lagerRedDayAllDay: Double? = null,
    // Backward compatibility
    val weekday1815: Double? = null,
    val weekday2000: Double? = null,
    val saturday1200: Double? = null,
    val sundayAllDay: Double? = null,
    val redDayAllDay: Double? = null
)

// Extension functions for conversion
fun TimeEntry.toJson(): TimeEntryJson {
    return TimeEntryJson(
        id = this.id,
        date = this.date.toString(),
        startTime = this.startTime?.toString(),
        endTime = this.endTime?.toString(),
        breakStart = this.breakStart?.toString(),
        breakEnd = this.breakEnd?.toString(),
        breakMinutes = this.breakMinutes,
        isRedDay = this.isRedDay,
        isSickDay = this.isSickDay,
        sickDayNumber = this.sickDayNumber,
        workHours = this.workHours,
        basePay = this.basePay,
        obPay = this.obPay,
        grossPay = this.grossPay,
        vacationPay = this.vacationPay,
        totalPay = this.totalPay,
        taxAmount = this.taxAmount,
        netPay = this.netPay,
        obBreakdown = this.obBreakdown
    )
}

fun TimeEntryJson.toTimeEntry(): TimeEntry {
    return TimeEntry(
        id = this.id,
        date = LocalDate.parse(this.date),
        startTime = this.startTime?.let { LocalTime.parse(it) },
        endTime = this.endTime?.let { LocalTime.parse(it) },
        breakStart = this.breakStart?.let { LocalTime.parse(it) },
        breakEnd = this.breakEnd?.let { LocalTime.parse(it) },
        breakMinutes = this.breakMinutes,
        isRedDay = this.isRedDay,
        isSickDay = this.isSickDay,
        sickDayNumber = this.sickDayNumber ?: 1,
        workHours = this.workHours,
        basePay = this.basePay,
        obPay = this.obPay,
        grossPay = this.grossPay ?: (this.basePay + this.obPay), // Fallback för bakåtkompatibilitet
        vacationPay = this.vacationPay ?: 0.0, // Fallback för bakåtkompatibilitet
        totalPay = this.totalPay,
        taxAmount = this.taxAmount,
        netPay = this.netPay,
        obBreakdown = this.obBreakdown
    )
}

fun Settings.toJson(): SettingsJson {
    return SettingsJson(
        basePay = this.basePay,
        taxRate = this.taxRate,
        obRates = OBRatesJson(
            workplaceType = this.obRates.workplaceType.name,
            butikWeekday1815to2000 = this.obRates.butikWeekday1815to2000,
            butikWeekdayAfter2000 = this.obRates.butikWeekdayAfter2000,
            butikSaturdayAfter1200 = this.obRates.butikSaturdayAfter1200,
            butikSundayAllDay = this.obRates.butikSundayAllDay,
            butikRedDayAllDay = this.obRates.butikRedDayAllDay,
            butikCoopMorning0500to0600 = this.obRates.butikCoopMorning0500to0600,
            lagerMondayNight0000to0600 = this.obRates.lagerMondayNight0000to0600,
            lagerWeekdayMorning0600to0700 = this.obRates.lagerWeekdayMorning0600to0700,
            lagerWeekdayEvening1800to2300 = this.obRates.lagerWeekdayEvening1800to2300,
            lagerWeekdayNight2300to0600 = this.obRates.lagerWeekdayNight2300to0600,
            lagerSaturdayNight0000to0600 = this.obRates.lagerSaturdayNight0000to0600,
            lagerSaturdayDay0600to2300 = this.obRates.lagerSaturdayDay0600to2300,
            lagerSaturdayNight2300to2400 = this.obRates.lagerSaturdayNight2300to2400,
            lagerSundayAllDay = this.obRates.lagerSundayAllDay,
            lagerRedDayAllDay = this.obRates.lagerRedDayAllDay
        ),
        contractLevel = this.contractLevel.name,
        vacationRate = this.vacationRate,
        workTimeSettings = WorkTimeSettingsJson(
            defaultStartTime = this.workTimeSettings.defaultStartTime,
            defaultEndTime = this.workTimeSettings.defaultEndTime,
            automaticBreaks = this.workTimeSettings.automaticBreaks,
            firstBreakThreshold = this.workTimeSettings.firstBreakThreshold,
            secondBreakThreshold = this.workTimeSettings.secondBreakThreshold,
            thirdBreakThreshold = this.workTimeSettings.thirdBreakThreshold,
            firstBreakMinutes = this.workTimeSettings.firstBreakMinutes,
            secondBreakMinutes = this.workTimeSettings.secondBreakMinutes,
            thirdBreakMinutes = this.workTimeSettings.thirdBreakMinutes
        ),
        calendarSettings = CalendarSettingsJson(
            weekStartsOnMonday = this.calendarSettings.weekStartsOnMonday,
            monthViewAsDefault = this.calendarSettings.monthViewAsDefault,
            showWeekNumbers = this.calendarSettings.showWeekNumbers
        ),
        customHolidays = this.customHolidays.map { holiday ->
            HolidayJson(
                date = holiday.date.toString(),
                name = holiday.name,
                isCustom = holiday.isCustom
            )
        },
        automaticHolidayDetection = this.automaticHolidayDetection
    )
}

fun SettingsJson.toSettings(): Settings {
    return Settings(
        basePay = this.basePay,
        taxRate = this.taxRate,
        obRates = this.obRates?.let { obRatesJson ->
            // Check if new format exists
            if (obRatesJson.workplaceType != null) {
                OBRates(
                    workplaceType = WorkplaceType.valueOf(obRatesJson.workplaceType),
                    butikWeekday1815to2000 = obRatesJson.butikWeekday1815to2000 ?: 0.5,
                    butikWeekdayAfter2000 = obRatesJson.butikWeekdayAfter2000 ?: 0.7,
                    butikSaturdayAfter1200 = obRatesJson.butikSaturdayAfter1200 ?: 1.0,
                    butikSundayAllDay = obRatesJson.butikSundayAllDay ?: 1.0,
                    butikRedDayAllDay = obRatesJson.butikRedDayAllDay ?: 1.0,
                    butikCoopMorning0500to0600 = obRatesJson.butikCoopMorning0500to0600 ?: 0.5,
                    lagerMondayNight0000to0600 = obRatesJson.lagerMondayNight0000to0600 ?: 0.7,
                    lagerWeekdayMorning0600to0700 = obRatesJson.lagerWeekdayMorning0600to0700 ?: 0.4,
                    lagerWeekdayEvening1800to2300 = obRatesJson.lagerWeekdayEvening1800to2300 ?: 0.4,
                    lagerWeekdayNight2300to0600 = obRatesJson.lagerWeekdayNight2300to0600 ?: 0.7,
                    lagerSaturdayNight0000to0600 = obRatesJson.lagerSaturdayNight0000to0600 ?: 0.7,
                    lagerSaturdayDay0600to2300 = obRatesJson.lagerSaturdayDay0600to2300 ?: 0.4,
                    lagerSaturdayNight2300to2400 = obRatesJson.lagerSaturdayNight2300to2400 ?: 0.7,
                    lagerSundayAllDay = obRatesJson.lagerSundayAllDay ?: 1.0,
                    lagerRedDayAllDay = obRatesJson.lagerRedDayAllDay ?: 1.0
                )
            } else {
                // Backward compatibility with old format
                OBRates(
                    workplaceType = WorkplaceType.BUTIK,
                    butikWeekday1815to2000 = obRatesJson.weekday1815 ?: 0.5,
                    butikWeekdayAfter2000 = obRatesJson.weekday2000 ?: 0.7,
                    butikSaturdayAfter1200 = obRatesJson.saturday1200 ?: 1.0,
                    butikSundayAllDay = obRatesJson.sundayAllDay ?: 1.0,
                    butikRedDayAllDay = obRatesJson.redDayAllDay ?: 1.0
                )
            }
        } ?: OBRates(),
        contractLevel = this.contractLevel?.let { 
            ContractLevel.valueOf(it) 
        } ?: ContractLevel.EXPERIENCE_2_YEARS,
        vacationRate = this.vacationRate ?: 12.0,
        workTimeSettings = this.workTimeSettings?.let { wts ->
            WorkTimeSettings(
                defaultStartTime = wts.defaultStartTime,
                defaultEndTime = wts.defaultEndTime,
                automaticBreaks = wts.automaticBreaks,
                // Använd nya fält om de finns, annars fallback till gamla
                firstBreakThreshold = wts.firstBreakThreshold ?: wts.shortBreakThreshold ?: 4.0,
                secondBreakThreshold = wts.secondBreakThreshold ?: wts.longBreakThreshold ?: 6.0,
                thirdBreakThreshold = wts.thirdBreakThreshold ?: 8.0,
                firstBreakMinutes = wts.firstBreakMinutes ?: wts.shortBreakMinutes ?: 15,
                secondBreakMinutes = wts.secondBreakMinutes ?: wts.longBreakMinutes ?: 30,
                thirdBreakMinutes = wts.thirdBreakMinutes ?: 60
            )
        } ?: WorkTimeSettings(),
        calendarSettings = this.calendarSettings?.let { cs ->
            CalendarSettings(
                weekStartsOnMonday = cs.weekStartsOnMonday,
                monthViewAsDefault = cs.monthViewAsDefault,
                showWeekNumbers = cs.showWeekNumbers
            )
        } ?: CalendarSettings(),
        customHolidays = this.customHolidays?.map { holidayJson ->
            Holiday(
                date = java.time.LocalDate.parse(holidayJson.date),
                name = holidayJson.name,
                isCustom = holidayJson.isCustom
            )
        } ?: emptyList(),
        automaticHolidayDetection = this.automaticHolidayDetection ?: true
    )
}

// Helper functions for serialization
object TimeReportSerializer {
    private val gson = Gson()
    
    fun toJson(entries: List<TimeEntry>, settings: Settings): String {
        val data = TimeReportData(
            entries = entries.map { it.toJson() },
            settings = settings.toJson()
        )
        return gson.toJson(data)
    }
    
    fun fromJson(json: String): Pair<List<TimeEntry>, Settings> {
        val data = gson.fromJson(json, TimeReportData::class.java)
        return Pair(
            data.entries.map { it.toTimeEntry() },
            data.settings.toSettings()
        )
    }
}