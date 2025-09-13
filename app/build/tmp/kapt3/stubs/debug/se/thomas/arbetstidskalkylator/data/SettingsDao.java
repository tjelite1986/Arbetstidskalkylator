package se.thomas.arbetstidskalkylator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\'J\u0013\u0010\b\u001a\u0004\u0018\u00010\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0019\u0010\f\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0007H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2 = {"Lse/thomas/arbetstidskalkylator/data/SettingsDao;", "", "clearSettings", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSettings", "Lkotlinx/coroutines/flow/Flow;", "Lse/thomas/arbetstidskalkylator/data/Settings;", "getSettingsSync", "insertSettings", "settings", "(Lse/thomas/arbetstidskalkylator/data/Settings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSettings", "app_debug"})
@androidx.room.Dao
public abstract interface SettingsDao {
    
    @androidx.room.Query(value = "SELECT * FROM settings WHERE id = 1 LIMIT 1")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<se.thomas.arbetstidskalkylator.data.Settings> getSettings();
    
    @androidx.room.Query(value = "SELECT * FROM settings WHERE id = 1 LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSettingsSync(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super se.thomas.arbetstidskalkylator.data.Settings> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertSettings(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.Settings settings, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.Settings settings, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM settings")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object clearSettings(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}