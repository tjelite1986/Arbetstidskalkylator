package com.example.timereportcalculator.data

import java.time.LocalTime
import java.util.UUID

data class WorkShiftTemplate(
    val id: String = UUID.randomUUID().toString(),
    val startTime: LocalTime,
    val endTime: LocalTime,
    val breakMinutes: Int = 0
) {
    fun getWorkHours(): Double {
        val totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes()
        val workMinutes = totalMinutes - breakMinutes
        return workMinutes / 60.0
    }
    
    fun getDisplayText(): String {
        val breakText = if (breakMinutes > 0) "($breakMinutes)" else ""
        val hours = String.format("%.0f", getWorkHours())
        return "${startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}-${endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}${breakText} ${hours}h"
    }
    
    companion object {
        // Fördefinierade mallar
        fun getDefaultTemplates(): List<WorkShiftTemplate> {
            return listOf(
                WorkShiftTemplate(
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(17, 0),
                    breakMinutes = 60
                ),
                WorkShiftTemplate(
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(18, 0),
                    breakMinutes = 60
                ),
                WorkShiftTemplate(
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(15, 0),
                    breakMinutes = 30
                ),
                WorkShiftTemplate(
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(22, 0),
                    breakMinutes = 45
                ),
                WorkShiftTemplate(
                    startTime = LocalTime.of(22, 0),
                    endTime = LocalTime.of(6, 0),
                    breakMinutes = 30
                ),
                WorkShiftTemplate(
                    startTime = LocalTime.of(6, 0),
                    endTime = LocalTime.of(16, 0),
                    breakMinutes = 60
                )
            )
        }
    }
}