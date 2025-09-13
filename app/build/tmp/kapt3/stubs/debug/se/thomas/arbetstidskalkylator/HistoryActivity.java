package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0018\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002J\b\u0010\u0012\u001a\u00020\fH\u0002J\b\u0010\u0013\u001a\u00020\fH\u0002J\u0012\u0010\u0014\u001a\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\fH\u0002J\b\u0010\u001a\u001a\u00020\fH\u0002J\u0010\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lse/thomas/arbetstidskalkylator/HistoryActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lse/thomas/arbetstidskalkylator/TimeEntryAdapter;", "binding", "Lse/thomas/arbetstidskalkylator/databinding/ActivityHistoryBinding;", "settingsViewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/SettingsViewModel;", "viewModel", "Lse/thomas/arbetstidskalkylator/viewmodel/TimeEntryViewModel;", "exportAllToPDF", "", "exportCSV", "exportMonthToPDF", "month", "", "year", "exportSummary", "observeTimeEntries", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onSupportNavigateUp", "", "setupNavigationButtons", "setupUI", "showDeleteDialog", "timeEntry", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "showMonthSelectionDialog", "app_debug"})
public final class HistoryActivity extends androidx.appcompat.app.AppCompatActivity {
    private se.thomas.arbetstidskalkylator.databinding.ActivityHistoryBinding binding;
    private se.thomas.arbetstidskalkylator.viewmodel.TimeEntryViewModel viewModel;
    private se.thomas.arbetstidskalkylator.viewmodel.SettingsViewModel settingsViewModel;
    private se.thomas.arbetstidskalkylator.TimeEntryAdapter adapter;
    
    public HistoryActivity() {
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
    
    private final void showDeleteDialog(se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry) {
    }
    
    private final void exportCSV() {
    }
    
    private final void exportSummary() {
    }
    
    private final void exportAllToPDF() {
    }
    
    private final void showMonthSelectionDialog() {
    }
    
    private final void exportMonthToPDF(int month, int year) {
    }
    
    @java.lang.Override
    public boolean onSupportNavigateUp() {
        return false;
    }
    
    private final void setupNavigationButtons() {
    }
}