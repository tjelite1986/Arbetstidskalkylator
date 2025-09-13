package se.thomas.arbetstidskalkylator.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J4\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00132\u0006\u0010\u0017\u001a\u00020\fH\u0002J.\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0013J>\u0010\u001c\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0013J$\u0010 \u001a\u00020\f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0013J.\u0010!\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0013J\u0016\u0010\"\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\u0019J\u0016\u0010$\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\u0019J\u0016\u0010%\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lse/thomas/arbetstidskalkylator/utils/ExportUtils;", "", "()V", "dateFormat", "Ljava/text/SimpleDateFormat;", "dateTimeFormat", "timeFormat", "addTableHeader", "", "table", "Lcom/itextpdf/text/pdf/PdfPTable;", "text", "", "font", "Lcom/itextpdf/text/Font;", "createPDF", "file", "Ljava/io/File;", "timeEntries", "", "Lse/thomas/arbetstidskalkylator/data/TimeEntry;", "overtimeRates", "Lse/thomas/arbetstidskalkylator/data/OvertimeRate;", "title", "exportAllToPDF", "Landroid/net/Uri;", "context", "Landroid/content/Context;", "exportMonthToPDF", "month", "", "year", "exportSummaryText", "exportToCSV", "shareCSV", "uri", "sharePDF", "shareSummaryText", "summaryText", "app_debug"})
public final class ExportUtils {
    @org.jetbrains.annotations.NotNull
    private static final java.text.SimpleDateFormat dateFormat = null;
    @org.jetbrains.annotations.NotNull
    private static final java.text.SimpleDateFormat timeFormat = null;
    @org.jetbrains.annotations.NotNull
    private static final java.text.SimpleDateFormat dateTimeFormat = null;
    @org.jetbrains.annotations.NotNull
    public static final se.thomas.arbetstidskalkylator.utils.ExportUtils INSTANCE = null;
    
    private ExportUtils() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri exportToCSV(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return null;
    }
    
    public final void shareCSV(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String exportSummaryText(@org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return null;
    }
    
    public final void shareSummaryText(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String summaryText) {
    }
    
    /**
     * Exporterar arbetstider för en specifik månad till PDF
     */
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri exportMonthToPDF(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, int month, int year, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return null;
    }
    
    /**
     * Exporterar alla arbetstider till PDF
     */
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri exportAllToPDF(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, @org.jetbrains.annotations.NotNull
    java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates) {
        return null;
    }
    
    private final void createPDF(java.io.File file, java.util.List<se.thomas.arbetstidskalkylator.data.TimeEntry> timeEntries, java.util.List<se.thomas.arbetstidskalkylator.data.OvertimeRate> overtimeRates, java.lang.String title) {
    }
    
    private final void addTableHeader(com.itextpdf.text.pdf.PdfPTable table, java.lang.String text, com.itextpdf.text.Font font) {
    }
    
    public final void sharePDF(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
}