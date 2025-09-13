package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001BW\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\rJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0011H\u0016J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u0011H\u0016J$\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0013\u001a\u00020\u00112\b\u0010\u0018\u001a\u0004\u0018\u00010\u00172\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0014\u0010\u001b\u001a\u00020\t2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lse/thomas/arbetstidskalkylator/CalendarAdapter;", "Landroid/widget/BaseAdapter;", "context", "Landroid/content/Context;", "days", "", "Lse/thomas/arbetstidskalkylator/CalendarDay;", "onDayClick", "Lkotlin/Function1;", "", "onDayLongClick", "onEditTimeEntry", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "(Landroid/content/Context;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "timeFormat", "Ljava/text/SimpleDateFormat;", "getCount", "", "getItem", "position", "getItemId", "", "getView", "Landroid/view/View;", "convertView", "parent", "Landroid/view/ViewGroup;", "updateDays", "newDays", "app_debug"})
public final class CalendarAdapter extends android.widget.BaseAdapter {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<se.thomas.arbetstidskalkylator.CalendarDay> days;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<se.thomas.arbetstidskalkylator.CalendarDay, kotlin.Unit> onDayClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<se.thomas.arbetstidskalkylator.CalendarDay, kotlin.Unit> onDayLongClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<se.thomas.arbetstidskalkylator.data.TimeEntry, kotlin.Unit> onEditTimeEntry = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat timeFormat = null;
    
    public CalendarAdapter(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.CalendarDay> days, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super se.thomas.arbetstidskalkylator.CalendarDay, kotlin.Unit> onDayClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super se.thomas.arbetstidskalkylator.CalendarDay, kotlin.Unit> onDayLongClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super se.thomas.arbetstidskalkylator.data.TimeEntry, kotlin.Unit> onEditTimeEntry) {
        super();
    }
    
    public final void updateDays(@org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.CalendarDay> newDays) {
    }
    
    @java.lang.Override
    public int getCount() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public se.thomas.arbetstidskalkylator.CalendarDay getItem(int position) {
        return null;
    }
    
    @java.lang.Override
    public long getItemId(int position) {
        return 0L;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.view.View getView(int position, @org.jetbrains.annotations.Nullable
    android.view.View convertView, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup parent) {
        return null;
    }
}