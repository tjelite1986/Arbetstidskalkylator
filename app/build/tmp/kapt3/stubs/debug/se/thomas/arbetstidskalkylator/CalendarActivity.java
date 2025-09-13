package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0014H\u0002J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0013H\u0002J\u0016\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\u001c\u001a\u00020\bH\u0002J\b\u0010\u001d\u001a\u00020\u0018H\u0002J\u0012\u0010\u001e\u001a\u00020\u00182\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0014J\b\u0010!\u001a\u00020\"H\u0016J\u0010\u0010#\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u000fH\u0002J\b\u0010%\u001a\u00020\u0018H\u0002J\b\u0010&\u001a\u00020\u0018H\u0002J\b\u0010\'\u001a\u00020\u0018H\u0002J\b\u0010(\u001a\u00020\u0018H\u0002J\b\u0010)\u001a\u00020\u0018H\u0002J\b\u0010*\u001a\u00020\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lse/thomas/arbetstidskalkylator/CalendarActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lse/thomas/arbetstidskalkylator/databinding/ActivityCalendarBinding;", "calendarAdapter", "Lse/thomas/arbetstidskalkylator/CalendarAdapter;", "currentCalendar", "Ljava/util/Calendar;", "kotlin.jvm.PlatformType", "dateFormat", "Ljava/text/SimpleDateFormat;", "displayDateFormat", "monthYearFormat", "selectedDay", "Lse/thomas/arbetstidskalkylator/CalendarDay;", "settingsViewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/SettingsViewModel;", "timeEntries", "", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "viewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/TimeEntryViewModel;", "editTimeEntry", "", "timeEntry", "generateCalendarDays", "getTimeEntriesForDate", "calendar", "observeTimeEntries", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "openEditTimeEntryForNewEntry", "day", "setupCalendar", "setupNavigationButtons", "setupUI", "updateCalendarView", "updateDateInfo", "updateMonthYearDisplay", "app_debug"})
public final class CalendarActivity extends androidx.appcompat.app.AppCompatActivity {
    private se.thomas.arbetstidskalkylator.databinding.ActivityCalendarBinding binding;
    private se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel viewModel;
    private se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel settingsViewModel;
    private se.thomas.arbetstidskalkylator.CalendarAdapter calendarAdapter;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat dateFormat = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat displayDateFormat = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat monthYearFormat = null;
    private java.util.Calendar currentCalendar;
    @org.jetbrains.annotations.Nullable
    private se.thomas.arbetstidskalkylator.CalendarDay selectedDay;
    @org.jetbrains.annotations.NotNull
    private java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries;
    
    public CalendarActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void setupNavigationButtons() {
    }
    
    private final void setupCalendar() {
    }
    
    private final void observeTimeEntries() {
    }
    
    private final void updateCalendarView() {
    }
    
    private final java.util.List<se.thomas.arbetstidskalkylator.CalendarDay> generateCalendarDays() {
        return null;
    }
    
    private final java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> getTimeEntriesForDate(java.util.Calendar calendar) {
        return null;
    }
    
    private final void updateMonthYearDisplay() {
    }
    
    private final void updateDateInfo() {
    }
    
    private final void openEditTimeEntryForNewEntry(se.thomas.arbetstidskalkylator.CalendarDay day) {
    }
    
    private final void editTimeEntry(se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry) {
    }
    
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
}