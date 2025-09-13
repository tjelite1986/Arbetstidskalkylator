package se.thomas.arbetstidskalkylator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J$\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\'J\'\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u001b\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0016"}, d2 = {"Lse/thomas/arbetstidskalkylator/data/TimeEntryDao;", "", "deleteTimeEntry", "", "timeEntry", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "(Lse/thomas/arbetstidskalkylator/data/TimeEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTimeEntries", "Lkotlinx/coroutines/flow/Flow;", "", "getTimeEntriesByDateRange", "startDate", "Ljava/util/Date;", "endDate", "getTimeEntriesByDateRangeSync", "(Ljava/util/Date;Ljava/util/Date;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTimeEntryById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertTimeEntry", "updateTimeEntry", "app_debug"})
@androidx.room.Dao
public abstract interface TimeEntryDao {
    
    @androidx.room.Query(value = "SELECT * FROM time_entries ORDER BY startTime DESC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> getAllTimeEntries();
    
    @androidx.room.Query(value = "SELECT * FROM time_entries WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime DESC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> getTimeEntriesByDateRange(@org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate);
    
    @androidx.room.Query(value = "SELECT * FROM time_entries WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTimeEntriesByDateRangeSync(@org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> $completion);
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM time_entries WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTimeEntryById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super se.thomas.arbetstidskalkylator.data.TimeEntry> $completion);
}