package se.thomas.arbetstidskalkylator.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TimeEntryDao {
    @Query("SELECT * FROM time_entries ORDER BY startTime DESC")
    fun getAllTimeEntries(): Flow<List<TimeEntry>>
    
    @Query("SELECT * FROM time_entries WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime DESC")
    fun getTimeEntriesByDateRange(startDate: Date, endDate: Date): Flow<List<TimeEntry>>
    
    @Query("SELECT * FROM time_entries WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime DESC")
    suspend fun getTimeEntriesByDateRangeSync(startDate: Date, endDate: Date): List<TimeEntry>
    
    @Insert
    suspend fun insertTimeEntry(timeEntry: TimeEntry): Long
    
    @Update
    suspend fun updateTimeEntry(timeEntry: TimeEntry)
    
    @Delete
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)
    
    @Query("SELECT * FROM time_entries WHERE id = :id")
    suspend fun getTimeEntryById(id: Long): TimeEntry?
}