package se.thomas.arbetstidskalkylator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity(tableName = "time_entries")
data class TimeEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Date,
    val endTime: Date?,
    val hourlyRate: Double,
    val description: String? = null,
    val isBreakDeducted: Boolean = true,
    // Nya rastfält
    val breakMinutes: Int = 0, // Förbestämd rast i minuter (0, 15, 30, 45, 60)
    val customBreakStart: Date? = null, // Anpassad rast starttid
    val customBreakEnd: Date? = null // Anpassad rast sluttid
) {
    fun getWorkedHours(): Double {
        if (endTime == null) return 0.0
        val diffInMillis = endTime.time - startTime.time
        val totalHours = diffInMillis / (1000.0 * 60 * 60)
        
        // Beräkna total rasttid
        val totalBreakMinutes = when {
            // Anpassad rast har prioritet
            customBreakStart != null && customBreakEnd != null -> {
                val customBreakMillis = customBreakEnd.time - customBreakStart.time
                (customBreakMillis / (1000 * 60)).toInt()
            }
            // Förbestämd rast
            breakMinutes > 0 -> breakMinutes
            // Automatisk lunchrast (bakåtkompatibilitet)
            isBreakDeducted && totalHours > 6 -> 30
            else -> 0
        }
        
        return totalHours - (totalBreakMinutes / 60.0)
    }
    
    fun calculatePay(overtimeRates: List<OvertimeRate> = emptyList()): Double {
        if (endTime == null) return 0.0
        
        val workedHours = getWorkedHours()
        var totalPay = 0.0
        
        // Om inga OB-satser är definierade, använd bara grundlönen
        if (overtimeRates.isEmpty()) {
            return workedHours * hourlyRate
        }
        
        // Beräkna timme för timme för att hantera OB-satser
        val workingMinutes = ((endTime.time - startTime.time) / (1000 * 60)).toInt()
        
        // Beräkna total rasttid i minuter
        val totalBreakMinutes = when {
            // Anpassad rast har prioritet
            customBreakStart != null && customBreakEnd != null -> {
                val customBreakMillis = customBreakEnd.time - customBreakStart.time
                (customBreakMillis / (1000 * 60)).toInt()
            }
            // Förbestämd rast
            breakMinutes > 0 -> breakMinutes
            // Automatisk lunchrast (bakåtkompatibilitet)
            isBreakDeducted && workingMinutes > 360 -> 30
            else -> 0
        }
        
        val actualWorkingMinutes = workingMinutes - totalBreakMinutes
        
        var processedMinutes = 0
        val currentTime = Calendar.getInstance().apply { time = startTime }
        
        while (processedMinutes < actualWorkingMinutes) {
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)
            val dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK)
            
            // Bestäm dagtyp
            val dayType = when (dayOfWeek) {
                Calendar.SATURDAY -> DayType.SATURDAY
                Calendar.SUNDAY -> DayType.SUNDAY
                else -> DayType.WEEKDAY // TODO: Lägg till helgdagshantering senare
            }
            
            // Hitta högsta OB-sats som gäller för denna tid och dag
            val applicableRate = overtimeRates
                .filter { it.isActiveAt(hour, minute, dayType) }
                .maxByOrNull { it.priority * 1000 + it.multiplier } // Prioritet först, sedan multiplikator
            
            val multiplier = applicableRate?.multiplier ?: 1.0
            totalPay += (hourlyRate * multiplier) / 60.0 // Per minut
            
            currentTime.add(Calendar.MINUTE, 1)
            processedMinutes++
        }
        
        return totalPay
    }
    
    // Bakåtkompatibilitet
    fun calculatePay(): Double {
        return calculatePay(emptyList())
    }
}