package se.thomas.arbetstidskalkylator.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0012\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bJ\"\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fJ\'\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\'\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u001b\u0010\u0018\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0019\u001a\u00020\u001aH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u0019\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001e"}, d2 = {"Lse/thomas/arbetstidskalkylator/repository/TimeEntryRepository;", "", "timeEntryDao", "Lse/thomas/arbetstidskalkylator/data/TimeEntryDao;", "(Lse/thomas/arbetstidskalkylator/data/TimeEntryDao;)V", "deleteTimeEntry", "", "timeEntry", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "(Lse/thomas/arbetstidskalkylator/data/TimeEntry;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTimeEntries", "Lkotlinx/coroutines/flow/Flow;", "", "getTimeEntriesByDateRange", "startDate", "Ljava/util/Date;", "endDate", "getTimeEntriesByDateRangeSync", "(Ljava/util/Date;Ljava/util/Date;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTimeEntriesForMonth", "month", "", "year", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTimeEntryById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertTimeEntry", "updateTimeEntry", "app_debug"})
public final class TimeEntryRepository {
    @org.jetbrains.annotations.NotNull
    private final se.thomas.arbetstidskalkylator.data.TimeEntryDao timeEntryDao = null;
    
    public TimeEntryRepository(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntryDao timeEntryDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> getAllTimeEntries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> getTimeEntriesByDateRange(@org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteTimeEntry(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getTimeEntryById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super se.thomas.arbetstidskalkylator.data.TimeEntry> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getTimeEntriesByDateRangeSync(@org.jetbrains.annotations.NotNull
    java.util.Date startDate, @org.jetbrains.annotations.NotNull
    java.util.Date endDate, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getTimeEntriesForMonth(int month, int year, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry>> $completion) {
        return null;
    }
}