package se.thomas.arbetstidskalkylator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B=\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0003JA\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u001eH\u00d6\u0001J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r\u00a8\u0006!"}, d2 = {"Lse/thomas/arbetstidskalkylator/data/Settings;", "", "id", "", "baseHourlyRate", "", "taxPercentage", "vacationPayPercentage", "overtimeRates", "", "Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "(JDDDLjava/util/List;)V", "getBaseHourlyRate", "()D", "getId", "()J", "getOvertimeRates", "()Ljava/util/List;", "getTaxPercentage", "getVacationPayPercentage", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
@androidx.room.Entity(tableName = "settings")
public final class Settings {
    @androidx.room.PrimaryKey
    private final long id = 0L;
    private final double baseHourlyRate = 0.0;
    private final double taxPercentage = 0.0;
    private final double vacationPayPercentage = 0.0;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates = null;
    
    public Settings(long id, double baseHourlyRate, double taxPercentage, double vacationPayPercentage, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final double getBaseHourlyRate() {
        return 0.0;
    }
    
    public final double getTaxPercentage() {
        return 0.0;
    }
    
    public final double getVacationPayPercentage() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> getOvertimeRates() {
        return null;
    }
    
    public Settings() {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final se.thomas.arbetstidskalkylator.data.Settings copy(long id, double baseHourlyRate, double taxPercentage, double vacationPayPercentage, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}