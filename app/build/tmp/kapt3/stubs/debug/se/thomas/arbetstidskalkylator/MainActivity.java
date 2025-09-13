package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002J\b\u0010\u0016\u001a\u00020\u0014H\u0002J\b\u0010\u0017\u001a\u00020\u0014H\u0002J\b\u0010\u0018\u001a\u00020\u0014H\u0002J\u0012\u0010\u0019\u001a\u00020\u00142\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0014H\u0002J\b\u0010\u001d\u001a\u00020\u0014H\u0002J\u0010\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\fH\u0002J\u0010\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\fH\u0002J\b\u0010\"\u001a\u00020\u0014H\u0002J\u0016\u0010#\u001a\u00020\u00142\f\u0010$\u001a\b\u0012\u0004\u0012\u00020&0%H\u0002J\b\u0010\'\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lse/thomas/arbetstidskalkylator/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lse/thomas/arbetstidskalkylator/databinding/ActivityMainBinding;", "breakEndDateTime", "Ljava/util/Calendar;", "breakStartDateTime", "dateTimeFormat", "Ljava/text/SimpleDateFormat;", "endDateTime", "isSettingCustomBreak", "", "settingsViewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/SettingsViewModel;", "startDateTime", "timeFormat", "viewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/TimeEntryViewModel;", "clearCustomBreak", "", "clearForm", "initializeDateTimes", "observeSettings", "observeTimeEntries", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "saveTimeEntry", "setupUI", "showBreakTimePicker", "isBreakStart", "showDateTimePicker", "isStartTime", "updateBreakButtons", "updateSummary", "timeEntries", "", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "updateTimeButtons", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private se.thomas.arbetstidskalkylator.databinding.ActivityMainBinding binding;
    private se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel viewModel;
    private se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel settingsViewModel;
    @org.jetbrains.annotations.NotNull
    private java.util.Calendar startDateTime;
    @org.jetbrains.annotations.NotNull
    private java.util.Calendar endDateTime;
    @org.jetbrains.annotations.Nullable
    private java.util.Calendar breakStartDateTime;
    @org.jetbrains.annotations.Nullable
    private java.util.Calendar breakEndDateTime;
    private boolean isSettingCustomBreak = false;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat dateTimeFormat = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat timeFormat = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initializeDateTimes() {
    }
    
    private final void setupUI() {
    }
    
    private final void showDateTimePicker(boolean isStartTime) {
    }
    
    private final void updateTimeButtons() {
    }
    
    private final void showBreakTimePicker(boolean isBreakStart) {
    }
    
    private final void updateBreakButtons() {
    }
    
    private final void clearCustomBreak() {
    }
    
    private final void saveTimeEntry() {
    }
    
    private final void clearForm() {
    }
    
    private final void observeTimeEntries() {
    }
    
    private final void observeSettings() {
    }
    
    private final void updateSummary(java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries) {
    }
}