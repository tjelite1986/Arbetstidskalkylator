package se.thomas.arbetstidskalkylator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.thomas.arbetstidskalkylator.data.Settings
import se.thomas.arbetstidskalkylator.data.TimeDatabase
import se.thomas.arbetstidskalkylator.data.OvertimeRate
import se.thomas.arbetstidskalkylator.data.DayType
import se.thomas.arbetstidskalkylator.repository.SettingsRepository
import android.util.Log

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: SettingsRepository
    val settings: LiveData<Settings?>
    
    init {
        val settingsDao = TimeDatabase.getDatabase(application).settingsDao()
        repository = SettingsRepository(settingsDao)
        settings = repository.getSettings().asLiveData()
    }
    
    fun saveSettings(settings: Settings) = viewModelScope.launch {
        repository.saveSettings(settings)
    }
    
    suspend fun getSettingsSync(): Settings? {
        return repository.getSettingsSync()
    }
    
    fun initializeDefaultSettingsIfNeeded() = viewModelScope.launch {
        val existingSettings = repository.getSettingsSync()
        Log.d("SettingsViewModel", "initializeDefaultSettingsIfNeeded: existingSettings=$existingSettings")
        
        if (existingSettings == null) {
            Log.d("SettingsViewModel", "No settings found, creating default settings with OB rates")
            val defaultSettings = Settings(
                id = 1,
                baseHourlyRate = 0.0,
                taxPercentage = 30.0,
                vacationPayPercentage = 12.0,
                overtimeRates = createDefaultOvertimeRates()
            )
            repository.saveSettings(defaultSettings)
            Log.d("SettingsViewModel", "Default settings saved with ${defaultSettings.overtimeRates.size} OB rates")
        } else {
            // Aktivera OB-satser även för befintliga användare om de är tomma
            Log.d("SettingsViewModel", "Existing settings found with ${existingSettings.overtimeRates.size} OB rates")
            if (existingSettings.overtimeRates.isEmpty()) {
                Log.d("SettingsViewModel", "OB rates empty, adding default OB rates")
                val updatedSettings = existingSettings.copy(
                    overtimeRates = createDefaultOvertimeRates()
                )
                repository.saveSettings(updatedSettings)
                Log.d("SettingsViewModel", "Updated settings saved with ${updatedSettings.overtimeRates.size} OB rates")
            }
        }
    }
    
    fun setupStandardOvertimeRates() = viewModelScope.launch {
        val currentSettings = repository.getSettingsSync() ?: Settings(id = 1)
        val updatedSettings = currentSettings.copy(
            overtimeRates = createDefaultOvertimeRates()
        )
        repository.saveSettings(updatedSettings)
    }
    
    private fun createDefaultOvertimeRates(): List<OvertimeRate> {
        return listOf(
            // OB 1: 50% Måndag-fredag 18:15-20:00
            OvertimeRate(
                startHour = 18,
                startMinute = 15,
                endHour = 20,
                endMinute = 0,
                multiplier = 1.5, // 50% = 1.5x grundlön
                name = "OB 1",
                dayTypes = listOf(DayType.WEEKDAY),
                priority = 1
            ),
            
            // OB 2: 70% Måndag-fredag efter 20:00
            OvertimeRate(
                startHour = 20,
                startMinute = 0,
                endHour = null, // Till slutet av dagen
                multiplier = 1.7, // 70% = 1.7x grundlön
                name = "OB 2",
                dayTypes = listOf(DayType.WEEKDAY),
                priority = 2
            ),
            
            // OB 3: 100% Lördagar efter 12:00
            OvertimeRate(
                startHour = 12,
                startMinute = 0,
                endHour = null, // Till slutet av dagen
                multiplier = 2.0, // 100% = 2x grundlön
                name = "OB 3 Lördag",
                dayTypes = listOf(DayType.SATURDAY),
                priority = 3
            ),
            
            // OB 3: 100% Hela söndagar
            OvertimeRate(
                startHour = 0,
                startMinute = 0,
                endHour = null, // Hela dagen
                multiplier = 2.0, // 100% = 2x grundlön
                name = "OB 3 Söndag",
                dayTypes = listOf(DayType.SUNDAY),
                priority = 3
            )
        )
    }
}