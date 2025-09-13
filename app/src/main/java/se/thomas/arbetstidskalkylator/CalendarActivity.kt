package se.thomas.arbetstidskalkylator

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.databinding.ActivityCalendarBinding
import se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel
import se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var viewModel: TimeEntryViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var calendarAdapter: CalendarAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("d MMMM yyyy", Locale("sv", "SE"))
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale("sv", "SE"))
    private var currentCalendar = Calendar.getInstance()
    private var selectedDay: CalendarDay? = null
    private var timeEntries: List<TimeEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TimeEntryViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        setupUI()
        observeTimeEntries()
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Kalendervy"

        setupNavigationButtons()
        setupCalendar()
        updateMonthYearDisplay()
    }

    private fun setupNavigationButtons() {
        binding.btnMain.setOnClickListener {
            finish()
        }

        binding.btnStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupCalendar() {
        calendarAdapter = CalendarAdapter(
            context = this,
            days = emptyList(),
            onDayClick = { day ->
                selectedDay = day
                updateDateInfo()
            },
            onDayLongClick = { day ->
                openEditTimeEntryForNewEntry(day)
            },
            onEditTimeEntry = { timeEntry ->
                editTimeEntry(timeEntry)
            }
        )
        binding.gridViewCalendar.adapter = calendarAdapter

        binding.btnPreviousMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            updateCalendarView()
            updateMonthYearDisplay()
        }

        binding.btnNextMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            updateCalendarView()
            updateMonthYearDisplay()
        }

        updateCalendarView()
    }

    private fun observeTimeEntries() {
        viewModel.allTimeEntries.observe(this) { entries ->
            timeEntries = entries
            updateCalendarView()
            updateDateInfo()
        }
    }

    private fun updateCalendarView() {
        val days = generateCalendarDays()
        calendarAdapter.updateDays(days)
    }

    private fun generateCalendarDays(): List<CalendarDay> {
        val days = mutableListOf<CalendarDay>()
        val calendar = Calendar.getInstance()
        calendar.time = currentCalendar.time
        
        // Set to first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        // Add days from previous month to fill first week
        val prevMonth = Calendar.getInstance()
        prevMonth.time = calendar.time
        prevMonth.add(Calendar.MONTH, -1)
        val daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        // Monday = 1, so we need to adjust
        val startOffset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2
        for (i in startOffset downTo 1) {
            val dayNum = daysInPrevMonth - i + 1
            val dayCalendar = Calendar.getInstance()
            dayCalendar.time = prevMonth.time
            dayCalendar.set(Calendar.DAY_OF_MONTH, dayNum)
            
            days.add(CalendarDay(
                dayOfMonth = dayNum,
                date = dayCalendar,
                isCurrentMonth = false,
                isToday = false,
                timeEntries = getTimeEntriesForDate(dayCalendar)
            ))
        }
        
        // Add days of current month
        val today = Calendar.getInstance()
        for (day in 1..daysInMonth) {
            val dayCalendar = Calendar.getInstance()
            dayCalendar.time = calendar.time
            dayCalendar.set(Calendar.DAY_OF_MONTH, day)
            
            val isToday = dayCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                         dayCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
            
            days.add(CalendarDay(
                dayOfMonth = day,
                date = dayCalendar,
                isCurrentMonth = true,
                isToday = isToday,
                timeEntries = getTimeEntriesForDate(dayCalendar)
            ))
        }
        
        // Add days from next month to fill last week
        val nextMonth = Calendar.getInstance()
        nextMonth.time = calendar.time
        nextMonth.add(Calendar.MONTH, 1)
        
        val remainingDays = 42 - days.size // 6 weeks * 7 days
        for (day in 1..remainingDays) {
            val dayCalendar = Calendar.getInstance()
            dayCalendar.time = nextMonth.time
            dayCalendar.set(Calendar.DAY_OF_MONTH, day)
            
            days.add(CalendarDay(
                dayOfMonth = day,
                date = dayCalendar,
                isCurrentMonth = false,
                isToday = false,
                timeEntries = getTimeEntriesForDate(dayCalendar)
            ))
        }
        
        return days
    }

    private fun getTimeEntriesForDate(calendar: Calendar): List<TimeEntry> {
        val dateString = dateFormat.format(calendar.time)
        return timeEntries.filter { entry ->
            dateFormat.format(entry.startTime) == dateString
        }
    }

    private fun updateMonthYearDisplay() {
        binding.tvMonthYear.text = monthYearFormat.format(currentCalendar.time)
            .replaceFirstChar { it.uppercase() }
    }

    private fun updateDateInfo() {
        if (selectedDay == null) {
            binding.layoutDateInfo.visibility = android.view.View.GONE
            return
        }

        val day = selectedDay!!
        if (day.timeEntries.isEmpty()) {
            binding.layoutDateInfo.visibility = android.view.View.GONE
        } else {
            binding.layoutDateInfo.visibility = android.view.View.VISIBLE
            
            val displayDate = displayDateFormat.format(day.date.time)
                .replaceFirstChar { it.uppercase() }
            binding.tvSelectedDate.text = "Valt datum: $displayDate"

            // Calculate total hours and earnings for the day
            var totalHours = 0.0
            var totalEarnings = 0.0

            for (entry in day.timeEntries) {
                val hours = entry.getWorkedHours()
                val earnings = hours * entry.hourlyRate
                totalHours += hours
                totalEarnings += earnings
            }

            if (day.timeEntries.size == 1) {
                val entry = day.timeEntries.first()
                val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(entry.startTime)
                val endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(entry.endTime ?: entry.startTime)
                binding.tvWorkHours.text = "Arbetstid: $startTime - $endTime (${String.format("%.1f", totalHours)}h)"
            } else {
                binding.tvWorkHours.text = "Arbetstider: ${day.timeEntries.size} pass (${String.format("%.1f", totalHours)}h totalt)"
            }

            binding.tvEarnings.text = "Intjänat: ${String.format("%.2f", totalEarnings).replace(".", ",")} kr"
        }
    }

    private fun openEditTimeEntryForNewEntry(day: CalendarDay) {
        val intent = Intent(this, EditTimeEntryActivity::class.java)
        intent.putExtra("SELECTED_DATE", dateFormat.format(day.date.time))
        intent.putExtra("IS_NEW_ENTRY", true)
        startActivity(intent)
    }
    
    private fun editTimeEntry(timeEntry: TimeEntry) {
        val intent = Intent(this, EditTimeEntryActivity::class.java)
        intent.putExtra("TIME_ENTRY_ID", timeEntry.id)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}