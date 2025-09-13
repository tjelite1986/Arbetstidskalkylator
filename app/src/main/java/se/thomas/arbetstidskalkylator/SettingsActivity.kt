package se.thomas.arbetstidskalkylator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.thomas.arbetstidskalkylator.data.Settings
import se.thomas.arbetstidskalkylator.data.OvertimeRate
import se.thomas.arbetstidskalkylator.databinding.ActivitySettingsBinding
import se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivitySettingsBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
            
            setupUI()
            observeSettings()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Fel vid uppstart av inställningar: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupUI() {
        try {
            // Använd den befintliga ActionBar istället för toolbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Inställningar"
            
            // Setup navigation buttons
            setupNavigationButtons()
            
            // Set default values
            binding.etTaxPercentage.setText("30")
            binding.etVacationPayPercentage.setText("12")
            
            // Initialize default settings if needed
            viewModel.initializeDefaultSettingsIfNeeded()
            
            // Setup OB-satser switches och input handlers
            setupOBControls()
            
            // Knappar
            binding.btnSave.setOnClickListener {
                saveBasicSettings()
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Fel vid UI-setup: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun observeSettings() {
        try {
            viewModel.settings.observe(this) { settings ->
                settings?.let {
                    try {
                        // Only update if values are different from current ones
                        if (it.baseHourlyRate > 0 && binding.etBaseHourlyRate.text.toString().isEmpty()) {
                            binding.etBaseHourlyRate.setText("%.2f".format(it.baseHourlyRate))
                        }
                        if (binding.etTaxPercentage.text.toString() != "%.2f".format(it.taxPercentage)) {
                            binding.etTaxPercentage.setText("%.2f".format(it.taxPercentage))
                        }
                        if (binding.etVacationPayPercentage.text.toString() != "%.2f".format(it.vacationPayPercentage)) {
                            binding.etVacationPayPercentage.setText("%.2f".format(it.vacationPayPercentage))
                        }
                        
                        // Uppdatera OB-satser
                        updateOBControlsFromSettings(it.overtimeRates)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
    }
    
    private fun saveBasicSettings() {
        try {
            val baseHourlyRateText = binding.etBaseHourlyRate.text.toString().trim()
            val taxPercentageText = binding.etTaxPercentage.text.toString().trim()
            val vacationPayPercentageText = binding.etVacationPayPercentage.text.toString().trim()
            
            // Validate inputs
            val baseHourlyRate = if (baseHourlyRateText.isNotEmpty()) {
                val rate = baseHourlyRateText.toDoubleOrNull()
                if (rate == null || rate < 0) {
                    Toast.makeText(this, "Ange giltig grundlön (0 eller högre)", Toast.LENGTH_SHORT).show()
                    return
                }
                rate
            } else 0.0
            
            val taxPercentage = if (taxPercentageText.isNotEmpty()) {
                val tax = taxPercentageText.toDoubleOrNull()
                if (tax == null || tax < 0 || tax > 100) {
                    Toast.makeText(this, "Ange giltig skattesats (0-100%)", Toast.LENGTH_SHORT).show()
                    return
                }
                tax
            } else 30.0
            
            val vacationPayPercentage = if (vacationPayPercentageText.isNotEmpty()) {
                val vacation = vacationPayPercentageText.toDoubleOrNull()
                if (vacation == null || vacation < 0 || vacation > 100) {
                    Toast.makeText(this, "Ange giltig semesterersättning (0-100%)", Toast.LENGTH_SHORT).show()
                    return
                }
                vacation
            } else 12.0
            
            // Samla OB-satser från UI
            val overtimeRates = collectOvertimeRatesFromUI()
            
            val settings = Settings(
                id = 1, // Always use ID 1 for singleton settings
                baseHourlyRate = baseHourlyRate,
                taxPercentage = taxPercentage,
                vacationPayPercentage = vacationPayPercentage,
                overtimeRates = overtimeRates
            )
            
            viewModel.saveSettings(settings)
            Toast.makeText(this, "Inställningar sparade!", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Fel vid sparande: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupOBControls() {
        // Setup switch listeners för att aktivera/inaktivera textfält
        binding.switchOB50.setOnCheckedChangeListener { _, isChecked ->
            binding.etOB50Percentage.isEnabled = isChecked
            if (!isChecked) binding.etOB50Percentage.alpha = 0.5f
            else binding.etOB50Percentage.alpha = 1.0f
        }
        
        binding.switchOB70.setOnCheckedChangeListener { _, isChecked ->
            binding.etOB70Percentage.isEnabled = isChecked
            if (!isChecked) binding.etOB70Percentage.alpha = 0.5f
            else binding.etOB70Percentage.alpha = 1.0f
        }
        
        binding.switchOB100.setOnCheckedChangeListener { _, isChecked ->
            binding.etOB100Percentage.isEnabled = isChecked
            if (!isChecked) binding.etOB100Percentage.alpha = 0.5f
            else binding.etOB100Percentage.alpha = 1.0f
        }
        
        binding.switchCoopMorning.setOnCheckedChangeListener { _, isChecked ->
            binding.etCoopMorningPercentage.isEnabled = isChecked
            if (!isChecked) binding.etCoopMorningPercentage.alpha = 0.5f
            else binding.etCoopMorningPercentage.alpha = 1.0f
        }
        
    }
    
    private fun updateOBControlsFromSettings(overtimeRates: List<OvertimeRate>) {
        // Hitta och uppdatera OB-satser från settings
        var hasOB50 = false
        var hasOB70 = false
        var hasOB100 = false
        var hasCoopMorning = false
        
        for (rate in overtimeRates) {
            when {
                rate.name.contains("OB50") && rate.startHour == 18 && !hasOB50 -> {
                    binding.switchOB50.isChecked = true
                    binding.etOB50Percentage.setText(((rate.multiplier - 1.0) * 100).toInt().toString())
                    hasOB50 = true
                }
                rate.name.contains("OB70") && !hasOB70 -> {
                    binding.switchOB70.isChecked = true
                    binding.etOB70Percentage.setText(((rate.multiplier - 1.0) * 100).toInt().toString())
                    hasOB70 = true
                }
                (rate.name.contains("OB100") || rate.name.contains("Helg")) && !hasOB100 -> {
                    binding.switchOB100.isChecked = true
                    binding.etOB100Percentage.setText(((rate.multiplier - 1.0) * 100).toInt().toString())
                    hasOB100 = true
                }
                rate.name.contains("Coop") && rate.startHour == 5 && !hasCoopMorning -> {
                    binding.switchCoopMorning.isChecked = true
                    binding.etCoopMorningPercentage.setText(((rate.multiplier - 1.0) * 100).toInt().toString())
                    hasCoopMorning = true
                }
            }
        }
    }
    
    private fun collectOvertimeRatesFromUI(): List<OvertimeRate> {
        val rates = mutableListOf<OvertimeRate>()
        
        // OB50 - Måndag-fredag 18:15-20:00
        if (binding.switchOB50.isChecked) {
            val percentage = binding.etOB50Percentage.text.toString().toDoubleOrNull() ?: 50.0
            rates.add(OvertimeRate(
                startHour = 18,
                startMinute = 15,
                endHour = 20,
                endMinute = 0,
                multiplier = 1.0 + (percentage / 100.0),
                name = "OB50",
                dayTypes = listOf(se.thomas.arbetstidskalkylator.data.DayType.WEEKDAY),
                priority = 1
            ))
        }
        
        // OB70 - Måndag-fredag efter 20:00
        if (binding.switchOB70.isChecked) {
            val percentage = binding.etOB70Percentage.text.toString().toDoubleOrNull() ?: 70.0
            rates.add(OvertimeRate(
                startHour = 20,
                startMinute = 0,
                endHour = null, // Till slutet av dagen
                endMinute = 0,
                multiplier = 1.0 + (percentage / 100.0),
                name = "OB70",
                dayTypes = listOf(se.thomas.arbetstidskalkylator.data.DayType.WEEKDAY),
                priority = 2
            ))
        }
        
        // OB100 - Lördag efter 12:00, hela söndag och rödadagar
        if (binding.switchOB100.isChecked) {
            val percentage = binding.etOB100Percentage.text.toString().toDoubleOrNull() ?: 100.0
            
            // Lördag från 12:00
            rates.add(OvertimeRate(
                startHour = 12,
                startMinute = 0,
                endHour = null, // Till slutet av dagen
                endMinute = 0,
                multiplier = 1.0 + (percentage / 100.0),
                name = "OB100 Lördag",
                dayTypes = listOf(se.thomas.arbetstidskalkylator.data.DayType.SATURDAY),
                priority = 3
            ))
            
            // Hela söndag
            rates.add(OvertimeRate(
                startHour = 0,
                startMinute = 0,
                endHour = null, // Hela dagen
                endMinute = 0,
                multiplier = 1.0 + (percentage / 100.0),
                name = "OB100 Söndag",
                dayTypes = listOf(se.thomas.arbetstidskalkylator.data.DayType.SUNDAY),
                priority = 3
            ))
            
            // Rödadagar
            rates.add(OvertimeRate(
                startHour = 0,
                startMinute = 0,
                endHour = null, // Hela dagen
                endMinute = 0,
                multiplier = 1.0 + (percentage / 100.0),
                name = "OB100 Helgdag",
                dayTypes = listOf(se.thomas.arbetstidskalkylator.data.DayType.HOLIDAY),
                priority = 4
            ))
        }
        
        // Coop Morgon-OB - Måndag-lördag 05:00-06:00
        if (binding.switchCoopMorning.isChecked) {
            val percentage = binding.etCoopMorningPercentage.text.toString().toDoubleOrNull() ?: 50.0
            rates.add(OvertimeRate(
                startHour = 5,
                startMinute = 0,
                endHour = 6,
                endMinute = 0,
                multiplier = 1.0 + (percentage / 100.0),
                name = "Coop Morgon-OB",
                dayTypes = listOf(se.thomas.arbetstidskalkylator.data.DayType.WEEKDAY, 
                                se.thomas.arbetstidskalkylator.data.DayType.SATURDAY),
                priority = 5 // Högst prioritet för att inte krocka med andra OB
            ))
        }
        
        return rates
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

