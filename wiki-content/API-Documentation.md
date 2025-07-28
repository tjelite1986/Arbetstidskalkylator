# 🔧 API Documentation

Teknisk dokumentation för Arbetstidskalkylator's interna API och arkitektur.

## 🏗️ Arkitektur Overview

### MVVM Pattern
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UI Components │    │   ViewModels    │    │  Data Models    │
│   (Composables) │◄──►│   (State Mgmt)  │◄──►│  (Business)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Module Structure
- **`data/`** - Business logic och datamodeller
- **`ui/`** - Jetpack Compose UI komponenter
- **`calculator/`** - Löneberäknings-engine
- **`export/`** - Fil-management och backup
- **`navigation/`** - App navigation logic

## 📊 Core Data Models

### TimeEntry
Huvuddatamodell för arbetsdagar med automatiska löneberäkningar.

```kotlin
data class TimeEntry(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val breakMinutes: Int = 0,
    val workplace: WorkplaceType = WorkplaceType.BUTIK,
    val description: String = "",
    val contractLevel: ContractLevel = ContractLevel.LEVEL_18_YEAR,
    val customHourlyRate: Double? = null
) {
    // Automatiska beräkningar
    val totalMinutes: Int get() = ChronoUnit.MINUTES.between(startTime, endTime).toInt()
    val workingMinutes: Int get() = totalMinutes - breakMinutes
    val workingHours: Double get() = workingMinutes / 60.0
    val basePay: Double get() = /* beräknas automatiskt */
    val obPay: Double get() = /* beräknas automatiskt */
    val totalPay: Double get() = basePay + obPay
}

enum class WorkplaceType { BUTIK, LAGER }
enum class ContractLevel(val hourlyRate: Double) {
    LEVEL_16_YEAR(101.48),
    LEVEL_17_YEAR(103.95),
    LEVEL_18_YEAR(155.51),
    LEVEL_19_YEAR(157.44),
    LEVEL_1_YEAR_EXP(160.95),
    LEVEL_2_YEAR_EXP(162.98),
    LEVEL_3_PLUS_YEAR_EXP(165.84)
}
```

### Settings
App-konfiguration inklusive avtalsnivåer och OB-satser.

```kotlin
data class Settings(
    val contractLevel: ContractLevel = ContractLevel.LEVEL_18_YEAR,
    val defaultWorkplace: WorkplaceType = WorkplaceType.BUTIK,
    val taxRate: Double = 0.30,
    val automaticBreaks: Boolean = true,
    val butikObRates: Map<String, Double> = defaultButikObRates,
    val lagerObRates: Map<String, Double> = defaultLagerObRates,
    val customHolidays: List<LocalDate> = emptyList()
) {
    companion object {
        val defaultButikObRates = mapOf(
            "weekday_evening" to 0.50,  // Mån-fre 18:15-20:00
            "weekday_night" to 0.70,    // Mån-fre efter 20:00
            "saturday_afternoon" to 1.0, // Lördag efter 12:00
            "sunday" to 1.0,            // Söndag hela dagen
            "holiday" to 1.0            // Helgdagar
        )
        
        val defaultLagerObRates = mapOf(
            "weekday_early" to 0.40,    // Mån-fre 06:00-07:00
            "weekday_evening" to 0.40,  // Mån-fre 18:00-23:00
            "weekday_night" to 0.70,    // Mån-fre 23:00-06:00
            "saturday_day" to 0.40,     // Lördag 06:00-23:00
            "saturday_night" to 0.70,   // Lördag nätter
            "sunday" to 1.0,            // Söndag
            "holiday" to 1.0            // Helgdagar
        )
    }
}
```

## 💰 PayCalculator API

Huvudklassen för löneberäkningar enligt Detaljhandelsavtalet.

### Core Methods

```kotlin
object PayCalculator {
    /**
     * Beräknar total lön för en arbetsdag
     */
    fun calculateDayPay(
        timeEntry: TimeEntry,
        settings: Settings,
        holidayManager: HolidayManager
    ): DayPayResult
    
    /**
     * Beräknar OB-tillägg för specifik tidsperiod
     */
    fun calculateObPay(
        startTime: LocalTime,
        endTime: LocalTime,
        date: LocalDate,
        workplace: WorkplaceType,
        hourlyRate: Double,
        obRates: Map<String, Double>,
        holidayManager: HolidayManager
    ): ObPayResult
    
    /**
     * Beräknar automatiska raster enligt Arbetsmiljöverket
     */
    fun calculateAutomaticBreaks(workingMinutes: Int): Int
    
    /**
     * Beräknar skatt och nettolön
     */
    fun calculateTaxAndNet(grossPay: Double, taxRate: Double): TaxCalculation
}

data class DayPayResult(
    val basePay: Double,
    val obPay: Double,
    val totalGrossPay: Double,
    val obBreakdown: Map<String, Double>, // Detaljerad OB-uppdelning
    val effectiveHourlyRate: Double
)

data class ObPayResult(
    val totalObPay: Double,
    val breakdown: Map<String, ObPeriod>
)

data class ObPeriod(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val rate: Double,
    val amount: Double,
    val type: String
)

data class TaxCalculation(
    val grossPay: Double,
    val taxAmount: Double,
    val netPay: Double,
    val vacationPay: Double // 12% enligt kollektivavtal
)
```

