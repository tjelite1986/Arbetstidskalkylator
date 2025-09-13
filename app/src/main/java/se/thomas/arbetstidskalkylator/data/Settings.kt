package se.thomas.arbetstidskalkylator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey
    val id: Long = 1, // Endast en inställningspost
    val baseHourlyRate: Double = 0.0,
    val taxPercentage: Double = 30.0, // Standard skatt 30%
    val vacationPayPercentage: Double = 12.0, // Standard semesterersättning 12%
    val overtimeRates: List<OvertimeRate> = emptyList()
)

enum class DayType {
    WEEKDAY, // Måndag-Fredag
    SATURDAY, // Lördag
    SUNDAY, // Söndag
    HOLIDAY // Helgdag
}

data class OvertimeRate(
    val startHour: Int, // Timme på dygnet (0-23)
    val startMinute: Int = 0, // Minut (0-59)
    val endHour: Int? = null, // Sluttimme (null = till slutet av dagen)
    val endMinute: Int = 0, // Slutminut
    val multiplier: Double, // Multiplikator för grundlönen (t.ex. 1.5 för 50% OB)
    val name: String = "OB", // Namn på OB-satsen
    val dayTypes: List<DayType> = listOf(DayType.WEEKDAY), // Vilka dagar som gäller
    val priority: Int = 0 // Högre nummer = högre prioritet när flera OB överlappar
) {
    fun getTimeString(): String {
        val endStr = if (endHour != null) {
            String.format("-%02d:%02d", endHour, endMinute)
        } else {
            ""
        }
        return String.format("%02d:%02d%s", startHour, startMinute, endStr)
    }
    
    fun getDayTypeString(): String {
        return when {
            dayTypes.contains(DayType.HOLIDAY) -> "Helgdagar"
            dayTypes.contains(DayType.SUNDAY) && dayTypes.contains(DayType.SATURDAY) -> "Helger"
            dayTypes.contains(DayType.SUNDAY) -> "Söndagar"
            dayTypes.contains(DayType.SATURDAY) -> "Lördagar"
            dayTypes.contains(DayType.WEEKDAY) -> "Måndag-Fredag"
            else -> "Okänd"
        }
    }
    
    fun isActiveAt(hour: Int, minute: Int, dayType: DayType): Boolean {
        // Kontrollera om denna OB gäller för den aktuella dagen
        if (!dayTypes.contains(dayType)) {
            return false
        }
        
        val currentMinutes = hour * 60 + minute
        val startMinutes = startHour * 60 + startMinute
        
        return if (endHour == null) {
            // Gäller från starttid till slutet av dagen
            currentMinutes >= startMinutes
        } else {
            val endMinutes = endHour * 60 + endMinute
            if (startMinutes <= endMinutes) {
                // Samma dag
                currentMinutes >= startMinutes && currentMinutes < endMinutes
            } else {
                // Över midnatt
                currentMinutes >= startMinutes || currentMinutes < endMinutes
            }
        }
    }
}