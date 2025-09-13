package se.thomas.arbetstidskalkylator.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J\u0013\u0010\u000f\u001a\u0004\u0018\u00010\tH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0007\u001a\u00020\tJ\u0006\u0010\u0014\u001a\u00020\u0012R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0015"}, d2 = {"Lse/thomas/arbetstidskalkylator/viewmodel/SettingsViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "repository", "Lse/thomas/arbetstidskalkylator/repository/SettingsRepository;", "settings", "Landroidx/lifecycle/LiveData;", "Lse/thomas/arbetstidskalkylator/data/Settings;", "getSettings", "()Landroidx/lifecycle/LiveData;", "createDefaultOvertimeRates", "", "Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "getSettingsSync", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initializeDefaultSettingsIfNeeded", "Lkotlinx/coroutines/Job;", "saveSettings", "setupStandardOvertimeRates", "app_debug"})
public final class SettingsViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final se.thomas.arbetstidskalkylator.repository.SettingsRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<se.thomas.arbetstidskalkylator.data.Settings> settings = null;
    
    public SettingsViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<se.thomas.arbetstidskalkylator.data.Settings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job saveSettings(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.Settings settings) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getSettingsSync(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super se.thomas.arbetstidskalkylator.data.Settings> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job initializeDefaultSettingsIfNeeded() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setupStandardOvertimeRates() {
        return null;
    }
    
    private final java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> createDefaultOvertimeRates() {
        return null;
    }
}