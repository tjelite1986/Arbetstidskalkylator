package se.thomas.arbetstidskalkylator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lse/thomas/arbetstidskalkylator/data/TimeDatabase;", "Landroidx/room/RoomDatabase;", "()V", "settingsDao", "Lse/thomas/arbetstidskalkylator/data/SettingsDao;", "timeEntryDao", "Lse/thomas/arbetstidskalkylator/data/TimeEntryDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {se.thomas.arbetstidskalkylator.data.TimeEntry.class, se.thomas.arbetstidskalkylator.data.Settings.class}, version = 3, exportSchema = false)
@androidx.room.TypeConverters(value = {se.thomas.arbetstidskalkylator.data.Converters.class})
public abstract class TimeDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private static volatile se.thomas.arbetstidskalkylator.data.TimeDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @org.jetbrains.annotations.NotNull
    private static final androidx.room.migration.Migration MIGRATION_2_3 = null;
    @org.jetbrains.annotations.NotNull
    public static final se.thomas.arbetstidskalkylator.data.TimeDatabase.Companion Companion = null;
    
    public TimeDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract se.thomas.arbetstidskalkylator.data.TimeEntryDao timeEntryDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract se.thomas.arbetstidskalkylator.data.SettingsDao settingsDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lse/thomas/arbetstidskalkylator/data/TimeDatabase$Companion;", "", "()V", "INSTANCE", "Lse/thomas/arbetstidskalkylator/data/TimeDatabase;", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "MIGRATION_2_3", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final se.thomas.arbetstidskalkylator.data.TimeDatabase getDatabase(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}