package se.thomas.arbetstidskalkylator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\u000b\n\u0002\b\n\b\u0086\b\u0018\u00002\u00020\u0001BY\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010 \u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\bH\u00c6\u0003J\t\u0010#\u001a\u00020\nH\u00c6\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003Jf\u0010&\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\b\b\u0002\u0010\u000e\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\'J\u0013\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0006\u0010+\u001a\u00020\nJ\u0006\u0010,\u001a\u00020\nJ\t\u0010-\u001a\u00020\u0003H\u00d6\u0001J\u001e\u0010.\u001a\u00020)2\u0006\u0010/\u001a\u00020\u00032\u0006\u00100\u001a\u00020\u00032\u0006\u00101\u001a\u00020\rJ\t\u00102\u001a\u00020\nH\u00d6\u0001R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0016\u00a8\u00063"}, d2 = {"Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "", "startHour", "", "startMinute", "endHour", "endMinute", "multiplier", "", "name", "", "dayTypes", "", "Lse/thomas/arbetstidskalkylator/data/DayType;", "priority", "(IILjava/lang/Integer;IDLjava/lang/String;Ljava/util/List;I)V", "getDayTypes", "()Ljava/util/List;", "getEndHour", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getEndMinute", "()I", "getMultiplier", "()D", "getName", "()Ljava/lang/String;", "getPriority", "getStartHour", "getStartMinute", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "(IILjava/lang/Integer;IDLjava/lang/String;Ljava/util/List;I)Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "equals", "", "other", "getDayTypeString", "getTimeString", "hashCode", "isActiveAt", "hour", "minute", "dayType", "toString", "app_debug"})
public final class OvertimeRate {
    private final int startHour = 0;
    private final int startMinute = 0;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer endHour = null;
    private final int endMinute = 0;
    private final double multiplier = 0.0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<se.thomas.arbetstidskalkylator.data.DayType> dayTypes = null;
    private final int priority = 0;
    
    public OvertimeRate(int startHour, int startMinute, @org.jetbrains.annotations.Nullable
    java.lang.Integer endHour, int endMinute, double multiplier, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.util.List<? extends se.thomas.arbetstidskalkylator.data.DayType> dayTypes, int priority) {
        super();
    }
    
    public final int getStartHour() {
        return 0;
    }
    
    public final int getStartMinute() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getEndHour() {
        return null;
    }
    
    public final int getEndMinute() {
        return 0;
    }
    
    public final double getMultiplier() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<se.thomas.arbetstidskalkylator.data.DayType> getDayTypes() {
        return null;
    }
    
    public final int getPriority() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTimeString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDayTypeString() {
        return null;
    }
    
    public final boolean isActiveAt(int hour, int minute, @org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.data.DayType dayType) {
        return false;
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<se.thomas.arbetstidskalkylator.data.DayType> component7() {
        return null;
    }
    
    public final int component8() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final se.thomas.arbetstidskalkylator.data.OvertimeRate copy(int startHour, int startMinute, @org.jetbrains.annotations.Nullable
    java.lang.Integer endHour, int endMinute, double multiplier, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.util.List<? extends se.thomas.arbetstidskalkylator.data.DayType> dayTypes, int priority) {
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