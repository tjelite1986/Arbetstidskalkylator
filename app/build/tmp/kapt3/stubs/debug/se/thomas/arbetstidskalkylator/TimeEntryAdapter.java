package se.thomas.arbetstidskalkylator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001+B-\u0012\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\rH\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u001c\u0010 \u001a\u00020\u00062\n\u0010!\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\"\u001a\u00020\rH\u0016J\u001c\u0010#\u001a\u00060\u0002R\u00020\u00002\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\rH\u0016J\u0014\u0010\'\u001a\u00020\u00062\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fJ\u0014\u0010)\u001a\u00020\u00062\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lse/thomas/arbetstidskalkylator/TimeEntryAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lse/thomas/arbetstidskalkylator/TimeEntryAdapter$TimeEntryViewHolder;", "onEditClick", "Lkotlin/Function1;", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "", "onDeleteClick", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "dateFormat", "Ljava/text/SimpleDateFormat;", "expandedItems", "", "", "overtimeRates", "", "Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "timeEntries", "timeFormat", "weekdayFormat", "formatHoursToReadable", "", "hours", "", "getItemCount", "isEasterRelated", "", "calendar", "Ljava/util/Calendar;", "isHolidayOrSunday", "date", "Ljava/util/Date;", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "submitList", "list", "updateOvertimeRates", "rates", "TimeEntryViewHolder", "app_debug"})
public final class TimeEntryAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<se.thomas.arbetstidskalkylator.TimeEntryAdapter.TimeEntryViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<se.thomas.arbetstidskalkylator.data.TimeEntry, kotlin.Unit> onEditClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<se.thomas.arbetstidskalkylator.data.TimeEntry, kotlin.Unit> onDeleteClick = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries;
    @org.jetbrains.annotations.NotNull
    private java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates;
    @org.jetbrains.annotations.NotNull
    private final java.util.Set<java.lang.Integer> expandedItems = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat dateFormat = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat timeFormat = null;
    @org.jetbrains.annotations.NotNull
    private final java.text.SimpleDateFormat weekdayFormat = null;
    
    public TimeEntryAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super se.thomas.arbetstidskalkylator.data.TimeEntry, kotlin.Unit> onEditClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super se.thomas.arbetstidskalkylator.data.TimeEntry, kotlin.Unit> onDeleteClick) {
        super();
    }
    
    private final boolean isHolidayOrSunday(java.util.Date date) {
        return false;
    }
    
    private final boolean isEasterRelated(java.util.Calendar calendar) {
        return false;
    }
    
    public final void submitList(@org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> list) {
    }
    
    public final void updateOvertimeRates(@org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> rates) {
    }
    
    private final java.lang.String formatHoursToReadable(double hours) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public se.thomas.arbetstidskalkylator.TimeEntryAdapter.TimeEntryViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    se.thomas.arbetstidskalkylator.TimeEntryAdapter.TimeEntryViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lse/thomas/arbetstidskalkylator/TimeEntryAdapter$TimeEntryViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lse/thomas/arbetstidskalkylator/databinding/ItemTimeEntryBinding;", "(Lse/thomas/arbetstidskalkylator/TimeEntryAdapter;Lse/thomas/arbetstidskalkylator/databinding/ItemTimeEntryBinding;)V", "bind", "", "timeEntry", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "position", "", "app_debug"})
    public final class TimeEntryViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final se.thomas.arbetstidskalkylator.databinding.ItemTimeEntryBinding binding = null;
        
        public TimeEntryViewHolder(@org.jetbrains.annotations.NotNull
        se.thomas.arbetstidskalkylator.databinding.ItemTimeEntryBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        se.thomas.arbetstidskalkylator.data.TimeEntry timeEntry, int position) {
        }
    }
}