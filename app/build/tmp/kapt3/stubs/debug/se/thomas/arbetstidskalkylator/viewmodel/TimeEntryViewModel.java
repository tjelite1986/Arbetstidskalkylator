package se.thomas.arbetstidskalkylator.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J$\u0010\u0012\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0007J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\bJ\"\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aJ\u000e\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\bJ\u000e\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\bR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001e"}, d2 = {"Lse/thomas/arbetstidskalkylator/viewmodel/TimeEntryViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "allTimeEntries", "Landroidx/lifecycle/LiveData;", "", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "getAllTimeEntries", "()Landroidx/lifecycle/LiveData;", "repository", "Lse/thomas/arbetstidskalkylator/repository/TimeEntryRepository;", "getRepository", "()Lse/thomas/arbetstidskalkylator/repository/TimeEntryRepository;", "calculateTotalHours", "", "timeEntries", "calculateTotalPay", "overtimeRates", "Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "deleteTimeEntry", "Lkotlinx/coroutines/Job;", "timeEntry", "getTimeEntriesByDateRange", "startDate", "Ljava/util/Date;", "endDate", "insertTimeEntry", "updateTimeEntry", "app_debug"})
public final class TimeEntryViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final se.thomas.arbetstidskalkylator.repository.TimeEntryRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> allTimeEntries = null;
    
    public TimeEntryViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final se.thomas.arbetstidskalkylator.repository.TimeEntryRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> getAllTimeEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insertTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> getTimeEntriesByDateRange(@org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate) {
        return null;
    }
    
    public final double calculateTotalPay(@org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return 0.0;
    }
    
    public final double calculateTotalHours(@org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries) {
        return 0.0;
    }
}