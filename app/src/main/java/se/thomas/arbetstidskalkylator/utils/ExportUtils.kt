package se.thomas.arbetstidskalkylator.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import se.thomas.arbetstidskalkylator.data.TimeEntry
import se.thomas.arbetstidskalkylator.data.OvertimeRate
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.BaseFont

object ExportUtils {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    fun exportToCSV(context: Context, timeEntries: List<TimeEntry>, overtimeRates: List<OvertimeRate> = emptyList()): Uri? {
        return try {
            val fileName = "arbetstider_${System.currentTimeMillis()}.csv"
            val file = File(context.getExternalFilesDir(null), fileName)
            
            FileWriter(file).use { writer ->
                // CSV Header
                writer.append("Datum,Starttid,Sluttid,Timmar,Timlön,Total lön,Beskrivning\n")
                
                timeEntries.forEach { entry ->
                    writer.append("${dateFormat.format(entry.startTime)},")
                    writer.append("${timeFormat.format(entry.startTime)},")
                    writer.append("${entry.endTime?.let { timeFormat.format(it) } ?: ""},")
                    writer.append("${"%.2f".format(entry.getWorkedHours())},")
                    writer.append("${"%.2f".format(entry.hourlyRate)},")
                    writer.append("${"%.2f".format(entry.calculatePay(overtimeRates))},")
                    writer.append("${entry.description ?: ""}\n")
                }
            }
            
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun shareCSV(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Dela arbetstider"))
    }
    
    fun exportSummaryText(timeEntries: List<TimeEntry>, overtimeRates: List<OvertimeRate> = emptyList()): String {
        val totalHours = timeEntries.sumOf { it.getWorkedHours() }
        val totalPay = timeEntries.sumOf { it.calculatePay(overtimeRates) }
        
        val summary = StringBuilder()
        summary.append("ARBETSTIDSSAMMANFATTNING\n")
        summary.append("========================\n\n")
        
        if (timeEntries.isNotEmpty()) {
            val firstDate = timeEntries.minBy { it.startTime }.startTime
            val lastDate = timeEntries.maxBy { it.startTime }.startTime
            
            summary.append("Period: ${dateFormat.format(firstDate)} - ${dateFormat.format(lastDate)}\n")
            summary.append("Antal registrerade pass: ${timeEntries.size}\n")
            summary.append("Totala arbetstimmar: ${"%.2f".format(totalHours)} h\n")
            summary.append("Total lön: ${"%.2f".format(totalPay)} kr\n\n")
            
            summary.append("DETALJERAT:\n")
            summary.append("-----------\n")
            
            timeEntries.sortedByDescending { it.startTime }.forEach { entry ->
                summary.append("${dateFormat.format(entry.startTime)}: ")
                summary.append("${timeFormat.format(entry.startTime)} - ")
                summary.append("${entry.endTime?.let { timeFormat.format(it) } ?: "Pågående"} ")
                summary.append("(${"%.2f".format(entry.getWorkedHours())} h, ")
                summary.append("${"%.2f".format(entry.calculatePay(overtimeRates))} kr)")
                if (!entry.description.isNullOrBlank()) {
                    summary.append(" - ${entry.description}")
                }
                summary.append("\n")
            }
        } else {
            summary.append("Inga arbetstider registrerade.\n")
        }
        
        return summary.toString()
    }
    
    fun shareSummaryText(context: Context, summaryText: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, summaryText)
            putExtra(Intent.EXTRA_SUBJECT, "Arbetstidssammanfattning")
        }
        context.startActivity(Intent.createChooser(intent, "Dela sammanfattning"))
    }
    
