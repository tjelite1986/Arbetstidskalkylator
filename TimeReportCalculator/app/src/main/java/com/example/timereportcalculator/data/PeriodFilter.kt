package com.example.timereportcalculator.data

import java.time.LocalDate
import java.time.YearMonth

enum class PeriodType(val displayName: String) {
    ALL("Alla rader"),
    THIS_MONTH("Denna månad"),
    LAST_MONTH("Förra månaden"),
    CUSTOM("Anpassad period")
}

data class PeriodFilter(
    val type: PeriodType = PeriodType.ALL,
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null
) {
    fun getDateRange(): Pair<LocalDate?, LocalDate?> {
        return when (type) {
            PeriodType.ALL -> Pair(null, null)
            PeriodType.THIS_MONTH -> {
                val thisMonth = YearMonth.now()
                Pair(thisMonth.atDay(1), thisMonth.atEndOfMonth())
            }
            PeriodType.LAST_MONTH -> {
                val lastMonth = YearMonth.now().minusMonths(1)
                Pair(lastMonth.atDay(1), lastMonth.atEndOfMonth())
            }
            PeriodType.CUSTOM -> Pair(customStartDate, customEndDate)
        }
    }
    
    fun getDisplayText(): String {
        return when (type) {
            PeriodType.CUSTOM -> {
                if (customStartDate != null && customEndDate != null) {
                    "${customStartDate} - ${customEndDate}"
                } else {
                    type.displayName
                }
            }
            else -> type.displayName
        }
    }
}