### OB-Beräkning Exempel

```kotlin
// Exempel: Beräkna OB för helgarbete
val timeEntry = TimeEntry(
    date = LocalDate.of(2025, 12, 25), // Juldagen
    startTime = LocalTime.of(10, 0),
    endTime = LocalTime.of(16, 0),
    workplace = WorkplaceType.BUTIK,
    contractLevel = ContractLevel.LEVEL_18_YEAR
)

val result = PayCalculator.calculateDayPay(timeEntry, settings, holidayManager)
// result.obPay = grundlön * arbetstimmar * 1.0 (100% OB för helgdag)
```

## 📅 HolidayManager API

Hanterar svenska helgdagar och företagsspecifika lediga dagar.

```kotlin
class HolidayManager(
    private val customHolidays: List<LocalDate> = emptyList()
) {
    /**
     * Kontrollerar om datum är helgdag
     */
    fun isHoliday(date: LocalDate): Boolean
    
    /**
     * Kontrollerar om datum är svensk helgdag
     */
    fun isSwedishHoliday(date: LocalDate): Boolean
    
    /**
     * Kontrollerar om datum är företagsspecifik helgdag
     */
    fun isCustomHoliday(date: LocalDate): Boolean
    
    /**
     * Hämtar alla helgdagar för ett år
     */
    fun getHolidaysForYear(year: Int): List<Holiday>
    
    /**
     * Hämtar helgdagsinformation för specifikt datum
     */
    fun getHolidayInfo(date: LocalDate): Holiday?
}

data class Holiday(
    val date: LocalDate,
    val name: String,
    val type: HolidayType
)

enum class HolidayType {
    SWEDISH_HOLIDAY,    // Officiell svensk helgdag
    CUSTOM_HOLIDAY      // Företagsspecifik helgdag
}
```

### Svenska Helgdagar Implementation

```kotlin
private fun calculateSwedishHolidays(year: Int): List<Holiday> {
    val holidays = mutableListOf<Holiday>()
    
    // Fasta helgdagar
    holidays.add(Holiday(LocalDate.of(year, 1, 1), "Nyårsdagen", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(LocalDate.of(year, 1, 6), "Trettondedag jul", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(LocalDate.of(year, 5, 1), "Första maj", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(LocalDate.of(year, 6, 6), "Nationaldagen", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(LocalDate.of(year, 12, 24), "Julafton", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(LocalDate.of(year, 12, 25), "Juldagen", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(LocalDate.of(year, 12, 31), "Nyårsafton", HolidayType.SWEDISH_HOLIDAY))
    
    // Rörliga helgdagar (baserat på påsk)
    val easter = calculateEaster(year)
    holidays.add(Holiday(easter.minusDays(2), "Långfredag", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(easter.plusDays(1), "Annandag påsk", HolidayType.SWEDISH_HOLIDAY))
    holidays.add(Holiday(easter.plusDays(39), "Kristi himmelsfärd", HolidayType.SWEDISH_HOLIDAY))
    
    // Midsommar (första lördagen efter 19 juni)
    val midsommarEve = LocalDate.of(year, 6, 19).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY))
    holidays.add(Holiday(midsommarEve, "Midsommarafton", HolidayType.SWEDISH_HOLIDAY))
    
    // Alla helgons dag (första lördagen efter 30 oktober)
    val allSaintsDay = LocalDate.of(year, 10, 31).with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
    holidays.add(Holiday(allSaintsDay, "Alla helgons dag", HolidayType.SWEDISH_HOLIDAY))
    
    return holidays.sortedBy { it.date }
}
```

## 💾 Data Persistence API

### FileManager
Hanterar export och import av tidsrapportdata.

