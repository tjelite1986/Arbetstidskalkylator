package se.thomas.arbetstidskalkylator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.databinding.ActivityStatisticsBinding
import se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel
import se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel
import se.thomas.arbetstidskalkylator.utils.LogUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var viewModel: TimeEntryViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private var selectedCalendar = Calendar.getInstance()
    private var showAllTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TimeEntryViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        setupUI()
        observeTimeEntries()
        
        // Rensa loggen vid start av Statistik-sidan
        LogUtils.clearLog(this)
        LogUtils.writeLog(this, "Statistics", "=== Statistik öppnad ===")
        
        // Initiera standardinställningar om de inte finns
        settingsViewModel.initializeDefaultSettingsIfNeeded()
        
        // Tvinga en uppdatering av OB-satser för befintliga användare
        settingsViewModel.setupStandardOvertimeRates()
    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Statistik"
        
        setupNavigationButtons()
        
        // Setup period selector buttons
        binding.btnPreviousMonth.setOnClickListener {
            selectedCalendar.add(Calendar.MONTH, -1)
            showAllTime = false
            updatePeriodDisplay()
            refreshStatistics()
        }
        
        binding.btnNextMonth.setOnClickListener {
            selectedCalendar.add(Calendar.MONTH, 1)
            showAllTime = false
            updatePeriodDisplay()
            refreshStatistics()
        }
        
        binding.btnThisMonth.setOnClickListener {
            selectedCalendar = Calendar.getInstance()
            showAllTime = false
            updatePeriodDisplay()
            refreshStatistics()
        }
        
        binding.btnAllTime.setOnClickListener {
            showAllTime = true
            updatePeriodDisplay()
            refreshStatistics()
        }
        
        // Initialize period display
        updatePeriodDisplay()
    }

    private fun observeTimeEntries() {
        viewModel.allTimeEntries.observe(this) { timeEntries ->
            settingsViewModel.settings.observe(this) { settings ->
                val filteredEntries = if (showAllTime) {
                    timeEntries
                } else {
                    filterEntriesByMonth(timeEntries)
                }
                updateStatistics(filteredEntries, settings)
            }
        }
    }
    
    private fun refreshStatistics() {
        viewModel.allTimeEntries.value?.let { allTimeEntries ->
            settingsViewModel.settings.value?.let { settings ->
                val filteredEntries = if (showAllTime) {
                    allTimeEntries
                } else {
                    filterEntriesByMonth(allTimeEntries)
                }
                updateStatistics(filteredEntries, settings)
            }
        }
    }
    
    private fun filterEntriesByMonth(timeEntries: List<TimeEntry>): List<TimeEntry> {
        val startOfMonth = Calendar.getInstance().apply {
            time = selectedCalendar.time
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        val endOfMonth = Calendar.getInstance().apply {
            time = selectedCalendar.time
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time
        
        return timeEntries.filter { entry ->
            entry.startTime >= startOfMonth && entry.startTime <= endOfMonth
        }
    }
    
    private fun updatePeriodDisplay() {
        if (showAllTime) {
            binding.tvSelectedMonth.text = "Alla månader"
            binding.tvPeriod.text = "Samtliga registrerade arbetstider"
        } else {
            val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("sv", "SE"))
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            
            val startOfMonth = Calendar.getInstance().apply {
                time = selectedCalendar.time
                set(Calendar.DAY_OF_MONTH, 1)
            }.time
            
            val endOfMonth = Calendar.getInstance().apply {
                time = selectedCalendar.time
                set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            }.time
            
            binding.tvSelectedMonth.text = monthFormat.format(selectedCalendar.time).replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
            }
            binding.tvPeriod.text = "${dateFormat.format(startOfMonth)} - ${dateFormat.format(endOfMonth)}"
        }
    }

    private fun updateStatistics(timeEntries: List<TimeEntry>, settings: se.thomas.arbetstidskalkylator.data.Settings?) {
        if (timeEntries.isEmpty()) {
            showEmptyState()
            return
        }

        hideEmptyState()
        
        val totalHours = timeEntries.sumOf { it.getWorkedHours() }
        val totalEntries = timeEntries.size
        val averageHoursPerDay = if (totalEntries > 0) totalHours / totalEntries else 0.0
        
        // Period calculation
        val sortedEntries = timeEntries.sortedBy { it.startTime }
        val firstDate = sortedEntries.first().startTime
        val lastDate = sortedEntries.last().startTime
        val daysBetween = ((lastDate.time - firstDate.time) / (24 * 60 * 60 * 1000)).toInt() + 1
        
        // Pay calculations
        val overtimeRates = settings?.overtimeRates ?: emptyList()
        val grossPay = timeEntries.sumOf { it.calculatePay(overtimeRates) }
        
        val netPay = if (settings != null) {
            val tax = grossPay * (settings.taxPercentage / 100)
            grossPay - tax
        } else {
            grossPay
        }
        
        val vacationPay = if (settings != null) {
            grossPay * (settings.vacationPayPercentage / 100)
        } else {
            0.0
        }
        
        val averagePayPerHour = if (totalHours > 0) grossPay / totalHours else 0.0
        val averagePayPerDay = if (totalEntries > 0) grossPay / totalEntries else 0.0
        
        // Calculate OB hours for overview
        var totalOB50Hours = 0.0
        var totalOB100Hours = 0.0
        
        for (entry in timeEntries) {
            if (entry.endTime == null) continue
            val breakdown = calculateEntryBreakdown(entry, overtimeRates)
            totalOB50Hours += breakdown.ob50Hours
            totalOB100Hours += breakdown.ob100Hours
        }
        
        val ob50Percentage = if (totalHours > 0) (totalOB50Hours / totalHours * 100).roundToInt() else 0
        val ob100Percentage = if (totalHours > 0) (totalOB100Hours / totalHours * 100).roundToInt() else 0
        
        // Update UI med svenska format
        binding.tvTotalEntries.text = totalEntries.toString()
        binding.tvTotalHours.text = formatHoursWithBothFormats(totalHours)
        binding.tvTotalDays.text = "$daysBetween dagar"
        binding.tvAverageHoursPerDay.text = formatHoursWithBothFormats(averageHoursPerDay) + "/dag"
        binding.tvGrossPay.text = "%.2f".format(grossPay).replace(".", ",")
        binding.tvNetPay.text = "%.2f".format(netPay).replace(".", ",")
        binding.tvVacationPay.text = "%.2f".format(vacationPay).replace(".", ",")
        binding.tvAveragePayPerHour.text = "%.2f".format(averagePayPerHour).replace(".", ",")
        binding.tvAveragePayPerDay.text = "%.2f".format(averagePayPerDay).replace(".", ",")
        
        // Update OB hours overview
        binding.tvOB50HoursOverview.text = formatHoursWithBothFormats(totalOB50Hours)
        binding.tvOB50Percentage.text = "$ob50Percentage%"
        binding.tvOB100HoursOverview.text = formatHoursWithBothFormats(totalOB100Hours)
        binding.tvOB100Percentage.text = "$ob100Percentage%"
        
        // Period display
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvPeriod.text = "${dateFormat.format(firstDate)} - ${dateFormat.format(lastDate)}"
        
        // Weekly and monthly statistics - always use all entries for weekly stats
        updateWeeklyMonthlyStats(viewModel.allTimeEntries.value ?: emptyList(), overtimeRates)
        
        // Hours distribution
        updateHoursDistribution(timeEntries)
        
        // Salary breakdown
        updateSalaryBreakdown(timeEntries, settings)
    }

    private fun updateWeeklyMonthlyStats(timeEntries: List<TimeEntry>, overtimeRates: List<se.thomas.arbetstidskalkylator.data.OvertimeRate>) {
        val calendar = Calendar.getInstance()
        val now = Date()
        
        // This week - Swedish week starts on Monday
        calendar.time = now
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.time
        
        val thisWeekEntries = timeEntries.filter { it.startTime >= weekStart }
        val thisWeekHours = thisWeekEntries.sumOf { it.getWorkedHours() }
        val thisWeekPay = thisWeekEntries.sumOf { it.calculatePay(overtimeRates) }
        
        binding.tvThisWeekHours.text = formatHoursWithBothFormats(thisWeekHours)
        binding.tvThisWeekPay.text = "%.2f".format(thisWeekPay).replace(".", ",")
        
        // This month
        calendar.time = now
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val monthStart = calendar.time
        
        val thisMonthEntries = timeEntries.filter { it.startTime >= monthStart }
        val thisMonthHours = thisMonthEntries.sumOf { it.getWorkedHours() }
        val thisMonthPay = thisMonthEntries.sumOf { it.calculatePay(overtimeRates) }
        
        binding.tvThisMonthHours.text = formatHoursWithBothFormats(thisMonthHours)
        binding.tvThisMonthPay.text = "%.2f".format(thisMonthPay).replace(".", ",")
    }

    private fun updateHoursDistribution(timeEntries: List<TimeEntry>) {
        // Räkna antal pass i varje kategori
        val shortShifts = timeEntries.count { it.getWorkedHours() < 4 }      // Korta pass (under 4h)
        val partTimeShifts = timeEntries.count { 
            val hours = it.getWorkedHours()
            hours >= 4 && hours < 6 
        }                                                                    // Deltid (4-6h)
        val normalShifts = timeEntries.count { 
            val hours = it.getWorkedHours()
            hours >= 6 && hours <= 8 
        }                                                                    // Normal arbetstid (6-8h)
        val longShifts = timeEntries.count { 
            val hours = it.getWorkedHours()
            hours > 8 && hours <= 10 
        }                                                                    // Långa pass (8-10h)
        val overtimeShifts = timeEntries.count { it.getWorkedHours() > 10 }  // Mycket övertid (över 10h)
        
        val total = timeEntries.size
        
        // Uppdatera UI med tabellformat
        binding.tvDistribution0to4Count.text = "$shortShifts st"
        binding.tvDistribution0to4Percent.text = if (total > 0) "${shortShifts * 100 / total}%" else "0%"
        
        binding.tvDistribution4to6Count.text = "$partTimeShifts st"
        binding.tvDistribution4to6Percent.text = if (total > 0) "${partTimeShifts * 100 / total}%" else "0%"
        
        binding.tvDistribution6to8Count.text = "$normalShifts st"
        binding.tvDistribution6to8Percent.text = if (total > 0) "${normalShifts * 100 / total}%" else "0%"
        
        binding.tvDistribution8to10Count.text = "$longShifts st"
        binding.tvDistribution8to10Percent.text = if (total > 0) "${longShifts * 100 / total}%" else "0%"
        
        binding.tvDistribution10plusCount.text = "$overtimeShifts st"
        binding.tvDistribution10plusPercent.text = if (total > 0) "${overtimeShifts * 100 / total}%" else "0%"
    }
    
    private fun updateSalaryBreakdown(timeEntries: List<TimeEntry>, settings: se.thomas.arbetstidskalkylator.data.Settings?) {
        val overtimeRates = settings?.overtimeRates ?: emptyList()
        
        // Uppdatera månadslabel baserat på vald period
        val monthLabel = if (showAllTime) {
            "Tim [Totalt]"
        } else {
            val monthFormat = java.text.SimpleDateFormat("MMMM", java.util.Locale("sv", "SE"))
            val monthName = monthFormat.format(selectedCalendar.time).replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() 
            }
            "Tim [$monthName]"
        }
        binding.tvTimMandLabel.text = monthLabel
        
        LogUtils.writeLog(this, "Statistics", "Beräknar löneuppdelning: ${timeEntries.size} poster, ${overtimeRates.size} OB-satser")
        
        var regularHours = 0.0
        var regularPay = 0.0
        var ob50Hours = 0.0
        var ob50SupplementPay = 0.0  // Bara OB-tillägget
        var ob70Hours = 0.0
        var ob70SupplementPay = 0.0  // Bara OB-tillägget
        var ob100Hours = 0.0
        var ob100SupplementPay = 0.0 // Bara OB-tillägget
        var averageRegularRate = 0.0
        
        var totalRegularEntries = 0
        var totalAllHours = 0.0  // Alla timmar för Tim [Månad]
        var totalAllBasePay = 0.0 // Alla timmar * grundlön
        
        // Analysera varje tidspost
        for (entry in timeEntries) {
            if (entry.endTime == null) continue
            
            val breakdown = calculateEntryBreakdown(entry, overtimeRates)
            val entryTotalHours = breakdown.regularHours + breakdown.ob50Hours + breakdown.ob70Hours + breakdown.ob100Hours
            
            // Lägg till alla timmar till Tim [Månad] - bara vanliga timmar får grundlön
            totalAllHours += entryTotalHours
            totalAllBasePay += breakdown.regularHours * entry.hourlyRate  // Bara vanliga timmar med grundlön
            
            regularHours += breakdown.regularHours
            regularPay += breakdown.regularPay
            ob50Hours += breakdown.ob50Hours
            ob70Hours += breakdown.ob70Hours
            ob100Hours += breakdown.ob100Hours
            
            // Beräkna bara OB-tilläggen (inte totallön) - detta är korrekt
            ob50SupplementPay += breakdown.ob50Hours * entry.hourlyRate * 0.5  // 50% tillägg
            ob70SupplementPay += breakdown.ob70Hours * entry.hourlyRate * 0.7  // 70% tillägg
            ob100SupplementPay += breakdown.ob100Hours * entry.hourlyRate * 1.0 // 100% tillägg
            
            averageRegularRate += entry.hourlyRate
            totalRegularEntries++
        }
        
        // Beräkna genomsnittslön
        if (totalRegularEntries > 0) averageRegularRate /= totalRegularEntries
        
        // Uppdatera UI med svenska format (komma som decimaltecken)
        // Tim [Månad] = alla timmar för månaden med grundlön
        binding.tvRegularHours.text = formatHoursWithBothFormats(totalAllHours)
        binding.tvRegularRate.text = "%.2f".format(averageRegularRate).replace(".", ",")
        binding.tvRegularPay.text = "%.2f".format(totalAllHours * averageRegularRate).replace(".", ",")
        
        // Visa/dölj OB-rader baserat på om det finns timmar - visa OB-timmar med tillägg
        if (ob50Hours > 0) {
            binding.layoutOB50.visibility = android.view.View.VISIBLE
            binding.tvOB50Hours.text = formatHoursWithBothFormats(ob50Hours)
            binding.tvOB50Rate.text = "%.2f".format(averageRegularRate * 1.5).replace(".", ",")  // 150% av grundlön (100% + 50% tillägg)
            binding.tvOB50Pay.text = "%.2f".format(ob50Hours * averageRegularRate * 1.5).replace(".", ",")  // Total OB50-lön
        } else {
            binding.layoutOB50.visibility = android.view.View.GONE
        }
        
        if (ob70Hours > 0) {
            binding.layoutOB70.visibility = android.view.View.VISIBLE
            binding.tvOB70Hours.text = formatHoursWithBothFormats(ob70Hours)
            binding.tvOB70Rate.text = "%.2f".format(averageRegularRate * 1.7).replace(".", ",")  // 170% av grundlön (100% + 70% tillägg)
            binding.tvOB70Pay.text = "%.2f".format(ob70Hours * averageRegularRate * 1.7).replace(".", ",")  // Total OB70-lön
        } else {
            binding.layoutOB70.visibility = android.view.View.GONE
        }
        
        if (ob100Hours > 0) {
            binding.layoutOB100.visibility = android.view.View.VISIBLE
            binding.tvOB100Hours.text = formatHoursWithBothFormats(ob100Hours)
            binding.tvOB100Rate.text = "%.2f".format(averageRegularRate * 2.0).replace(".", ",")  // 200% av grundlön (100% + 100% tillägg)
            binding.tvOB100Pay.text = "%.2f".format(ob100Hours * averageRegularRate * 2.0).replace(".", ",")  // Total OB100-lön
        } else {
            binding.layoutOB100.visibility = android.view.View.GONE
        }
        
        LogUtils.writeLog(this, "Statistics", "Tim [Månad]: ${totalAllHours.toInt()}h, OB50: ${ob50Hours.toInt()}h (tillägg: ${ob50SupplementPay.toInt()}kr), OB100: ${ob100Hours.toInt()}h (tillägg: ${ob100SupplementPay.toInt()}kr)")
        
        // Beräkna skatt och sammanfattning - Total bruttolön beräknas korrekt
        val grossPay = totalAllBasePay + (ob50Hours * averageRegularRate * 1.5) + (ob70Hours * averageRegularRate * 1.7) + (ob100Hours * averageRegularRate * 2.0)
        val taxAmount = if (settings != null) {
            grossPay * (settings.taxPercentage / 100)
        } else {
            0.0
        }
        val netPay = grossPay - taxAmount
        
        // Uppdatera preliminär skatt
        binding.tvTaxAmount.text = "-%.2f".format(taxAmount).replace(".", ",")
        
        // Uppdatera sammanfattningssektion 
        binding.tvMonthlyGrossPay.text = "%.2f".format(grossPay).replace(".", ",")
        binding.tvMonthlyTax.text = "%.2f".format(taxAmount).replace(".", ",")
        binding.tvMonthlyHours.text = "%.2f".format(totalAllHours).replace(".", ",")
        binding.tvMonthlyNetPay.text = "%.2f".format(netPay).replace(".", ",")
        
        // Uppdatera summering-kolumnerna (alltid med totalsummor från alla månader)
        val allMonthsSummary = calculateAllMonthsSummary(viewModel.allTimeEntries.value ?: emptyList(), settings)
        binding.tvSummaryGrossPay.text = "%.2f".format(allMonthsSummary.totalGrossPay).replace(".", ",")
        binding.tvSummaryTax.text = "%.2f".format(allMonthsSummary.totalTax).replace(".", ",")
        binding.tvSummaryHours.text = "%.2f".format(allMonthsSummary.totalHours).replace(".", ",")
        binding.tvSummaryNetPay.text = "%.2f".format(allMonthsSummary.totalNetPay).replace(".", ",")
    }
    
    data class SalaryBreakdown(
        val regularHours: Double = 0.0,
        val regularPay: Double = 0.0,
        val ob50Hours: Double = 0.0,
        val ob50Pay: Double = 0.0,
        val ob70Hours: Double = 0.0,
        val ob70Pay: Double = 0.0,
        val ob100Hours: Double = 0.0,
        val ob100Pay: Double = 0.0
    )
    
    data class AllMonthsSummary(
        val totalGrossPay: Double = 0.0,
        val totalTax: Double = 0.0,
        val totalHours: Double = 0.0,
        val totalNetPay: Double = 0.0
    )
    
    private fun calculateAllMonthsSummary(timeEntries: List<TimeEntry>, settings: se.thomas.arbetstidskalkylator.data.Settings?): AllMonthsSummary {
        val overtimeRates = settings?.overtimeRates ?: emptyList()
        
        // Beräkna totaler för alla månader
        val totalHours = timeEntries.sumOf { it.getWorkedHours() }
        val totalGrossPay = timeEntries.sumOf { it.calculatePay(overtimeRates) }
        
        val totalTax = if (settings != null) {
            totalGrossPay * (settings.taxPercentage / 100)
        } else {
            0.0
        }
        
        val totalNetPay = totalGrossPay - totalTax
        
        return AllMonthsSummary(
            totalGrossPay = totalGrossPay,
            totalTax = totalTax, 
            totalHours = totalHours,
            totalNetPay = totalNetPay
        )
    }
    
    private fun calculateEntryBreakdown(entry: TimeEntry, overtimeRates: List<se.thomas.arbetstidskalkylator.data.OvertimeRate>): SalaryBreakdown {
        if (entry.endTime == null) return SalaryBreakdown()
        
        var regularHours = 0.0
        var regularPay = 0.0
        var ob50Hours = 0.0
        var ob50Pay = 0.0
        var ob70Hours = 0.0
        var ob70Pay = 0.0
        var ob100Hours = 0.0
        var ob100Pay = 0.0
        
        val currentTime = java.util.Calendar.getInstance().apply { time = entry.startTime }
        val endTime = java.util.Calendar.getInstance().apply { time = entry.endTime }
        
        // Bestäm rasttidsintervall
        val breakStart = when {
            entry.customBreakStart != null -> java.util.Calendar.getInstance().apply { time = entry.customBreakStart }
            entry.breakMinutes > 0 -> null // Förbestämd rast hanteras separat
            else -> null
        }
        val breakEnd = when {
            entry.customBreakEnd != null -> java.util.Calendar.getInstance().apply { time = entry.customBreakEnd }
            entry.breakMinutes > 0 -> null
            else -> null
        }
        
        // Automatisk lunchrast (30 min) för pass över 6 timmar - placeras mitt i passet
        val autoBreakStart = if (entry.isBreakDeducted && entry.breakMinutes == 0 && breakStart == null) {
            val totalMinutes = ((entry.endTime.time - entry.startTime.time) / (1000 * 60)).toInt()
            if (totalMinutes > 360) {
                val midPoint = totalMinutes / 2
                java.util.Calendar.getInstance().apply { 
                    time = entry.startTime
                    add(java.util.Calendar.MINUTE, midPoint - 15) // 15 min före mittpunkt
                }
            } else null
        } else null
        val autoBreakEnd = autoBreakStart?.let {
            java.util.Calendar.getInstance().apply {
                time = it.time
                add(java.util.Calendar.MINUTE, 30)
            }
        }
        
        while (currentTime.before(endTime)) {
            val hour = currentTime.get(java.util.Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(java.util.Calendar.MINUTE)
            val dayOfWeek = currentTime.get(java.util.Calendar.DAY_OF_WEEK)
            
            // Kontrollera om vi är i rasttid
            val inBreak = when {
                // Anpassad rast
                breakStart != null && breakEnd != null -> 
                    !currentTime.before(breakStart) && currentTime.before(breakEnd)
                // Automatisk lunchrast
                autoBreakStart != null && autoBreakEnd != null ->
                    !currentTime.before(autoBreakStart) && currentTime.before(autoBreakEnd)
                // Förbestämd rast - enkel avdragning i slutet
                entry.breakMinutes > 0 -> false // Hanteras genom att räkna exakt arbetstid minus exakt rasttid
                else -> false
            }
            
            if (!inBreak) {
                // Bestäm dagtyp
                val dayType = when (dayOfWeek) {
                    java.util.Calendar.SATURDAY -> se.thomas.arbetstidskalkylator.data.DayType.SATURDAY
                    java.util.Calendar.SUNDAY -> se.thomas.arbetstidskalkylator.data.DayType.SUNDAY
                    else -> se.thomas.arbetstidskalkylator.data.DayType.WEEKDAY
                }
                
                // Hitta högsta OB-sats som gäller för denna tid och dag
                val applicableRate = overtimeRates
                    .filter { it.isActiveAt(hour, minute, dayType) }
                    .maxByOrNull { it.priority * 1000 + it.multiplier }
                
                val multiplier = applicableRate?.multiplier ?: 1.0
                val payPerMinute = (entry.hourlyRate * multiplier) / 60.0
                
                // Klassificera baserat på multiplikator
                when {
                    multiplier >= 2.0 -> {
                        ob100Hours += 1.0 / 60.0
                        ob100Pay += payPerMinute
                    }
                    multiplier >= 1.7 -> {
                        ob70Hours += 1.0 / 60.0
                        ob70Pay += payPerMinute
                    }
                    multiplier >= 1.5 -> {
                        ob50Hours += 1.0 / 60.0
                        ob50Pay += payPerMinute
                    }
                    else -> {
                        regularHours += 1.0 / 60.0
                        regularPay += payPerMinute
                    }
                }
            }
            
            currentTime.add(java.util.Calendar.MINUTE, 1)
        }
        
        // Hantera förbestämda raster - dra av exakt tid proportionellt
        if (entry.breakMinutes > 0) {
            val totalCalculatedHours = regularHours + ob50Hours + ob70Hours + ob100Hours
            val breakHours = entry.breakMinutes / 60.0
            val reductionRatio = breakHours / (totalCalculatedHours + breakHours)
            
            regularHours *= (1 - reductionRatio)
            regularPay *= (1 - reductionRatio)
            ob50Hours *= (1 - reductionRatio)
            ob50Pay *= (1 - reductionRatio)
            ob70Hours *= (1 - reductionRatio)
            ob70Pay *= (1 - reductionRatio)
            ob100Hours *= (1 - reductionRatio)
            ob100Pay *= (1 - reductionRatio)
        }
        
        return SalaryBreakdown(
            regularHours, regularPay,
            ob50Hours, ob50Pay,
            ob70Hours, ob70Pay,
            ob100Hours, ob100Pay
        )
    }

    private fun formatHoursWithBothFormats(hours: Double): String {
        val hoursInt = hours.toInt()
        val minutes = ((hours - hoursInt) * 60).toInt()
        val decimal = "%.2f Tim".format(hours).replace(".", ",")
        val readable = "%dh %02dm".format(hoursInt, minutes)
        return "$decimal • $readable"
    }

    private fun showEmptyState() {
        binding.scrollContent.visibility = android.view.View.GONE
        binding.tvEmptyState.visibility = android.view.View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.scrollContent.visibility = android.view.View.VISIBLE
        binding.tvEmptyState.visibility = android.view.View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun setupNavigationButtons() {
        binding.btnMain.setOnClickListener {
            finish() // Går tillbaka till MainActivity
        }
        
        binding.btnHistory.setOnClickListener {
            startActivity(android.content.Intent(this, HistoryActivity::class.java))
        }
        
        binding.btnSettings.setOnClickListener {
            startActivity(android.content.Intent(this, SettingsActivity::class.java))
        }
        
        binding.btnCalendar.setOnClickListener {
            startActivity(android.content.Intent(this, CalendarActivity::class.java))
        }
    }
}