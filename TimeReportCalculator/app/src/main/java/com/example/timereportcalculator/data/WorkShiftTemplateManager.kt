package com.example.timereportcalculator.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalTime

class WorkShiftTemplateManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("work_shift_templates", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_TEMPLATES = "templates"
        private const val KEY_INITIALIZED = "initialized"
    }
    
    init {
        // Initialize with default templates if first time
        if (!prefs.getBoolean(KEY_INITIALIZED, false)) {
            initializeDefaultTemplates()
        }
    }
    
    private fun initializeDefaultTemplates() {
        val defaultTemplates = WorkShiftTemplate.getDefaultTemplates()
        saveTemplates(defaultTemplates)
        prefs.edit().putBoolean(KEY_INITIALIZED, true).apply()
    }
    
    fun getAllTemplates(): List<WorkShiftTemplate> {
        val json = prefs.getString(KEY_TEMPLATES, null) ?: return emptyList()
        try {
            val type = object : TypeToken<List<WorkShiftTemplateDto>>() {}.type
            val dtos: List<WorkShiftTemplateDto> = gson.fromJson(json, type)
            return dtos.map { it.toWorkShiftTemplate() }
        } catch (e: Exception) {
            return emptyList()
        }
    }
    
    fun saveTemplate(template: WorkShiftTemplate) {
        val currentTemplates = getAllTemplates().toMutableList()
        val existingIndex = currentTemplates.indexOfFirst { it.id == template.id }
        
        if (existingIndex >= 0) {
            currentTemplates[existingIndex] = template
        } else {
            currentTemplates.add(template)
        }
        
        saveTemplates(currentTemplates)
    }
    
    fun deleteTemplate(templateId: String) {
        val currentTemplates = getAllTemplates().toMutableList()
        currentTemplates.removeAll { it.id == templateId }
        saveTemplates(currentTemplates)
    }
    
    fun getFavoriteTemplates(): List<WorkShiftTemplate> {
        return getAllTemplates().filter { it.isFavorite }.take(4)
    }
    
    fun toggleFavorite(templateId: String) {
        val currentTemplates = getAllTemplates().toMutableList()
        val templateIndex = currentTemplates.indexOfFirst { it.id == templateId }
        
        if (templateIndex >= 0) {
            val template = currentTemplates[templateIndex]
            val favoriteCount = currentTemplates.count { it.isFavorite }
            
            // Allow toggling off or on if less than 4 favorites
            if (template.isFavorite || favoriteCount < 4) {
                currentTemplates[templateIndex] = template.copy(isFavorite = !template.isFavorite)
                saveTemplates(currentTemplates)
            }
        }
    }
    
    private fun saveTemplates(templates: List<WorkShiftTemplate>) {
        val dtos = templates.map { WorkShiftTemplateDto.fromWorkShiftTemplate(it) }
        val json = gson.toJson(dtos)
        prefs.edit().putString(KEY_TEMPLATES, json).apply()
    }
    
    // DTO for JSON serialization (LocalTime is not directly serializable)
    private data class WorkShiftTemplateDto(
        val id: String,
        val name: String,
        val startHour: Int,
        val startMinute: Int,
        val endHour: Int,
        val endMinute: Int,
        val breakMinutes: Int,
        val isFavorite: Boolean
    ) {
        fun toWorkShiftTemplate(): WorkShiftTemplate {
            return WorkShiftTemplate(
                id = id,
                name = name,
                startTime = LocalTime.of(startHour, startMinute),
                endTime = LocalTime.of(endHour, endMinute),
                breakMinutes = breakMinutes,
                isFavorite = isFavorite
            )
        }
        
        companion object {
            fun fromWorkShiftTemplate(template: WorkShiftTemplate): WorkShiftTemplateDto {
                return WorkShiftTemplateDto(
                    id = template.id,
                    name = template.name,
                    startHour = template.startTime.hour,
                    startMinute = template.startTime.minute,
                    endHour = template.endTime.hour,
                    endMinute = template.endTime.minute,
                    breakMinutes = template.breakMinutes,
                    isFavorite = template.isFavorite
                )
            }
        }
    }
}