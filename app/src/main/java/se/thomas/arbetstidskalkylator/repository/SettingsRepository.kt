package se.thomas.arbetstidskalkylator.repository

import kotlinx.coroutines.flow.Flow
import se.thomas.arbetstidskalkylator.data.Settings
import se.thomas.arbetstidskalkylator.data.SettingsDao

class SettingsRepository(private val settingsDao: SettingsDao) {
    
    fun getSettings(): Flow<Settings?> = settingsDao.getSettings()
    
    suspend fun getSettingsSync(): Settings? = settingsDao.getSettingsSync()
    
    suspend fun saveSettings(settings: Settings) = settingsDao.insertSettings(settings)
    
    suspend fun updateSettings(settings: Settings) = settingsDao.updateSettings(settings)
    
    suspend fun clearSettings() = settingsDao.clearSettings()
}