package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\u0012\u0010\u0018\u001a\u00020\u00172\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0014J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u0012H\u0002J\b\u0010\u001f\u001a\u00020\u0017H\u0002J\b\u0010 \u001a\u00020\u0017H\u0002J\b\u0010!\u001a\u00020\u0017H\u0002J\b\u0010\"\u001a\u00020\u0017H\u0002J\b\u0010#\u001a\u00020\u0017H\u0002J\b\u0010$\u001a\u00020\u0017H\u0002J\b\u0010%\u001a\u00020\u0017H\u0002J\b\u0010&\u001a\u00020\u0017H\u0002J\b\u0010\'\u001a\u00020\u0017H\u0002J\b\u0010(\u001a\u00020\u0017H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lse/thomas/arbetstidskalkylator/EditTimeEntryActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lse/thomas/arbetstidskalkylator/databinding/ActivityEditTimeEntryBinding;", "breakEndTime", "Ljava/util/Date;", "breakStartTime", "calendar", "Ljava/util/Calendar;", "kotlin.jvm.PlatformType", "dateFormat", "Ljava/text/SimpleDateFormat;", "endTime", "settingsViewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/SettingsViewModel;", "startTime", "timeEntry", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "timeFormat", "viewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/TimeEntryViewModel;", "loadTimeEntry", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "populateFields", "entry", "saveTimeEntry", "setupNavigationButtons", "setupNewEntry", "setupUI", "showBreakEndTimePicker", "showBreakStartTimePicker", "showDatePicker", "showEndTimePicker", "showStartTimePicker", "updateStartEndTimes", "app_debug"})
public final class EditTimeEntryActivity extends androidx.appcompat.app.AppCompatActivity {
    private se.thomas.arbetstidskalkylator.databinding.ActivityEditTimeEntryBinding binding;
    private se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel viewModel;
    private se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel settingsViewModel;
    @org.jetbrains.annotations.Nullable
    private se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry;
    private final java.util.Calendar calendar = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat dateFormat = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat timeFormat = null;
    @org.jetbrains.annotations.Nullable
    private java.util.Date startTime;
    @org.jetbrains.annotations.Nullable
    private java.util.Date endTime;
    @org.jetbrains.annotations.Nullable
    private java.util.Date breakStartTime;
    @org.jetbrains.annotations.Nullable
    private java.util.Date breakEndTime;
    
    public EditTimeEntryActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void loadTimeEntry() {
    }
    
    private final void setupNewEntry() {
    }
    
    private final void populateFields(se.thomas.arbetstidskalkylator.data.TimeEntry entry) {
    }
    
    private final void showDatePicker() {
    }
    
    private final void showStartTimePicker() {
    }
    
    private final void showEndTimePicker() {
    }
    
    private final void updateStartEndTimes() {
    }
    
    private final void showBreakStartTimePicker() {
    }
    
    private final void showBreakEndTimePicker() {
    }
    
    private final void saveTimeEntry() {
    }
    
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    private final void setupNavigationButtons() {
    }
}