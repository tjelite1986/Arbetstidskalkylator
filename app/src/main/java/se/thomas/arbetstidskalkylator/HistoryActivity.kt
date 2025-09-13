package se.thomas.arbetstidskalkylator

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.data.OvertimeRate
import se.thomas.arbetstidskalkylator.databinding.ActivityHistoryBinding
import se.thomas.arbetstidskalkylator.databinding.ItemTimeEntryBinding
import se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel
import se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel
import se.thomas.arbetstidskalkylator.utils.ExportUtils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: TimeEntryViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var adapter: TimeEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TimeEntryViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        setupUI()
        observeTimeEntries()
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Arbetstidshistorik"
        
        setupNavigationButtons()

        adapter = TimeEntryAdapter(
            onEditClick = { timeEntry ->
                val intent = Intent(this, EditTimeEntryActivity::class.java)
                intent.putExtra("TIME_ENTRY_ID", timeEntry.id)
                startActivity(intent)
            },
            onDeleteClick = { timeEntry ->
                showDeleteDialog(timeEntry)
            }
        )
        
        binding.recyclerViewEntries.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEntries.adapter = adapter

        binding.btnExportCSV.setOnClickListener { exportCSV() }
        binding.btnExportSummary.setOnClickListener { exportSummary() }
        binding.btnExportAllPDF.setOnClickListener { exportAllToPDF() }
        binding.btnExportMonthPDF.setOnClickListener { showMonthSelectionDialog() }
    }

    private fun observeTimeEntries() {
        // Observera både tidsposter och inställningar
        viewModel.allTimeEntries.observe(this) { timeEntries ->
            settingsViewModel.settings.observe(this) { settings ->
                val overtimeRates = settings?.overtimeRates ?: emptyList()
                adapter.updateOvertimeRates(overtimeRates)
                adapter.submitList(timeEntries.sortedByDescending { it.startTime })
            }
            
            if (timeEntries.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.recyclerViewEntries.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewEntries.visibility = View.VISIBLE
            }
        }
    }

    private fun showDeleteDialog(timeEntry: TimeEntry) {
        AlertDialog.Builder(this)
            .setTitle("Ta bort arbetstid")
            .setMessage("Vill du ta bort denna arbetstidspost?")
            .setPositiveButton("Ta bort") { _, _ ->
                viewModel.deleteTimeEntry(timeEntry)
            }
            .setNegativeButton("Avbryt", null)
            .show()
    }

    private fun exportCSV() {
        viewModel.allTimeEntries.value?.let { timeEntries ->
            if (timeEntries.isEmpty()) {
                Toast.makeText(this, "Inga arbetstider att exportera", Toast.LENGTH_SHORT).show()
                return
            }
            
            val overtimeRates = settingsViewModel.settings.value?.overtimeRates ?: emptyList()
            ExportUtils.exportToCSV(this, timeEntries, overtimeRates)?.let { uri ->
                ExportUtils.shareCSV(this, uri)
            } ?: run {
                Toast.makeText(this, "Kunde inte exportera CSV", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun exportSummary() {
        viewModel.allTimeEntries.value?.let { timeEntries ->
            val overtimeRates = settingsViewModel.settings.value?.overtimeRates ?: emptyList()
            val summaryText = ExportUtils.exportSummaryText(timeEntries, overtimeRates)
            ExportUtils.shareSummaryText(this, summaryText)
        }
    }
    
    private fun exportAllToPDF() {
        viewModel.allTimeEntries.value?.let { timeEntries ->
            if (timeEntries.isEmpty()) {
                Toast.makeText(this, "Inga arbetstider att exportera", Toast.LENGTH_SHORT).show()
                return
            }
            
            val overtimeRates = settingsViewModel.settings.value?.overtimeRates ?: emptyList()
            ExportUtils.exportAllToPDF(this, timeEntries, overtimeRates)?.let { uri ->
                ExportUtils.sharePDF(this, uri)
                Toast.makeText(this, "PDF skapad och delad", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Kunde inte skapa PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showMonthSelectionDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        
        DatePickerDialog(
            this,
            { _, year, month, _ ->
                exportMonthToPDF(month + 1, year) // month är 0-baserad
            },
            currentYear,
            currentMonth,
            1 // Dag spelar ingen roll för månadsval
        ).apply {
            setTitle("Välj månad att exportera")
            show()
        }
    }
    
    private fun exportMonthToPDF(month: Int, year: Int) {
        lifecycleScope.launch {
            try {
                val timeEntries = viewModel.repository.getTimeEntriesForMonth(month, year)
                
                if (timeEntries.isEmpty()) {
                    Toast.makeText(
                        this@HistoryActivity, 
                        "Inga arbetstider registrerade för vald månad", 
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                
                val overtimeRates = settingsViewModel.settings.value?.overtimeRates ?: emptyList()
                ExportUtils.exportMonthToPDF(this@HistoryActivity, timeEntries, month, year, overtimeRates)?.let { uri ->
                    ExportUtils.sharePDF(this@HistoryActivity, uri)
                    Toast.makeText(this@HistoryActivity, "PDF skapad och delad", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(this@HistoryActivity, "Kunde inte skapa PDF", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@HistoryActivity, "Fel vid PDF-export: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
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
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
        
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}

class TimeEntryAdapter(
    private val onEditClick: (TimeEntry) -> Unit,
    private val onDeleteClick: (TimeEntry) -> Unit
) : RecyclerView.Adapter<TimeEntryAdapter.TimeEntryViewHolder>() {
    
    private var timeEntries = listOf<TimeEntry>()
    private var overtimeRates = listOf<OvertimeRate>()
    private val expandedItems = mutableSetOf<Int>() // Håller reda på vilka items som är expanderade
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val weekdayFormat = SimpleDateFormat("EEEE", Locale("sv", "SE")) // Svenska veckodagar
    
    // Funktion för att kontrollera om datum är helgdag eller söndag
    private fun isHolidayOrSunday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        
        // Kontrollera söndag
        if (dayOfWeek == Calendar.SUNDAY) return true
        
        // Kontrollera svenska helgdagar (förenklad version)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        return when (month) {
            Calendar.JANUARY -> day == 1 || day == 6 // Nyårsdagen, Trettondag jul
            Calendar.MARCH, Calendar.APRIL -> isEasterRelated(calendar) // Påsk-relaterade  
            Calendar.MAY -> day == 1 || isEasterRelated(calendar) // Första maj + påsk-relaterade
            Calendar.JUNE -> day == 6 // Nationaldagen
            Calendar.DECEMBER -> day == 24 || day == 25 || day == 26 || day == 31 // Julhelger + Nyårsafton
            else -> false
        }
    }
    
    // Förenklad påsk-kontroll (kan förbättras med exakt påskberäkning)
    private fun isEasterRelated(calendar: Calendar): Boolean {
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        // Approximativ kontroll för påsk-relaterade helger (mars-maj)
        // Detta är en förenklad version - exakt påskberäkning är mer komplex
        return (month == Calendar.MARCH && day >= 20) ||
               (month == Calendar.APRIL && day <= 26) ||
               (month == Calendar.MAY && day <= 15)
    }

    fun submitList(list: List<TimeEntry>) {
        timeEntries = list
        expandedItems.clear() // Rensa expanderade items när listan uppdateras
        notifyDataSetChanged()
    }
    
    fun updateOvertimeRates(rates: List<OvertimeRate>) {
        overtimeRates = rates
        notifyDataSetChanged()
    }
    
    private fun formatHoursToReadable(hours: Double): String {
        val hoursInt = hours.toInt()
        val minutes = ((hours - hoursInt) * 60).toInt()
        return "${hoursInt}h ${minutes}min"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeEntryViewHolder {
        val binding = ItemTimeEntryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TimeEntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeEntryViewHolder, position: Int) {
        holder.bind(timeEntries[position], position)
    }

    override fun getItemCount() = timeEntries.size

    inner class TimeEntryViewHolder(private val binding: ItemTimeEntryBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(timeEntry: TimeEntry, position: Int) {
            // Grundinformation som alltid visas - kollapsad vy
            val weekday = weekdayFormat.format(timeEntry.startTime)
            binding.tvWeekday.text = weekday.replaceFirstChar { it.uppercase() }
            binding.tvDate.text = dateFormat.format(timeEntry.startTime)
            
            // Markera helgdagar och söndagar med röd färg
            val isHoliday = isHolidayOrSunday(timeEntry.startTime)
            if (isHoliday) {
                // Röd text för veckodag och datum på helgdagar
                binding.tvWeekday.setTextColor(binding.root.context.getColor(android.R.color.holo_red_light))
                binding.tvDate.setTextColor(binding.root.context.getColor(android.R.color.holo_red_light))
                // Lägg till en emoji för extra tydlighet
                val originalWeekday = binding.tvWeekday.text.toString()
                if (!originalWeekday.contains("🔴")) {
                    binding.tvWeekday.text = "🔴 $originalWeekday"
                }
            } else {
                // Vanlig vit text för vardagar
                binding.tvWeekday.setTextColor(binding.root.context.getColor(android.R.color.white))
                binding.tvDate.setTextColor(binding.root.context.getColor(android.R.color.white))
            }
            binding.tvTimeRange.text = "${timeFormat.format(timeEntry.startTime)} - ${
                timeEntry.endTime?.let { timeFormat.format(it) } ?: "Pågående"
            }"
            
            // Nytt tidsformat: 8h 30min
            binding.tvHours.text = formatHoursToReadable(timeEntry.getWorkedHours())
            
            // Total lön summa i kollapsad vy
            binding.tvTotalPaySummary.text = "%.2f kr".format(timeEntry.calculatePay(overtimeRates))
            
            // Expanderbar information
            binding.tvDescription.text = timeEntry.description?.takeIf { it.isNotBlank() } ?: "Ingen beskrivning"
            binding.tvHourlyRate.text = "%.2f kr/h".format(timeEntry.hourlyRate)
            binding.tvTotalPay.text = "%.2f kr".format(timeEntry.calculatePay(overtimeRates))
            
            // Visa rastinformation
            val breakInfo = when {
                timeEntry.customBreakStart != null && timeEntry.customBreakEnd != null -> {
                    val breakMinutes = ((timeEntry.customBreakEnd.time - timeEntry.customBreakStart.time) / (1000 * 60)).toInt()
                    "Anpassad rast: ${timeFormat.format(timeEntry.customBreakStart)} - ${timeFormat.format(timeEntry.customBreakEnd)} (${breakMinutes} min)"
                }
                timeEntry.breakMinutes > 0 -> "Förbestämd rast: ${timeEntry.breakMinutes} min"
                timeEntry.isBreakDeducted && timeEntry.getWorkedHours() > 6 -> "Automatisk lunchrast: 30 min"
                else -> "Ingen rast"
            }
            binding.tvBreakInfo.text = breakInfo
            
            // Expandera/kollaps logik
            val isExpanded = expandedItems.contains(position)
            binding.layoutExpandedContent.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.ivExpandArrow.rotation = if (isExpanded) 180f else 0f
            
            // Klick-lyssnare för att expandera/kollaps
            binding.layoutMainRow.setOnClickListener {
                if (isExpanded) {
                    expandedItems.remove(position)
                } else {
                    expandedItems.add(position)
                }
                // Använd notifyDataSetChanged för att säkerställa uppdatering
                notifyDataSetChanged()
            }
            
            // Knappar i expanderat innehåll
            binding.btnEdit.setOnClickListener {
                onEditClick(timeEntry)
            }
            
            binding.btnDelete.setOnClickListener {
                onDeleteClick(timeEntry)
            }
        }
    }
}