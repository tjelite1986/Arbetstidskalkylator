package se.thomas.arbetstidskalkylator.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromOvertimeRateList(value: List<OvertimeRate>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toOvertimeRateList(value: String): List<OvertimeRate> {
        val listType = object : TypeToken<List<OvertimeRate>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }
}