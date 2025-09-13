package se.thomas.arbetstidskalkylator.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0005\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tJ\u0013\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u0019\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0010"}, d2 = {"Lse/thomas/arbetstidskalkylator/repository/SettingsRepository;", "", "settingsDao", "Lse/thomas/arbetstidskalkylator/data/SettingsDao;", "(Lse/thomas/arbetstidskalkylator/data/SettingsDao;)V", "clearSettings", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSettings", "Lkotlinx/coroutines/flow/Flow;", "Lse/thomas/arbetstidskalkylator/data/Settings;", "getSettingsSync", "saveSettings", "settings", "(Lse/thomas/arbetstidskalkylator/data/Settings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSettings", "app_debug"})
public final class SettingsRepository {
    @org.jetbrains.annotations.NotNull
    private final se.thomas.arbetstidskalkylator.data.SettingsDao settingsDao = null;
    
    public SettingsRepository(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.SettingsDao settingsDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<se.thomas.arbetstidskalkylator.data.Settings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getSettingsSync(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super se.thomas.arbetstidskalkylator.data.Settings> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object saveSettings(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.Settings settings, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.Settings settings, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object clearSettings(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}