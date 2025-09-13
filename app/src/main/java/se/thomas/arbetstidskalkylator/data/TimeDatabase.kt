package se.thomas.arbetstidskalkylator.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [TimeEntry::class, Settings::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TimeDatabase : RoomDatabase() {
    abstract fun timeEntryDao(): TimeEntryDao
    abstract fun settingsDao(): SettingsDao
    
    companion object {
        @Volatile
        private var INSTANCE: TimeDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Skapa settings tabellen
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `settings` (
                        `id` INTEGER NOT NULL, 
                        `baseHourlyRate` REAL NOT NULL, 
                        `taxPercentage` REAL NOT NULL, 
                        `vacationPayPercentage` REAL NOT NULL, 
                        `overtimeRates` TEXT NOT NULL, 
                        PRIMARY KEY(`id`)
                    )
                """)
            }
        }
        
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Lägg till nya rastfält i time_entries tabellen
                db.execSQL("ALTER TABLE time_entries ADD COLUMN breakMinutes INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE time_entries ADD COLUMN customBreakStart INTEGER")
                db.execSQL("ALTER TABLE time_entries ADD COLUMN customBreakEnd INTEGER")
            }
        }
        
        fun getDatabase(context: Context): TimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeDatabase::class.java,
                    "time_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .fallbackToDestructiveMigration() // Som backup om migration misslyckas
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}