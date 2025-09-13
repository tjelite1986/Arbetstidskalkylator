package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001:\u0002/0B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0002J\u001e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00122\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0011H\u0002J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0002J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020 H\u0002J\b\u0010!\u001a\u00020 H\u0002J\u0012\u0010\"\u001a\u00020 2\b\u0010#\u001a\u0004\u0018\u00010$H\u0014J\b\u0010%\u001a\u00020\u000bH\u0016J\b\u0010&\u001a\u00020 H\u0002J\b\u0010\'\u001a\u00020 H\u0002J\b\u0010(\u001a\u00020 H\u0002J\b\u0010)\u001a\u00020 H\u0002J\u0016\u0010*\u001a\u00020 2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0002J\b\u0010+\u001a\u00020 H\u0002J \u0010,\u001a\u00020 2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0002J \u0010-\u001a\u00020 2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0002J$\u0010.\u001a\u00020 2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00061"}, d2 = {"Lse/thomas/arbetstidskalkylator/StatisticsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lse/thomas/arbetstidskalkylator/databinding/ActivityStatisticsBinding;", "selectedCalendar", "Ljava/util/Calendar;", "kotlin.jvm.PlatformType", "settingsViewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/SettingsViewModel;", "showAllTime", "", "viewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/TimeEntryViewModel;", "calculateAllMonthsSummary", "Lse/thomas/arbetstidskalkylator/StatisticsActivity$AllMonthsSummary;", "timeEntries", "", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "settings", "Lse/thomas/arbetstidskalkylator/data/Settings;", "calculateEntryBreakdown", "Lse/thomas/arbetstidskalkylator/StatisticsActivity$SalaryBreakdown;", "entry", "overtimeRates", "Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "filterEntriesByMonth", "formatHoursWithBothFormats", "", "hours", "", "hideEmptyState", "", "observeTimeEntries", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "refreshStatistics", "setupNavigationButtons", "setupUI", "showEmptyState", "updateHoursDistribution", "updatePeriodDisplay", "updateSalaryBreakdown", "updateStatistics", "updateWeeklyMonthlyStats", "AllMonthsSummary", "SalaryBreakdown", "app_debug"})
public final class StatisticsActivity extends androidx.appcompat.app.AppCompatActivity {
    private se.thomas.arbetstidskalkylator.databinding.ActivityStatisticsBinding binding;
    private se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel viewModel;
    private se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel settingsViewModel;
    private java.util.Calendar selectedCalendar;
    private boolean showAllTime = false;
    
    public StatisticsActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void observeTimeEntries() {
    }
    
    private final void refreshStatistics() {
    }
    
    private final java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> filterEntriesByMonth(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries) {
        return null;
    }
    
    private final void updatePeriodDisplay() {
    }
    
    private final void updateStatistics(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, se.thomas.arbetstidskalkylator.data.Settings settings) {
    }
    
    private final void updateWeeklyMonthlyStats(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
    }
    
    private final void updateHoursDistribution(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries) {
    }
    
    private final void updateSalaryBreakdown(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, se.thomas.arbetstidskalkylator.data.Settings settings) {
    }
    
    private final se.thomas.arbetstidskalkylator.StatisticsActivity.AllMonthsSummary calculateAllMonthsSummary(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, se.thomas.arbetstidskalkylator.data.Settings settings) {
        return null;
    }
    
    private final se.thomas.arbetstidskalkylator.StatisticsActivity.SalaryBreakdown calculateEntryBreakdown(se.thomas.arbetstidskalkylator.data.TimeEntry entry, java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return null;
    }
    
    private final java.lang.String formatHoursWithBothFormats(double hours) {
        return null;
    }
    
    private final void showEmptyState() {
    }
    
    private final void hideEmptyState() {
    }
    
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    private final void setupNavigationButtons() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\u0019"}, d2 = {"Lse/thomas/arbetstidskalkylator/StatisticsActivity$AllMonthsSummary;", "", "totalGrossPay", "", "totalTax", "totalHours", "totalNetPay", "(DDDD)V", "getTotalGrossPay", "()D", "getTotalHours", "getTotalNetPay", "getTotalTax", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class AllMonthsSummary {
        private final double totalGrossPay = 0.0;
        private final double totalTax = 0.0;
        private final double totalHours = 0.0;
        private final double totalNetPay = 0.0;
        
        public AllMonthsSummary(double totalGrossPay, double totalTax, double totalHours, double totalNetPay) {
            super();
        }
        
        public final double getTotalGrossPay() {
            return 0.0;
        }
        
        public final double getTotalTax() {
            return 0.0;
        }
        
        public final double getTotalHours() {
            return 0.0;
        }
        
        public final double getTotalNetPay() {
            return 0.0;
        }
        
        public AllMonthsSummary() {
            super();
        }
        
        public final double component1() {
            return 0.0;
        }
        
        public final double component2() {
            return 0.0;
        }
        
        public final double component3() {
            return 0.0;
        }
        
        public final double component4() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final se.thomas.arbetstidskalkylator.StatisticsActivity.AllMonthsSummary copy(double totalGrossPay, double totalTax, double totalHours, double totalNetPay) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u001b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003JY\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\t\u0010#\u001a\u00020$H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\r\u00a8\u0006%"}, d2 = {"Lse/thomas/arbetstidskalkylator/StatisticsActivity$SalaryBreakdown;", "", "regularHours", "", "regularPay", "ob50Hours", "ob50Pay", "ob70Hours", "ob70Pay", "ob100Hours", "ob100Pay", "(DDDDDDDD)V", "getOb100Hours", "()D", "getOb100Pay", "getOb50Hours", "getOb50Pay", "getOb70Hours", "getOb70Pay", "getRegularHours", "getRegularPay", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class SalaryBreakdown {
        private final double regularHours = 0.0;
        private final double regularPay = 0.0;
        private final double ob50Hours = 0.0;
        private final double ob50Pay = 0.0;
        private final double ob70Hours = 0.0;
        private final double ob70Pay = 0.0;
        private final double ob100Hours = 0.0;
        private final double ob100Pay = 0.0;
        
        public SalaryBreakdown(double regularHours, double regularPay, double ob50Hours, double ob50Pay, double ob70Hours, double ob70Pay, double ob100Hours, double ob100Pay) {
            super();
        }
        
        public final double getRegularHours() {
            return 0.0;
        }
        
        public final double getRegularPay() {
            return 0.0;
        }
        
        public final double getOb50Hours() {
            return 0.0;
        }
        
        public final double getOb50Pay() {
            return 0.0;
        }
        
        public final double getOb70Hours() {
            return 0.0;
        }
        
        public final double getOb70Pay() {
            return 0.0;
        }
        
        public final double getOb100Hours() {
            return 0.0;
        }
        
        public final double getOb100Pay() {
            return 0.0;
        }
        
        public SalaryBreakdown() {
            super();
        }
        
        public final double component1() {
            return 0.0;
        }
        
        public final double component2() {
            return 0.0;
        }
        
        public final double component3() {
            return 0.0;
        }
        
        public final double component4() {
            return 0.0;
        }
        
        public final double component5() {
            return 0.0;
        }
        
        public final double component6() {
            return 0.0;
        }
        
        public final double component7() {
            return 0.0;
        }
        
        public final double component8() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final se.thomas.arbetstidskalkylator.StatisticsActivity.SalaryBreakdown copy(double regularHours, double regularPay, double ob50Hours, double ob50Pay, double ob70Hours, double ob70Pay, double ob100Hours, double ob100Pay) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
}