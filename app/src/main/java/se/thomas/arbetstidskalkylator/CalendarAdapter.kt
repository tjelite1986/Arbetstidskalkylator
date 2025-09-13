package se.thomas.arbetstidskalkylator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.databinding.CalendarDayCellBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    private val context: Context,
    private var days: List<CalendarDay>,
    private val onDayClick: (CalendarDay) -> Unit,
    private val onDayLongClick: (CalendarDay) -> Unit,
    private val onEditTimeEntry: (TimeEntry) -> Unit
) : BaseAdapter() {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun updateDays(newDays: List<CalendarDay>) {
        days = newDays
        notifyDataSetChanged()
    }

    override fun getCount(): Int = days.size

    override fun getItem(position: Int): CalendarDay = days[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            CalendarDayCellBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            CalendarDayCellBinding.bind(convertView)
        }

        val day = days[position]
        
        // Set day number
        binding.tvDayNumber.text = if (day.dayOfMonth > 0) day.dayOfMonth.toString() else ""
        
        // Style current month vs other months
        if (day.isCurrentMonth) {
            binding.root.alpha = 1.0f
            binding.tvDayNumber.setTextColor(context.getColor(android.R.color.black))
        } else {
            binding.root.alpha = 0.3f
            binding.tvDayNumber.setTextColor(context.getColor(android.R.color.darker_gray))
        }

        // Style today
        if (day.isToday) {
            binding.root.setBackgroundColor(context.getColor(android.R.color.holo_blue_light))
        } else {
            binding.root.setBackgroundColor(context.getColor(android.R.color.white))
        }

        // Show work times if available
        if (day.timeEntries.isNotEmpty() && day.isCurrentMonth) {
            binding.tvWorkTime.visibility = View.VISIBLE
            binding.tvEarnings.visibility = View.VISIBLE

            when (day.timeEntries.size) {
                1 -> {
                    val entry = day.timeEntries.first()
                    val startTime = timeFormat.format(entry.startTime)
                    val endTime = if (entry.endTime != null) timeFormat.format(entry.endTime) else "?"
                    binding.tvWorkTime.text = "$startTime\n$endTime"
                }
                else -> {
                    binding.tvWorkTime.text = "${day.timeEntries.size}\npass"
                }
            }

            // Calculate total earnings for the day
            val totalEarnings = day.timeEntries.sumOf { it.getWorkedHours() * it.hourlyRate }
            binding.tvEarnings.text = "${totalEarnings.toInt()}kr"
        } else {
            binding.tvWorkTime.visibility = View.GONE
            binding.tvEarnings.visibility = View.GONE
        }

        // Set click listeners
        binding.root.setOnClickListener {
            if (day.isCurrentMonth) {
                if (day.timeEntries.isEmpty()) {
                    // No time entries - show info or do nothing
                    onDayClick(day)
                } else if (day.timeEntries.size == 1) {
                    // Single entry - edit it directly
                    onEditTimeEntry(day.timeEntries.first())
                } else {
                    // Multiple entries - show day info
                    onDayClick(day)
                }
            }
        }
        
        binding.root.setOnLongClickListener {
            if (day.isCurrentMonth) {
                onDayLongClick(day)
                true
            } else {
                false
            }
        }

        return binding.root
    }
}

data class CalendarDay(
    val dayOfMonth: Int,
    val date: Calendar,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val timeEntries: List<TimeEntry> = emptyList()
)