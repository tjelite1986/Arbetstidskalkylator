package com.example.timereportcalculator.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.timereportcalculator.data.*
import com.example.timereportcalculator.calculator.PayCalculator
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class WorkSessionManager private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: WorkSessionManager? = null
        
        fun getInstance(): WorkSessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WorkSessionManager().also { INSTANCE = it }
            }
        }
    }
    
    private val _currentSession = MutableStateFlow<ActiveWorkSession?>(null)
    val currentSession: StateFlow<ActiveWorkSession?> = _currentSession.asStateFlow()
    
    private val _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()
    
    private val payCalculator = PayCalculator()
    
    fun startWorkSession(
        date: LocalDate = LocalDate.now(),
        description: String = ""
    ): ActiveWorkSession {
        try {
            val session = ActiveWorkSession(
                date = date,
                startTime = LocalDateTime.now(),
                description = description
            )
            _currentSession.value = session
            _timerState.value = TimerState.RUNNING
            android.util.Log.d("WorkSessionManager", "Work session started successfully")
            return session
        } catch (e: Exception) {
            android.util.Log.e("WorkSessionManager", "Error starting work session", e)
            throw e
        }
    }
    
    fun stopWorkSession(): TimeEntry? {
        val session = _currentSession.value ?: return null
        
        // Convert active session to TimeEntry for permanent storage
        val timeEntry = convertSessionToTimeEntry(session)
        
        _currentSession.value = null
        _timerState.value = TimerState.STOPPED
        
        return timeEntry
    }
    
    fun startBreak(breakType: BreakType = BreakType.REGULAR) {
        val session = _currentSession.value ?: return
        
        if (!session.isOnBreak) {
            _currentSession.value = session.copy(
                isOnBreak = true,
                breakStartTime = LocalDateTime.now()
            )
            _timerState.value = TimerState.ON_BREAK
        }
    }
    
    fun endBreak() {
        val session = _currentSession.value ?: return
        
        if (session.isOnBreak && session.breakStartTime != null) {
            val breakEnd = LocalDateTime.now()
            val breakPeriod = BreakPeriod(
                startTime = session.breakStartTime,
                endTime = breakEnd
            )
            
            _currentSession.value = session.copy(
                isOnBreak = false,
                breakStartTime = null,
                breakMinutesSoFar = session.getTotalBreakMinutes(),
                breakHistory = session.breakHistory + breakPeriod
            )
            _timerState.value = TimerState.RUNNING
        }
    }
    
    fun pauseSession() {
        _timerState.value = TimerState.PAUSED
    }
    
    fun resumeSession() {
        _timerState.value = if (_currentSession.value?.isOnBreak == true) {
            TimerState.ON_BREAK
        } else {
            TimerState.RUNNING
        }
    }
    
    fun updateSessionEarnings(settings: Settings?) {
        val session = _currentSession.value ?: return
        if (settings == null) {
            // Just update time without earnings if settings unavailable
            _currentSession.value = session.copy(currentTime = LocalDateTime.now())
            return
        }
        
        try {
            // Create a temporary TimeEntry for calculation
            val tempEntry = TimeEntry(
                date = session.date,
                startTime = session.startTime.toLocalTime(),
                endTime = LocalDateTime.now().toLocalTime(),
                breakMinutes = session.getTotalBreakMinutes(),
                workHours = session.getWorkHours()
            )
            
            // Calculate current earnings
            val calculatedEntry = payCalculator.calculatePay(tempEntry, settings)
            
            // Update session with current earnings
            val earnings = SessionEarnings(
                basePay = calculatedEntry.basePay ?: 0.0,
                obPay = calculatedEntry.obPay ?: 0.0,
                grossPay = calculatedEntry.grossPay ?: 0.0,
                vacationPay = calculatedEntry.vacationPay ?: 0.0,
                totalPay = calculatedEntry.totalPay ?: 0.0,
                netPay = calculatedEntry.netPay ?: 0.0,
                obBreakdown = calculatedEntry.obBreakdown ?: emptyMap(),
                currentOBRate = getCurrentOBRate(session, settings),
                isCurrentlyEligibleForOB = isCurrentlyEligibleForOB(session, settings),
                nextOBChangeTime = getNextOBChangeTime(session, settings)
            )
            
            _currentSession.value = session.copy(
                currentTime = LocalDateTime.now(),
                currentEarnings = earnings
            )
        } catch (e: Exception) {
            // Handle calculation errors gracefully
            android.util.Log.e("WorkSessionManager", "Error updating session earnings", e)
            // Continue with basic session updates
            _currentSession.value = session.copy(
                currentTime = LocalDateTime.now()
            )
        }
    }
    
    private fun convertSessionToTimeEntry(session: ActiveWorkSession): TimeEntry {
        return TimeEntry(
            date = session.date,
            startTime = session.startTime.toLocalTime(),
            endTime = session.currentTime.toLocalTime(),
            breakMinutes = session.getTotalBreakMinutes(),
            workHours = session.getWorkHours()
        )
    }
    
    private fun getCurrentOBRate(session: ActiveWorkSession, settings: Settings): Double {
        val currentTime = session.currentTime.toLocalTime()
        val dayOfWeek = session.date.dayOfWeek
        
        // This is a simplified version - you might want to expand this based on PayCalculator logic
        return when (settings.obRates.workplaceType) {
            WorkplaceType.BUTIK -> {
                when {
                    dayOfWeek.value == 7 -> settings.obRates.butikSundayAllDay // Sunday
                    dayOfWeek.value == 6 && currentTime.isAfter(LocalTime.of(12, 0)) -> 
                        settings.obRates.butikSaturdayAfter1200 // Saturday after 12:00
                    dayOfWeek.value in 1..5 && currentTime.isAfter(LocalTime.of(20, 0)) -> 
                        settings.obRates.butikWeekdayAfter2000 // Weekday after 20:00
                    dayOfWeek.value in 1..5 && currentTime.isAfter(LocalTime.of(18, 15)) -> 
                        settings.obRates.butikWeekday1815to2000 // Weekday 18:15-20:00
                    else -> 0.0
                }
            }
            WorkplaceType.LAGER -> {
                when {
                    dayOfWeek.value == 7 -> settings.obRates.lagerSundayAllDay // Sunday
                    dayOfWeek.value == 6 -> {
                        when {
                            currentTime.isBefore(LocalTime.of(6, 0)) || currentTime.isAfter(LocalTime.of(23, 0)) -> 
                                settings.obRates.lagerSaturdayNight0000to0600
                            else -> settings.obRates.lagerSaturdayDay0600to2300
                        }
                    }
                    dayOfWeek.value in 1..5 -> {
                        when {
                            currentTime.isAfter(LocalTime.of(23, 0)) || currentTime.isBefore(LocalTime.of(6, 0)) -> 
                                settings.obRates.lagerWeekdayNight2300to0600
                            currentTime.isAfter(LocalTime.of(18, 0)) -> 
                                settings.obRates.lagerWeekdayEvening1800to2300
                            currentTime.isBefore(LocalTime.of(7, 0)) -> 
                                settings.obRates.lagerWeekdayMorning0600to0700
                            else -> 0.0
                        }
                    }
                    else -> 0.0
                }
            }
        }
    }
    
    private fun isCurrentlyEligibleForOB(session: ActiveWorkSession, settings: Settings): Boolean {
        return getCurrentOBRate(session, settings) > 0.0
    }
    
    private fun getNextOBChangeTime(session: ActiveWorkSession, settings: Settings): LocalTime? {
        // This would calculate when the next OB rate change occurs
        // Implementation depends on specific OB rules
        return null // Simplified for now
    }
}