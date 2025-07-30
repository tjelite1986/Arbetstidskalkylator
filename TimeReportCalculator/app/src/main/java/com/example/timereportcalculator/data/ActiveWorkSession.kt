package com.example.timereportcalculator.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class ActiveWorkSession(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalDateTime,
    val currentTime: LocalDateTime = LocalDateTime.now(),
    val isOnBreak: Boolean = false,
    val breakStartTime: LocalDateTime? = null,
    val breakMinutesSoFar: Int = 0,
    val breakHistory: List<BreakPeriod> = emptyList(),
    val description: String = "",
    val currentEarnings: SessionEarnings = SessionEarnings()
) {
    fun getWorkDurationMinutes(): Long {
        val now = if (currentTime == startTime) LocalDateTime.now() else currentTime
        val startDateTime = startTime
        val totalWorkMinutes = java.time.Duration.between(startDateTime, now).toMinutes()
        return totalWorkMinutes - getTotalBreakMinutes()
    }
    
    fun getCurrentBreakDurationMinutes(): Int {
        return if (isOnBreak && breakStartTime != null) {
            val now = if (currentTime == startTime) LocalDateTime.now() else currentTime
            java.time.Duration.between(breakStartTime, now).toMinutes().toInt()
        } else {
            0
        }
    }
    
    fun getTotalBreakMinutes(): Int {
        val completedBreaks = breakHistory.sumOf { it.durationMinutes }
        val currentBreak = getCurrentBreakDurationMinutes()
        return completedBreaks + currentBreak
    }
    
    fun getWorkHours(): Double {
        return getWorkDurationMinutes() / 60.0
    }
}

data class BreakPeriod(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val durationMinutes: Int = java.time.Duration.between(startTime, endTime).toMinutes().toInt(),
    val breakType: BreakType = BreakType.REGULAR
)

enum class BreakType(val displayName: String) {
    REGULAR("Rast"),
    LUNCH("Lunch"),
    FIKA("Fika"),
    OTHER("Annat")
}

data class SessionEarnings(
    val basePay: Double = 0.0,
    val obPay: Double = 0.0,
    val grossPay: Double = 0.0,
    val vacationPay: Double = 0.0,
    val totalPay: Double = 0.0,
    val netPay: Double = 0.0,
    val obBreakdown: Map<String, Double> = emptyMap(),
    val currentOBRate: Double = 0.0,
    val isCurrentlyEligibleForOB: Boolean = false,
    val nextOBChangeTime: LocalTime? = null
)

enum class TimerState {
    STOPPED,
    RUNNING,
    ON_BREAK,
    PAUSED
}