```kotlin
class FileManager(private val context: Context) {
    
    /**
     * Exporterar data till JSON
     */
    suspend fun exportToJson(
        timeEntries: List<TimeEntry>,
        filename: String = "arbetstid_backup_${getCurrentDate()}.json"
    ): Result<Uri>
    
    /**
     * Importerar data från JSON
     */
    suspend fun importFromJson(uri: Uri): Result<List<TimeEntry>>
    
    /**
     * Exporterar till Excel-kompatibel CSV
     */
    suspend fun exportToCsv(
        timeEntries: List<TimeEntry>,
        filename: String = "arbetstid_rapport_${getCurrentDate()}.csv"
    ): Result<Uri>
    
    /**
     * Skapar månadsrapport
     */
    suspend fun createMonthlyReport(
        timeEntries: List<TimeEntry>,
        month: YearMonth
    ): Result<Uri>
}

// Data format för JSON export/import
data class BackupData(
    val version: String = "1.1.0",
    val exportDate: String,
    val timeEntries: List<TimeEntry>,
    val settings: Settings,
    val customHolidays: List<LocalDate>
)
```

### BackupManager
Google Drive integration för molnlagring.

```kotlin
class BackupManager(
    private val context: Context,
    private val googleSignInClient: GoogleSignInClient
) {
    /**
     * Skapar backup till Google Drive
     */
    suspend fun createBackup(
        timeEntries: List<TimeEntry>,
        settings: Settings
    ): Result<String> // Returnerar backup-ID
    
    /**
     * Återställer från Google Drive backup
     */
    suspend fun restoreBackup(backupId: String): Result<BackupData>
    
    /**
     * Listar tillgängliga backups
     */
    suspend fun listBackups(): Result<List<BackupInfo>>
    
    /**
     * Raderar gammal backup
     */
    suspend fun deleteBackup(backupId: String): Result<Unit>
}

data class BackupInfo(
    val id: String,
    val name: String,
    val createdDate: LocalDateTime,
    val size: Long
)
```

## 📊 Statistics API

### StatisticsCalculator
Beräknar olika typer av statistik och rapporter.

```kotlin
object StatisticsCalculator {
    
    /**
     * Beräknar periodstatistik
     */
    fun calculatePeriodStatistics(
        timeEntries: List<TimeEntry>,
        startDate: LocalDate,
        endDate: LocalDate,
        settings: Settings,
        holidayManager: HolidayManager
    ): PeriodStatistics
    
    /**
     * Beräknar månadsstatistik
     */
    fun calculateMonthlyStatistics(
        timeEntries: List<TimeEntry>,
        month: YearMonth,
        settings: Settings,
        holidayManager: HolidayManager
    ): MonthlyStatistics
    
    /**
     * Beräknar veckostatistik
     */
    fun calculateWeeklyStatistics(
        timeEntries: List<TimeEntry>,
        week: WeekFields,
        settings: Settings,
        holidayManager: HolidayManager
    ): WeeklyStatistics
}

data class PeriodStatistics(
    val totalHours: Double,
    val totalBasePay: Double,
    val totalObPay: Double,
    val totalGrossPay: Double,
    val totalNetPay: Double,
    val vacationPay: Double,
    val averageHoursPerDay: Double,
    val averagePayPerHour: Double,
    val obPercentage: Double,
    val dayStatistics: List<DayStatistic>
)

data class MonthlyStatistics(
    val month: YearMonth,
    val workingDays: Int,
    val totalHours: Double,
    val expectedHours: Double, // Baserat på standard arbetsvecka
    val overtime: Double,
    val weeklyBreakdown: List<WeeklyStatistics>
) : PeriodStatistics

data class DayStatistic(
    val date: LocalDate,
    val hours: Double,
    val basePay: Double,
    val obPay: Double,
    val totalPay: Double,
    val isHoliday: Boolean,
    val workplace: WorkplaceType
)
```

## 🎨 UI Components API

### Core Composables

```kotlin
/**
 * Huvudkomponent för att lägga till arbetsdagar
 */
@Composable
fun AddDayDialog(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onSave: (TimeEntry) -> Unit,
    templates: List<WorkShiftTemplate> = emptyList(),
    settings: Settings
)

/**
 * Interaktiv kalendervy
 */
@Composable
fun CalendarView(
    selectedDate: LocalDate?,
    timeEntries: List<TimeEntry>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    modifier: Modifier = Modifier
)

/**
 * Sammanfattningskort för totaler
 */
@Composable
fun TotalSummaryCard(
    statistics: PeriodStatistics,
    period: String,
    modifier: Modifier = Modifier
)

/**
 * Kort för individuell tidsregistrering
 */
@Composable
fun TimeEntryCard(
    timeEntry: TimeEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
)
```

### Navigation Structure

