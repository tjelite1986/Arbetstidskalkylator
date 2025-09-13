package se.thomas.arbetstidskalkylator

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.databinding.ActivityMainBinding
import se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel
import se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TimeEntryViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private var startDateTime: Calendar = Calendar.getInstance()
    private var endDateTime: Calendar = Calendar.getInstance()
    private var breakStartDateTime: Calendar? = null
    private var breakEndDateTime: Calendar? = null
    private var isSettingCustomBreak = false // Flagga för att förhindra clearing av anpassad rast
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[TimeEntryViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        
        // Initialize default settings if needed
        settingsViewModel.initializeDefaultSettingsIfNeeded()
        
        // Initialisera arbetstider korrekt
        initializeDateTimes()
        
        setupUI()
        observeTimeEntries()
        observeSettings()
    }
    
    private fun initializeDateTimes() {
        // Säkerställ att start- och sluttider är korrekt initialiserade
        startDateTime = Calendar.getInstance()
        endDateTime = Calendar.getInstance().apply { 
            add(Calendar.HOUR_OF_DAY, 8) // Lägg till 8 timmar som standard
        }
    }
    
    private fun setupUI() {
        binding.btnStartTime.setOnClickListener { showDateTimePicker(true) }
        binding.btnEndTime.setOnClickListener { showDateTimePicker(false) }
        binding.btnSaveEntry.setOnClickListener { saveTimeEntry() }
        binding.btnStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.btnSettings.setOnClickListener { 
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
        
        // Rast knappar
        binding.btnBreakStart.setOnClickListener { showBreakTimePicker(true) }
        binding.btnBreakEnd.setOnClickListener { showBreakTimePicker(false) }
        
        // Rast radio buttons - rensa anpassad rast när förbestämd rast väljs
        binding.rgBreakOptions.setOnCheckedChangeListener { _, _ ->
            if (!isSettingCustomBreak) {
                clearCustomBreak()
            }
        }
        
        updateTimeButtons()
        updateBreakButtons()
    }
    
    private fun showDateTimePicker(isStartTime: Boolean) {
        val calendar = if (isStartTime) startDateTime else endDateTime
        
        DatePickerDialog(this, { _, year, month, day ->
            calendar.set(year, month, day)
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeButtons()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
    
    private fun updateTimeButtons() {
        binding.btnStartTime.text = "Starttid: ${dateTimeFormat.format(startDateTime.time)}"
        binding.btnEndTime.text = "Sluttid: ${dateTimeFormat.format(endDateTime.time)}"
    }
    
    private fun showBreakTimePicker(isBreakStart: Boolean) {
        // Sätt flagga för att förhindra clearing av anpassad rast
        isSettingCustomBreak = true
        
        // Rensa förbestämda raster när anpassad rast används
        binding.rgBreakOptions.clearCheck()
        
        // Återställ flagga
        isSettingCustomBreak = false
        
        val currentBreak = if (isBreakStart) breakStartDateTime else breakEndDateTime
        
        // Använd befintlig tid om den finns, annars använd arbetstarttid som bas
        val initialTime = currentBreak ?: startDateTime
        
        TimePickerDialog(this, { _, hour, minute ->
            if (isBreakStart) {
                breakStartDateTime = Calendar.getInstance().apply {
                    time = startDateTime.time // Använd samma datum som arbetstarttid
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
            } else {
                breakEndDateTime = Calendar.getInstance().apply {
                    time = startDateTime.time // Använd samma datum som arbetstarttid
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
            }
            updateBreakButtons()
        }, initialTime.get(Calendar.HOUR_OF_DAY), initialTime.get(Calendar.MINUTE), true).show()
    }
    
    private fun updateBreakButtons() {
        binding.btnBreakStart.text = if (breakStartDateTime != null) {
            "Rast start: ${timeFormat.format(breakStartDateTime!!.time)}"
        } else {
            "Rast start: --:--"
        }
        
        binding.btnBreakEnd.text = if (breakEndDateTime != null) {
            "Rast slut: ${timeFormat.format(breakEndDateTime!!.time)}"
        } else {
            "Rast slut: --:--"
        }
    }
    
    private fun clearCustomBreak() {
        breakStartDateTime = null
        breakEndDateTime = null
        updateBreakButtons()
    }
    
    private fun saveTimeEntry() {
        if (startDateTime.timeInMillis >= endDateTime.timeInMillis) {
            Toast.makeText(this, "Sluttid måste vara efter starttid", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Använd grundlön från inställningar
        val currentSettings = settingsViewModel.settings.value
        val hourlyRate = currentSettings?.baseHourlyRate ?: 0.0
        
        if (hourlyRate <= 0) {
            Toast.makeText(this, "Konfigurera grundlön i inställningar först", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Hantera rastinformation
        val selectedBreakMinutes = when {
            binding.rbBreak0.isChecked -> 0
            binding.rbBreak15.isChecked -> 15
            binding.rbBreak30.isChecked -> 30
            binding.rbBreak45.isChecked -> 45
            binding.rbBreak60.isChecked -> 60
            else -> 0
        }
        
        // Validera anpassad rast om den används
        var customBreakStart: Date? = null
        var customBreakEnd: Date? = null
        
        if (breakStartDateTime != null && breakEndDateTime != null) {
            if (breakStartDateTime!!.timeInMillis >= breakEndDateTime!!.timeInMillis) {
                Toast.makeText(this, "Rast sluttid måste vara efter starttid", Toast.LENGTH_SHORT).show()
                return
            }
            customBreakStart = breakStartDateTime!!.time
            customBreakEnd = breakEndDateTime!!.time
        } else if (breakStartDateTime != null || breakEndDateTime != null) {
            Toast.makeText(this, "Ange både start- och sluttid för anpassad rast", Toast.LENGTH_SHORT).show()
            return
        }
        
        val timeEntry = TimeEntry(
            startTime = startDateTime.time,
            endTime = endDateTime.time,
            hourlyRate = hourlyRate,
            description = binding.etDescription.text.toString(),
            breakMinutes = selectedBreakMinutes,
            customBreakStart = customBreakStart,
            customBreakEnd = customBreakEnd
        )
        
        viewModel.insertTimeEntry(timeEntry)
        Toast.makeText(this, "Arbetstid sparad!", Toast.LENGTH_SHORT).show()
        clearForm()
    }
    
    private fun clearForm() {
        binding.etDescription.text?.clear()
        startDateTime = Calendar.getInstance()
        endDateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 8) }
        
        // Återställ rastinformation
        binding.rbBreak0.isChecked = true
        clearCustomBreak()
        
        updateTimeButtons()
    }
    
    private fun observeTimeEntries() {
        viewModel.allTimeEntries.observe(this) { timeEntries ->
            updateSummary(timeEntries)
        }
    }
    
    private fun observeSettings() {
        settingsViewModel.settings.observe(this) { _ ->
            // Uppdatera summering när inställningar ändras
            viewModel.allTimeEntries.value?.let { timeEntries ->
                updateSummary(timeEntries)
            }
        }
    }
    
    private fun updateSummary(timeEntries: List<TimeEntry>) {
        if (timeEntries.isNotEmpty()) {
            val currentSettings = settingsViewModel.settings.value
            val totalHours = viewModel.calculateTotalHours(timeEntries)
            
            // Beräkna lön med OB-satser om inställningar finns
            val overtimeRates = currentSettings?.overtimeRates ?: emptyList()
            val grossPay = viewModel.calculateTotalPay(timeEntries, overtimeRates)
            
            if (currentSettings != null) {
                // Beräkna skatt och semesterersättning separat
                val vacationPay = grossPay * (currentSettings.vacationPayPercentage / 100)
                val tax = grossPay * (currentSettings.taxPercentage / 100)
                val netPay = grossPay - tax
                
                binding.tvTotalHours.text = "Totala timmar: %.2f".format(totalHours)
                binding.tvTotalPay.text = "Netto: %.2f kr | Semester: %.2f kr".format(netPay, vacationPay)
            } else {
                binding.tvTotalHours.text = "Totala timmar: %.2f".format(totalHours)
                binding.tvTotalPay.text = "Total lön: %.2f kr".format(grossPay)
            }
        } else {
            binding.tvTotalHours.text = "Totala timmar: 0.00"
            binding.tvTotalPay.text = "Total lön: 0.00 kr"
        }
    }
}