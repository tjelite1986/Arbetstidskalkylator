package se.thomas.arbetstidskalkylator.repository

import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.data.TimeEntryDao
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date

class TimeEntryRepository(private val timeEntryDao: TimeEntryDao) {
    
    fun getAllTimeEntries(): Flow<List<TimeEntry>> = timeEntryDao.getAllTimeEntries()
    
    fun getTimeEntriesByDateRange(startDate: Date, endDate: Date): Flow<List<TimeEntry>> = 
        timeEntryDao.getTimeEntriesByDateRange(startDate, endDate)
    
    suspend fun insertTimeEntry(timeEntry: TimeEntry): Long = timeEntryDao.insertTimeEntry(timeEntry)
    
    suspend fun updateTimeEntry(timeEntry: TimeEntry) = timeEntryDao.updateTimeEntry(timeEntry)
    
    suspend fun deleteTimeEntry(timeEntry: TimeEntry) = timeEntryDao.deleteTimeEntry(timeEntry)
    
    suspend fun getTimeEntryById(id: Long): TimeEntry? = timeEntryDao.getTimeEntryById(id)
    
    suspend fun getTimeEntriesByDateRangeSync(startDate: Date, endDate: Date): List<TimeEntry> = 
        timeEntryDao.getTimeEntriesByDateRangeSync(startDate, endDate)
    
    suspend fun getTimeEntriesForMonth(month: Int, year: Int): List<TimeEntry> {
        val calendar = Calendar.getInstance()
        
        // Första dagen i månaden
        calendar.set(year, month - 1, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        
        // Sista dagen i månaden
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.time
        
        return timeEntryDao.getTimeEntriesByDateRangeSync(startDate, endDate)
    }
}