```kotlin
sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavigationItem("home", Icons.Filled.Home, "Hem")
    object Statistics : NavigationItem("statistics", Icons.Filled.Analytics, "Statistik")
    object Templates : NavigationItem("templates", Icons.Filled.ContentCopy, "Mallar")
    object WeeklySchedule : NavigationItem("schedule", Icons.Filled.Schedule, "Veckoschema")
    object Export : NavigationItem("export", Icons.Filled.FileDownload, "Export")
    object Settings : NavigationItem("settings", Icons.Filled.Settings, "Inställningar")
}

@Composable
fun TimeReportNavigation(
    navController: NavHostController,
    timeEntries: List<TimeEntry>,
    settings: Settings,
    onUpdateSettings: (Settings) -> Unit
)
```

## 🔧 Utility Classes

### TimeCalculationUtils
Hjälpfunktioner för tidsberäkningar.

```kotlin
object TimeCalculationUtils {
    
    /**
     * Validerar arbetstider
     */
    fun validateTimeEntry(
        startTime: LocalTime,
        endTime: LocalTime,
        breakMinutes: Int
    ): ValidationResult
    
    /**
     * Beräknar arbetstid över midnatt
     */
    fun calculateOvernightWork(
        startTime: LocalTime,
        endTime: LocalTime
    ): Pair<Int, Boolean> // (total minuter, över midnatt)
    
    /**
     * Formaterar tid för visning
     */
    fun formatTime(minutes: Int): String // "8:30"
    fun formatHours(hours: Double): String // "8,5 h"
    fun formatCurrency(amount: Double): String // "1 234,56 kr"
    
    /**
     * Parsar tid från sträng
     */
    fun parseTimeString(timeString: String): LocalTime?
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)
```

### Version Management

```kotlin
object VersionManager {
    const val VERSION_CODE = 127
    const val VERSION_NAME = "1.1.127"
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 34
    
    /**
     * Kontrollerar om data är kompatibel med aktuell version
     */
    fun isDataCompatible(dataVersion: String): Boolean
    
    /**
     * Migrerar data från äldre versioner
     */
    fun migrateData(data: Any, fromVersion: String): Any
}
```

## 🚀 Performance Considerations

### Memory Management
```kotlin
// Använd sealed classes för begränsade tillstånd
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<TimeEntry>) : UiState()
    data class Error(val message: String) : UiState()
}

// Lazy initialization för stora datasets
class TimeEntryRepository {
    private val cache: Map<String, TimeEntry> by lazy {
        loadTimeEntries().associateBy { it.id }
    }
}
```

### Database Considerations
```kotlin
// För framtida Room/SQLite implementation
@Entity(tableName = "time_entries")
data class TimeEntryEntity(
    @PrimaryKey val id: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val breakMinutes: Int,
    val workplace: String,
    val description: String
)

@Dao
interface TimeEntryDao {
    @Query("SELECT * FROM time_entries WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getEntriesInPeriod(startDate: String, endDate: String): List<TimeEntryEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: TimeEntryEntity)
    
    @Delete
    suspend fun deleteEntry(entry: TimeEntryEntity)
}
```

## 📝 Error Handling

### Custom Exceptions
```kotlin
sealed class TimeReportException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class InvalidTimeEntry(message: String) : TimeReportException(message)
    class CalculationError(message: String, cause: Throwable) : TimeReportException(message, cause)
    class FileOperationError(message: String, cause: Throwable) : TimeReportException(message, cause)
    class BackupError(message: String, cause: Throwable) : TimeReportException(message, cause)
}

// Användning
try {
    val result = PayCalculator.calculateDayPay(timeEntry, settings, holidayManager)
} catch (e: TimeReportException.CalculationError) {
    Log.e("PayCalculator", "Calculation failed", e)
    // Visa användarvänligt felmeddelande
}
```

## 🔧 Testing API

### Test Utilities
```kotlin
object TestDataFactory {
    fun createTimeEntry(
        date: LocalDate = LocalDate.now(),
        startTime: LocalTime = LocalTime.of(9, 0),
        endTime: LocalTime = LocalTime.of(17, 0),
        workplace: WorkplaceType = WorkplaceType.BUTIK
    ): TimeEntry
    
    fun createSettings(
        contractLevel: ContractLevel = ContractLevel.LEVEL_18_YEAR,
        taxRate: Double = 0.30
    ): Settings
    
    fun createHolidayManager(
        customHolidays: List<LocalDate> = emptyList()
    ): HolidayManager
}

// Test Extensions
fun TimeEntry.withOvertime(): TimeEntry = copy(endTime = LocalTime.of(22, 0))
fun TimeEntry.withHolidayWork(): TimeEntry = copy(date = LocalDate.of(2025, 12, 25))
```

---

**📚 Detta kompletterar API-dokumentationen för Arbetstidskalkylator.**

*För användarinstruktioner, se [User Guide](User-Guide)*
*För utvecklingsmiljö, se [Developer Setup](Developer-Setup)*