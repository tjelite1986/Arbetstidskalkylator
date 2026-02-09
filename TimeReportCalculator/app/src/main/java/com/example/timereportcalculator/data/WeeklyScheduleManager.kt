package com.example.timereportcalculator.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.LocalTime

class WeeklyScheduleManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("weekly_schedules", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_SCHEDULES = "schedules"
        private const val KEY_ACTIVE_SCHEDULE = "active_schedule"
        private const val KEY_INITIALIZED = "initialized"
    }
    
    init {
        // Initialize with default schedule if first time
        if (!prefs.getBoolean(KEY_INITIALIZED, false)) {
            initializeDefaultSchedule()
        }
    }
    
    private fun initializeDefaultSchedule() {
        val defaultSchedule = WeeklySchedule()
        saveSchedule(defaultSchedule)
        setActiveSchedule(defaultSchedule.id)
        prefs.edit().putBoolean(KEY_INITIALIZED, true).apply()
    }
    
    fun getAllSchedules(): List<WeeklySchedule> {
        val json = prefs.getString(KEY_SCHEDULES, null) ?: return emptyList()
        try {
            val type = object : TypeToken<List<WeeklyScheduleDto>>() {}.type
            val dtos: List<WeeklyScheduleDto> = gson.fromJson(json, type)
            return dtos.map { it.toWeeklySchedule() }
        } catch (e: Exception) {
            return emptyList()
        }
    }
    
    fun getActiveSchedule(): WeeklySchedule? {
        val activeId = prefs.getString(KEY_ACTIVE_SCHEDULE, null) ?: return null
        return getScheduleById(activeId)
    }
    
    fun getScheduleById(id: String): WeeklySchedule? {
        return getAllSchedules().find { it.id == id }
    }
    
    fun saveSchedule(schedule: WeeklySchedule) {
        val currentSchedules = getAllSchedules().toMutableList()
        val existingIndex = currentSchedules.indexOfFirst { it.id == schedule.id }
        
        if (existingIndex >= 0) {
            currentSchedules[existingIndex] = schedule
        } else {
            currentSchedules.add(schedule)
        }
        
        saveSchedules(currentSchedules)
    }
    
    fun deleteSchedule(scheduleId: String) {
        val currentSchedules = getAllSchedules().toMutableList()
        currentSchedules.removeAll { it.id == scheduleId }
        saveSchedules(currentSchedules)
        
        // If deleted schedule was active, set first available as active
        if (prefs.getString(KEY_ACTIVE_SCHEDULE, null) == scheduleId) {
            val firstSchedule = currentSchedules.firstOrNull()
            if (firstSchedule != null) {
                setActiveSchedule(firstSchedule.id)
            } else {
                // Create a new default schedule if none left
                initializeDefaultSchedule()
            }
        }
    }
    
    fun setActiveSchedule(scheduleId: String) {
        prefs.edit().putString(KEY_ACTIVE_SCHEDULE, scheduleId).apply()
    }
    
    fun updateScheduleEntry(scheduleId: String, entry: WeeklyScheduleEntry) {
        val schedule = getScheduleById(scheduleId) ?: return
        val updatedSchedule = schedule.updateEntry(entry)
        saveSchedule(updatedSchedule)
    }
    
    fun createScheduleFromTemplate(name: String, template: WorkShiftTemplate, workingDays: List<DayOfWeek>): WeeklySchedule {
        val schedule = WeeklySchedule.createFromTemplate(template, workingDays).copy(name = name)
        saveSchedule(schedule)
        return schedule
    }
    
    fun duplicateSchedule(scheduleId: String, newName: String): WeeklySchedule? {
        val originalSchedule = getScheduleById(scheduleId) ?: return null
        val duplicatedSchedule = originalSchedule.copy(
            id = java.util.UUID.randomUUID().toString(),
            name = newName,
            createdAt = System.currentTimeMillis()
        )
        saveSchedule(duplicatedSchedule)
        return duplicatedSchedule
    }
    
    private fun saveSchedules(schedules: List<WeeklySchedule>) {
        val dtos = schedules.map { WeeklyScheduleDto.fromWeeklySchedule(it) }
        val json = gson.toJson(dtos)
        prefs.edit().putString(KEY_SCHEDULES, json).apply()
    }
    
    // DTO for JSON serialization
    private data class WeeklyScheduleDto(
        val id: String,
        val name: String,
        val entries: List<WeeklyScheduleEntryDto>,
        val isActive: Boolean,
        val createdAt: Long
    ) {
        fun toWeeklySchedule(): WeeklySchedule {
            return WeeklySchedule(
                id = id,
                name = name,
                entries = entries.map { it.toWeeklyScheduleEntry() },
                isActive = isActive,
                createdAt = createdAt
            )
        }
        
        companion object {
            fun fromWeeklySchedule(schedule: WeeklySchedule): WeeklyScheduleDto {
                return WeeklyScheduleDto(
                    id = schedule.id,
                    name = schedule.name,
                    entries = schedule.entries.map { WeeklyScheduleEntryDto.fromWeeklyScheduleEntry(it) },
                    isActive = schedule.isActive,
                    createdAt = schedule.createdAt
                )
            }
        }
    }
    
    private data class WeeklyScheduleEntryDto(
        val id: String,
        val dayOfWeek: String,
        val startHour: Int?,
        val startMinute: Int?,
        val endHour: Int?,
        val endMinute: Int?,
        val breakMinutes: Int,
        val isEnabled: Boolean,
        val templateId: String?
    ) {
        fun toWeeklyScheduleEntry(): WeeklyScheduleEntry {
            return WeeklyScheduleEntry(
                id = id,
                dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
                startTime = if (startHour != null && startMinute != null) LocalTime.of(startHour, startMinute) else null,
                endTime = if (endHour != null && endMinute != null) LocalTime.of(endHour, endMinute) else null,
                breakMinutes = breakMinutes,
                isEnabled = isEnabled,
                template = null // Template reference is not persisted, could be added later if needed
            )
        }
        
        companion object {
            fun fromWeeklyScheduleEntry(entry: WeeklyScheduleEntry): WeeklyScheduleEntryDto {
                return WeeklyScheduleEntryDto(
                    id = entry.id,
                    dayOfWeek = entry.dayOfWeek.name,
                    startHour = entry.startTime?.hour,
                    startMinute = entry.startTime?.minute,
                    endHour = entry.endTime?.hour,
                    endMinute = entry.endTime?.minute,
                    breakMinutes = entry.breakMinutes,
                    isEnabled = entry.isEnabled,
                    templateId = entry.template?.id
                )
            }
        }
    }
}