package com.example.timereportcalculator.data

import java.time.LocalTime
import java.util.UUID

data class WorkShiftTemplate(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val startTime: LocalTime,
    val endTime: LocalTime,
    val breakMinutes: Int = 0,
    val isFavorite: Boolean = false
) {
    fun getWorkHours(): Double {
        val totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes()
        val workMinutes = totalMinutes - breakMinutes
        return workMinutes / 60.0
    }
    
    fun getDisplayText(): String {
        val breakText = if (breakMinutes > 0) "($breakMinutes min)" else ""
        val nameText = if (name.isNotEmpty()) "$name: " else ""
        return "$nameText${startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}-${endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))} $breakText"
    }
    
    fun getShortDisplayText(): String {
        val nameText = if (name.isNotEmpty()) name else "${startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}-${endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}"
        return nameText
    }
    
    companion object {
        // Fördefinierade mallar
        fun getDefaultTemplates(): List<WorkShiftTemplate> {
            return listOf(
                WorkShiftTemplate(
                    name = "Kontorstid",
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(17, 0),
                    breakMinutes = 60,
                    isFavorite = true
                ),
                WorkShiftTemplate(
                    name = "Sent start",
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(18, 0),
                    breakMinutes = 60,
                    isFavorite = true
                ),
                WorkShiftTemplate(
                    name = "Deltid",
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(15, 0),
                    breakMinutes = 30,
                    isFavorite = true
                ),
                WorkShiftTemplate(
                    name = "Kväll",
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(22, 0),
                    breakMinutes = 45,
                    isFavorite = true
                ),
                WorkShiftTemplate(
                    name = "Natt",
                    startTime = LocalTime.of(22, 0),
                    endTime = LocalTime.of(6, 0),
                    breakMinutes = 30
                ),
                WorkShiftTemplate(
                    name = "Tidigt",
                    startTime = LocalTime.of(6, 0),
                    endTime = LocalTime.of(16, 0),
                    breakMinutes = 60
                )
            )
        }
    }
}