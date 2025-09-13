package se.thomas.arbetstidskalkylator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.thomas.arbetstidskalkylator.data.TimeDatabase
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.data.OvertimeRate
import se.thomas.arbetstidskalkylator.repository.TimeEntryRepository
import java.util.Date

class TimeEntryViewModel(application: Application) : AndroidViewModel(application) {
    
    val repository: TimeEntryRepository
    val allTimeEntries: LiveData<List<TimeEntry>>
    
    init {
        val timeEntryDao = TimeDatabase.getDatabase(application).timeEntryDao()
        repository = TimeEntryRepository(timeEntryDao)
        allTimeEntries = repository.getAllTimeEntries().asLiveData()
    }
    
    fun insertTimeEntry(timeEntry: TimeEntry) = viewModelScope.launch {
        repository.insertTimeEntry(timeEntry)
    }
    
    fun updateTimeEntry(timeEntry: TimeEntry) = viewModelScope.launch {
        repository.updateTimeEntry(timeEntry)
    }
    
    fun deleteTimeEntry(timeEntry: TimeEntry) = viewModelScope.launch {
        repository.deleteTimeEntry(timeEntry)
    }
    
    fun getTimeEntriesByDateRange(startDate: Date, endDate: Date): LiveData<List<TimeEntry>> {
        return repository.getTimeEntriesByDateRange(startDate, endDate).asLiveData()
    }
    
    fun calculateTotalPay(timeEntries: List<TimeEntry>, overtimeRates: List<OvertimeRate> = emptyList()): Double {
        return timeEntries.sumOf { it.calculatePay(overtimeRates) }
    }
    
    fun calculateTotalHours(timeEntries: List<TimeEntry>): Double {
        return timeEntries.sumOf { it.getWorkedHours() }
    }
}