    /**
     * Exporterar arbetstider för en specifik månad till PDF
     */
    fun exportMonthToPDF(context: Context, timeEntries: List<TimeEntry>, month: Int, year: Int, overtimeRates: List<OvertimeRate> = emptyList()): Uri? {
        return try {
            val monthName = SimpleDateFormat("MMMM", Locale("sv", "SE")).format(Calendar.getInstance().apply {
                set(Calendar.MONTH, month - 1)
            }.time)
            val fileName = "arbetstider_${monthName}_${year}.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)
            
            createPDF(file, timeEntries, overtimeRates, "Arbetstider $monthName $year")
            
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Exporterar alla arbetstider till PDF
     */
    fun exportAllToPDF(context: Context, timeEntries: List<TimeEntry>, overtimeRates: List<OvertimeRate> = emptyList()): Uri? {
        return try {
            val fileName = "alla_arbetstider_${System.currentTimeMillis()}.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)
            
            createPDF(file, timeEntries, overtimeRates, "Alla arbetstider")
            
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun createPDF(file: File, timeEntries: List<TimeEntry>, overtimeRates: List<OvertimeRate>, title: String) {
        val document = Document(PageSize.A4)
        val writer = PdfWriter.getInstance(document, FileOutputStream(file))
        
        document.open()
        
        try {
            // Titel
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD)
            val titleParagraph = Paragraph(title, titleFont)
            titleParagraph.alignment = Element.ALIGN_CENTER
            titleParagraph.spacingAfter = 20f
            document.add(titleParagraph)
            
            if (timeEntries.isNotEmpty()) {
                // Sammanfattning
                val totalHours = timeEntries.sumOf { it.getWorkedHours() }
                val totalPay = timeEntries.sumOf { it.calculatePay(overtimeRates) }
                
                val summaryFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
                val normalFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL)
                
                document.add(Paragraph("SAMMANFATTNING", summaryFont))
                document.add(Paragraph("Antal pass: ${timeEntries.size}", normalFont))
                document.add(Paragraph("Totala timmar: ${"%.2f".format(totalHours)} h", normalFont))
                document.add(Paragraph("Total lön: ${"%.2f".format(totalPay)} kr", normalFont))
                document.add(Paragraph(" ")) // Tom rad
                
                // Tabell med arbetstider
                val table = PdfPTable(6)
                table.widthPercentage = 100f
                table.setWidths(floatArrayOf(2f, 1.5f, 1.5f, 1f, 1.5f, 2f))
                
                // Tabell headers
                val headerFont = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD)
                addTableHeader(table, "Datum", headerFont)
                addTableHeader(table, "Start", headerFont)
                addTableHeader(table, "Slut", headerFont)
                addTableHeader(table, "Timmar", headerFont)
                addTableHeader(table, "Lön (kr)", headerFont)
                addTableHeader(table, "Beskrivning", headerFont)
                
                // Tabell data
                val dataFont = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL)
                timeEntries.sortedByDescending { it.startTime }.forEach { entry ->
                    table.addCell(PdfPCell(Phrase(dateFormat.format(entry.startTime), dataFont)))
                    table.addCell(PdfPCell(Phrase(timeFormat.format(entry.startTime), dataFont)))
                    table.addCell(PdfPCell(Phrase(entry.endTime?.let { timeFormat.format(it) } ?: "-", dataFont)))
                    table.addCell(PdfPCell(Phrase("%.2f".format(entry.getWorkedHours()), dataFont)))
                    table.addCell(PdfPCell(Phrase("%.2f".format(entry.calculatePay(overtimeRates)), dataFont)))
                    table.addCell(PdfPCell(Phrase(entry.description ?: "", dataFont)))
                }
                
                document.add(table)
                
                // Footer med genererad datum
                document.add(Paragraph(" ")) // Tom rad
                val footerFont = Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC)
                val footerText = Paragraph("Genererad: ${dateTimeFormat.format(Date())}", footerFont)
                footerText.alignment = Element.ALIGN_RIGHT
                document.add(footerText)
                
            } else {
                val noDataFont = Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL)
                document.add(Paragraph("Inga arbetstider registrerade för denna period.", noDataFont))
            }
            
        } finally {
            document.close()
        }
    }
    
    private fun addTableHeader(table: PdfPTable, text: String, font: Font) {
        val cell = PdfPCell(Phrase(text, font))
        cell.backgroundColor = BaseColor.LIGHT_GRAY
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.setPadding(8f)
        table.addCell(cell)
    }
    
    fun sharePDF(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Dela PDF"))
    }
}