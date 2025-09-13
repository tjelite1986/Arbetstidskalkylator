package se.thomas.arbetstidskalkylator

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.databinding.ActivityEditTimeEntryBinding
import se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel
import se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditTimeEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTimeEntryBinding
    private lateinit var viewModel: TimeEntryViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private var timeEntry: TimeEntry? = null
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    private var startTime: Date? = null
    private var endTime: Date? = null
    private var breakStartTime: Date? = null
    private var breakEndTime: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTimeEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TimeEntryViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        setupUI()
        loadTimeEntry()
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val isNewEntry = intent.getBooleanExtra("IS_NEW_ENTRY", false)
        title = if (isNewEntry) "Lägg till arbetstid" else "Redigera arbetstid"
        
        binding.tvHeader.text = if (isNewEntry) "➕ Lägg till arbetstid" else "✏️ Redigera arbetstid"
        
        setupNavigationButtons()

        binding.btnSelectDate.setOnClickListener { showDatePicker() }
        binding.btnStartTime.setOnClickListener { showStartTimePicker() }
        binding.btnEndTime.setOnClickListener { showEndTimePicker() }
        binding.btnBreakStartTime.setOnClickListener { showBreakStartTimePicker() }
        binding.btnBreakEndTime.setOnClickListener { showBreakEndTimePicker() }
        
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { saveTimeEntry() }
    }

    private fun loadTimeEntry() {
        val isNewEntry = intent.getBooleanExtra("IS_NEW_ENTRY", false)
        
        if (isNewEntry) {
            // Ny post - sätt upp standardvärden
            setupNewEntry()
        } else {
            // Befintlig post - ladda från databas
            val timeEntryId = intent.getLongExtra("TIME_ENTRY_ID", -1L)
            if (timeEntryId == -1L) {
                Toast.makeText(this, "Fel: Kunde inte ladda arbetstidspost", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            viewModel.allTimeEntries.observe(this) { timeEntries ->
                timeEntry = timeEntries.find { it.id == timeEntryId }
                timeEntry?.let { populateFields(it) }
            }
        }
    }
    
    private fun setupNewEntry() {
        // Sätt valt datum från intent
        val selectedDateString = intent.getStringExtra("SELECTED_DATE")
        if (selectedDateString != null) {
            try {
                calendar.time = dateFormat.parse(selectedDateString)!!
                binding.btnSelectDate.text = dateFormat.format(calendar.time)
            } catch (e: Exception) {
                calendar.time = Date() // Fallback till dagens datum
                binding.btnSelectDate.text = dateFormat.format(calendar.time)
            }
        }
        
        // Sätt standardtider
        val startCal = Calendar.getInstance().apply {
            time = calendar.time
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        startTime = startCal.time
        binding.btnStartTime.text = timeFormat.format(startTime!!)
        
        val endCal = Calendar.getInstance().apply {
            time = calendar.time
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        endTime = endCal.time
        binding.btnEndTime.text = timeFormat.format(endTime!!)
        
        // Sätt standard rasttider
        val breakStartCal = Calendar.getInstance().apply {
            time = calendar.time
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        breakStartTime = breakStartCal.time
        binding.btnBreakStartTime.text = timeFormat.format(breakStartTime!!)
        
        val breakEndCal = Calendar.getInstance().apply {
            time = calendar.time
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        breakEndTime = breakEndCal.time
        binding.btnBreakEndTime.text = timeFormat.format(breakEndTime!!)
        
        // Sätt standardrast
        binding.etBreakMinutes.setText("30")
        
        // Töm beskrivning
        binding.etDescription.setText("")
    }

    private fun populateFields(entry: TimeEntry) {
        // Datum
        calendar.time = entry.startTime
        binding.btnSelectDate.text = dateFormat.format(entry.startTime)
        
        // Starttid
        startTime = entry.startTime
        binding.btnStartTime.text = timeFormat.format(entry.startTime)
        
        // Sluttid
        endTime = entry.endTime
        binding.btnEndTime.text = entry.endTime?.let { timeFormat.format(it) } ?: "Ej satt"
        
        // Rast
        binding.etBreakMinutes.setText(entry.breakMinutes.toString())
        
        // Anpassade rasttider
        breakStartTime = entry.customBreakStart
        breakEndTime = entry.customBreakEnd
        
        binding.btnBreakStartTime.text = entry.customBreakStart?.let { 
            timeFormat.format(it) 
        } ?: "12:00"
        
        binding.btnBreakEndTime.text = entry.customBreakEnd?.let { 
            timeFormat.format(it) 
        } ?: "12:30"
        
        // Beskrivning
        binding.etDescription.setText(entry.description ?: "")
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                binding.btnSelectDate.text = dateFormat.format(calendar.time)
                updateStartEndTimes()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showStartTimePicker() {
        val currentStart = startTime ?: calendar.time
        val startCalendar = Calendar.getInstance().apply { time = currentStart }
        
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val newStartCalendar = Calendar.getInstance().apply {
                    time = calendar.time
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                startTime = newStartCalendar.time
                binding.btnStartTime.text = timeFormat.format(startTime!!)
            },
            startCalendar.get(Calendar.HOUR_OF_DAY),
            startCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showEndTimePicker() {
        val currentEnd = endTime ?: Calendar.getInstance().apply {
            time = startTime ?: calendar.time
            add(Calendar.HOUR_OF_DAY, 8)
        }.time
        val endCalendar = Calendar.getInstance().apply { time = currentEnd }
        
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val newEndCalendar = Calendar.getInstance().apply {
                    time = calendar.time
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                endTime = newEndCalendar.time
                binding.btnEndTime.text = timeFormat.format(endTime!!)
            },
            endCalendar.get(Calendar.HOUR_OF_DAY),
            endCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateStartEndTimes() {
        // Uppdatera start- och sluttider med nytt datum
        startTime?.let {
            val startCal = Calendar.getInstance().apply {
                time = it
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            }
            startTime = startCal.time
            binding.btnStartTime.text = timeFormat.format(startTime!!)
        }
        
        endTime?.let {
            val endCal = Calendar.getInstance().apply {
                time = it
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            }
            endTime = endCal.time
            binding.btnEndTime.text = timeFormat.format(endTime!!)
        }
        
        // Uppdatera rasttider med nytt datum
        breakStartTime?.let {
            val breakStartCal = Calendar.getInstance().apply {
                time = it
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            }
            breakStartTime = breakStartCal.time
            binding.btnBreakStartTime.text = timeFormat.format(breakStartTime!!)
        }
        
        breakEndTime?.let {
            val breakEndCal = Calendar.getInstance().apply {
                time = it
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            }
            breakEndTime = breakEndCal.time
            binding.btnBreakEndTime.text = timeFormat.format(breakEndTime!!)
        }
    }

    private fun showBreakStartTimePicker() {
        val currentBreakStart = breakStartTime ?: Calendar.getInstance().apply {
            time = calendar.time
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
        }.time
        val breakStartCalendar = Calendar.getInstance().apply { time = currentBreakStart }
        
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val newBreakStartCalendar = Calendar.getInstance().apply {
                    time = calendar.time
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                breakStartTime = newBreakStartCalendar.time
                binding.btnBreakStartTime.text = timeFormat.format(breakStartTime!!)
            },
            breakStartCalendar.get(Calendar.HOUR_OF_DAY),
            breakStartCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showBreakEndTimePicker() {
        val currentBreakEnd = breakEndTime ?: Calendar.getInstance().apply {
            time = breakStartTime ?: calendar.time
            add(Calendar.MINUTE, 30)
        }.time
        val breakEndCalendar = Calendar.getInstance().apply { time = currentBreakEnd }
        
        TimePickerDialog(
            this,
            { _, hour, minute ->
                val newBreakEndCalendar = Calendar.getInstance().apply {
                    time = calendar.time
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                breakEndTime = newBreakEndCalendar.time
                binding.btnBreakEndTime.text = timeFormat.format(breakEndTime!!)
            },
            breakEndCalendar.get(Calendar.HOUR_OF_DAY),
            breakEndCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveTimeEntry() {
        val isNewEntry = intent.getBooleanExtra("IS_NEW_ENTRY", false)
        
        // Validera fält
        if (startTime == null) {
            Toast.makeText(this, "Starttid måste anges", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (endTime == null) {
            Toast.makeText(this, "Sluttid måste anges", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (endTime!! <= startTime!!) {
            Toast.makeText(this, "Sluttid måste vara efter starttid", Toast.LENGTH_SHORT).show()
            return
        }

        // Hämta värden
        val breakMinutes = try {
            binding.etBreakMinutes.text.toString().toIntOrNull() ?: 0
        } catch (e: Exception) {
            0
        }

        val currentSettings = settingsViewModel.settings.value
        val hourlyRate = currentSettings?.baseHourlyRate ?: 162.98

        val description = binding.etDescription.text.toString().trim()

        if (isNewEntry) {
            // Skapa ny TimeEntry
            val newEntry = TimeEntry(
                startTime = startTime!!,
                endTime = endTime,
                breakMinutes = breakMinutes,
                hourlyRate = hourlyRate,
                description = if (description.isEmpty()) null else description,
                customBreakStart = breakStartTime,
                customBreakEnd = breakEndTime
            )

            // Spara ny
            viewModel.insertTimeEntry(newEntry)
            Toast.makeText(this, "Arbetstid sparad", Toast.LENGTH_SHORT).show()
        } else {
            // Uppdatera befintlig
            val originalEntry = timeEntry ?: return
            val updatedEntry = originalEntry.copy(
                startTime = startTime!!,
                endTime = endTime,
                breakMinutes = breakMinutes,
                hourlyRate = hourlyRate,
                description = if (description.isEmpty()) null else description,
                customBreakStart = breakStartTime,
                customBreakEnd = breakEndTime
            )

            // Spara uppdatering
            viewModel.updateTimeEntry(updatedEntry)
            Toast.makeText(this, "Arbetstid uppdaterad", Toast.LENGTH_SHORT).show()
        }
        
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun setupNavigationButtons() {
        binding.btnMain.setOnClickListener {
            finish() // Går tillbaka till MainActivity
        }
        
        binding.btnStatistics.setOnClickListener {
            startActivity(android.content.Intent(this, StatisticsActivity::class.java))
        }
        
        binding.btnHistory.setOnClickListener {
            startActivity(android.content.Intent(this, HistoryActivity::class.java))
        }
        
        binding.btnSettings.setOnClickListener {
            startActivity(android.content.Intent(this, SettingsActivity::class.java))
        }
    }
}