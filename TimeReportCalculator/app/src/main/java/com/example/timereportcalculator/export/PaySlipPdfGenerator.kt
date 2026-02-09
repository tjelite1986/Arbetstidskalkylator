package com.example.timereportcalculator.export

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.timereportcalculator.calculator.TaxTable2025
import com.example.timereportcalculator.calculator.TaxTable2026
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.data.VacationPaymentMode
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaySlipPdfGenerator(private val context: Context) {

    companion object {
        // A4 dimensions in points (72 dpi)
        private const val PAGE_WIDTH = 595
        private const val PAGE_HEIGHT = 842
        private const val MARGIN_LEFT = 50f
        private const val MARGIN_RIGHT = 545f
        private const val MARGIN_TOP = 50f

        private val SWEDISH_MONTHS = arrayOf(
            "Januari", "Februari", "Mars", "April", "Maj", "Juni",
            "Juli", "Augusti", "September", "Oktober", "November", "December"
        )
    }

    private val numberFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("sv", "SE")).apply {
        groupingSeparator = ' '
        decimalSeparator = ','
    })

    private val intFormat = DecimalFormat("#,##0", DecimalFormatSymbols(Locale("sv", "SE")).apply {
        groupingSeparator = ' '
    })

    fun getSwedishMonthName(month: Int): String = SWEDISH_MONTHS[month - 1]

    fun generate(
        entries: List<TimeEntry>,
        settings: Settings,
        year: Int,
        month: Int
    ): File {
        val yearMonth = YearMonth.of(year, month)
        val monthEntries = entries.filter {
            it.date.year == year && it.date.monthValue == month
        }

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)

        drawPaySlip(page.canvas, monthEntries, settings, yearMonth)

        document.finishPage(page)

        val dir = File(context.filesDir, "payslips")
        if (!dir.exists()) dir.mkdirs()
        val monthName = getSwedishMonthName(month).lowercase()
        val file = File(dir, "lonebesked_${year}_${monthName}.pdf")
        file.outputStream().use { document.writeTo(it) }
        document.close()

        return file
    }

    private fun drawPaySlip(
        canvas: Canvas,
        entries: List<TimeEntry>,
        settings: Settings,
        yearMonth: YearMonth
    ) {
        val paintTitle = Paint().apply {
            color = Color.BLACK
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val paintHeader = Paint().apply {
            color = Color.BLACK
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val paintNormal = Paint().apply {
            color = Color.BLACK
            textSize = 11f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        val paintBold = Paint().apply {
            color = Color.BLACK
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val paintLine = Paint().apply {
            color = Color.BLACK
            strokeWidth = 0.5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        val paintLineThin = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 0.3f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        var y = MARGIN_TOP

        // === HEADER ===
        canvas.drawText("L\u00f6nebesked", MARGIN_LEFT, y + 22f, paintTitle)

        // Period info (right side)
        val periodStart = yearMonth.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
        val periodEnd = yearMonth.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val periodText = "$periodStart - $periodEnd"

        val labelPaint = Paint(paintNormal).apply { textSize = 10f }
        canvas.drawText("L\u00f6neperiod", 400f, y + 10f, labelPaint)
        canvas.drawText(periodText, 400f, y + 24f, paintNormal)

        y += 55f

        // === EMPLOYER / EMPLOYEE INFO ===
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_RIGHT, y, paintLineThin)
        y += 18f

        val employerName = settings.employerName.ifEmpty { "Arbetsgivare" }
        val employeeName = settings.employeeName.ifEmpty { "Anst\u00e4lld" }

        canvas.drawText(employerName, MARGIN_LEFT, y, paintHeader)
        canvas.drawText(employeeName, 320f, y, paintHeader)

        y += 30f
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_RIGHT, y, paintLineThin)
        y += 25f

        // === TABLE HEADER ===
        val colArt = MARGIN_LEFT
        val colText = MARGIN_LEFT + 35f
        val colAntal = 300f
        val colUnit = 370f
        val colApris = 410f
        val colBelopp = MARGIN_RIGHT

        // Header row background
        val headerPaint = Paint().apply {
            color = Color.rgb(240, 240, 240)
            style = Paint.Style.FILL
        }
        canvas.drawRect(MARGIN_LEFT - 5f, y - 14f, MARGIN_RIGHT + 5f, y + 4f, headerPaint)
        canvas.drawLine(MARGIN_LEFT - 5f, y - 14f, MARGIN_RIGHT + 5f, y - 14f, paintLine)
        canvas.drawLine(MARGIN_LEFT - 5f, y + 4f, MARGIN_RIGHT + 5f, y + 4f, paintLine)

        canvas.drawText("Art", colArt, y, paintBold)
        canvas.drawText("Text", colText, y, paintBold)
        drawRightAligned(canvas, "Antal", colAntal + 40f, y, paintBold)
        drawRightAligned(canvas, "A-pris", colApris + 30f, y, paintBold)
        drawRightAligned(canvas, "Belopp", colBelopp, y, paintBold)

        y += 20f

        // === CALCULATIONS ===
        val totalWorkHours = entries.sumOf { it.workHours }
        val totalBasePay = entries.sumOf { it.basePay }
        val totalOBPay = entries.sumOf { it.obPay }
        val totalGrossPay = entries.sumOf { it.grossPay }
        val totalVacationPay = entries.sumOf { it.vacationPay }

        // Group OB by percentage
        val obGroups = mutableMapOf<String, Pair<Double, Double>>() // label -> (hours, amount)
        for (entry in entries) {
            for ((desc, amount) in entry.obBreakdown) {
                // Extract OB percentage from description, e.g. "OB 50% mån-fre 18:15-20:00"
                val percentMatch = Regex("(\\d+)%").find(desc)
                val percent = percentMatch?.groupValues?.get(1)?.toIntOrNull()
                if (percent != null && amount > 0) {
                    val rate = percent / 100.0
                    val obHours = if (settings.basePay * rate > 0) amount / (settings.basePay * rate) else 0.0
                    val key = "OB-till\u00e4gg ${percent}%"
                    val existing = obGroups[key]
                    if (existing != null) {
                        obGroups[key] = Pair(existing.first + obHours, existing.second + amount)
                    } else {
                        obGroups[key] = Pair(obHours, amount)
                    }
                }
            }
        }

        // Merge OB groups by percentage (40%, 50%, 70%, 100%)
        val mergedOB = mutableMapOf<Int, Pair<Double, Double>>()
        for ((key, value) in obGroups) {
            val percentMatch = Regex("(\\d+)%").find(key)
            val percent = percentMatch?.groupValues?.get(1)?.toIntOrNull() ?: continue
            val existing = mergedOB[percent]
            if (existing != null) {
                mergedOB[percent] = Pair(existing.first + value.first, existing.second + value.second)
            } else {
                mergedOB[percent] = value
            }
        }

        val monthName = getSwedishMonthName(yearMonth.monthValue)

        // Row 1: Timlön
        canvas.drawText("10", colArt, y, paintNormal)
        canvas.drawText("Timl\u00f6n $monthName", colText, y, paintNormal)
        drawRightAligned(canvas, formatNumber(totalWorkHours), colAntal + 20f, y, paintNormal)
        canvas.drawText("Tim", colUnit, y, paintNormal)
        drawRightAligned(canvas, formatNumber(settings.basePay), colApris + 30f, y, paintNormal)
        drawRightAligned(canvas, formatNumber(totalBasePay), colBelopp, y, paintNormal)
        y += 18f

        // OB rows
        val obArtCodes = mapOf(40 to "410", 50 to "411", 70 to "412", 100 to "413")
        for (percent in mergedOB.keys.sorted()) {
            val (hours, amount) = mergedOB[percent] ?: continue
            if (amount <= 0) continue
            val artCode = obArtCodes[percent] ?: "411"
            val aPris = settings.basePay * (percent / 100.0)

            canvas.drawText(artCode, colArt, y, paintNormal)
            canvas.drawText("OB-till\u00e4gg ${percent}%", colText, y, paintNormal)
            drawRightAligned(canvas, formatNumber(hours), colAntal + 20f, y, paintNormal)
            canvas.drawText("Tim", colUnit, y, paintNormal)
            drawRightAligned(canvas, formatNumber(aPris), colApris + 30f, y, paintNormal)
            drawRightAligned(canvas, formatNumber(amount), colBelopp, y, paintNormal)
            y += 18f
        }

        // Semesterersättning row (if included in salary)
        if (settings.vacationPaymentMode == VacationPaymentMode.INCLUDED_IN_SALARY && totalVacationPay > 0) {
            canvas.drawText("500", colArt, y, paintNormal)
            canvas.drawText("Semesterersättning ${formatNumber(settings.vacationRate)}%", colText, y, paintNormal)
            drawRightAligned(canvas, formatNumber(totalVacationPay), colBelopp, y, paintNormal)
            y += 18f
        }

        // Bruttolön before tax
        val brutto = if (settings.vacationPaymentMode == VacationPaymentMode.INCLUDED_IN_SALARY) {
            totalGrossPay + totalVacationPay
        } else {
            totalGrossPay
        }

        // Tax calculation
        val taxAmount: Double
        val taxLabel: String
        if (settings.useTaxTable) {
            taxAmount = when (yearMonth.year) {
                2026 -> TaxTable2026.lookupMonthlyTax(brutto)
                else -> TaxTable2025.lookupMonthlyTax(brutto)
            }
            taxLabel = "Prelimin\u00e4r skatt (Tabell 35)"
        } else {
            taxAmount = brutto * (settings.taxRate / 100.0)
            taxLabel = "Prelimin\u00e4r skatt ${formatNumber(settings.taxRate)}%"
        }

        // Tax row
        canvas.drawText("912", colArt, y, paintNormal)
        canvas.drawText(taxLabel, colText, y, paintNormal)
        drawRightAligned(canvas, "-${formatNumber(taxAmount)}", colBelopp, y, paintNormal)
        y += 5f

        // Line under table
        canvas.drawLine(MARGIN_LEFT - 5f, y, MARGIN_RIGHT + 5f, y, paintLine)
        y += 25f

        // === SUMMARY SECTION ===
        val netPay = brutto - taxAmount

        // Summary box
        val summaryTop = y - 5f
        canvas.drawRect(MARGIN_LEFT - 5f, summaryTop, MARGIN_RIGHT + 5f, summaryTop + 120f, Paint().apply {
            color = Color.rgb(248, 248, 248)
            style = Paint.Style.FILL
        })
        canvas.drawRect(MARGIN_LEFT - 5f, summaryTop, MARGIN_RIGHT + 5f, summaryTop + 120f, paintLine)

        // Left column: details
        canvas.drawText("Bruttol\u00f6n:", MARGIN_LEFT + 5f, y + 12f, paintNormal)
        drawRightAligned(canvas, "${formatNumber(brutto)} kr", 250f, y + 12f, paintNormal)

        canvas.drawText("Prelimin\u00e4r skatt:", MARGIN_LEFT + 5f, y + 30f, paintNormal)
        drawRightAligned(canvas, "-${formatNumber(taxAmount)} kr", 250f, y + 30f, paintNormal)

        canvas.drawText("Arbetad tid:", MARGIN_LEFT + 5f, y + 48f, paintNormal)
        drawRightAligned(canvas, "${formatNumber(totalWorkHours)} tim", 250f, y + 48f, paintNormal)

        if (totalOBPay > 0) {
            canvas.drawText("Varav OB-till\u00e4gg:", MARGIN_LEFT + 5f, y + 66f, paintNormal)
            drawRightAligned(canvas, "${formatNumber(totalOBPay)} kr", 250f, y + 66f, paintNormal)
        }

        // Right column: "Utbetalas" (big)
        canvas.drawText("Utbetalas", 380f, y + 20f, paintHeader)

        val netPayFormatted = intFormat.format(netPay.toLong()) + ",${String.format("%02d", ((netPay * 100).toLong() % 100).let { if (it < 0) -it else it })}"
        canvas.drawText("${netPayFormatted} kr", 360f, y + 55f, Paint().apply {
            color = Color.BLACK
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        })

        // Payment date estimate (last weekday of month)
        val paymentDate = getPaymentDate(yearMonth)
        canvas.drawText(paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE), 400f, y + 75f, paintNormal)

        y += 140f

        // === MESSAGE / DISCLAIMER ===
        y += 15f
        canvas.drawText("Meddelande", MARGIN_LEFT, y, paintBold)
        y += 16f
        canvas.drawText("Om du har n\u00e5gra fr\u00e5gor, v\u00e4nligen kontakta alltid din n\u00e4rmaste chef", MARGIN_LEFT, y, paintNormal)

        y += 30f
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_RIGHT, y, paintLineThin)

        y += 20f

        // === FOOTER: Disclaimer ===
        val disclaimerPaint = Paint().apply {
            color = Color.rgb(150, 150, 150)
            textSize = 8f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            isAntiAlias = true
        }
        canvas.drawText("Detta \u00e4r inte ett officiellt l\u00f6nebesked. Genererat av Arbetstidskalkylator-appen.", MARGIN_LEFT, y, disclaimerPaint)
        y += 12f
        canvas.drawText("Alla ber\u00e4kningar \u00e4r uppskattningar baserade p\u00e5 inmatade tider och inst\u00e4llningar.", MARGIN_LEFT, y, disclaimerPaint)
    }

    private fun drawRightAligned(canvas: Canvas, text: String, x: Float, y: Float, paint: Paint) {
        val width = paint.measureText(text)
        canvas.drawText(text, x - width, y, paint)
    }

    private fun formatNumber(value: Double): String {
        return numberFormat.format(value)
    }

    private fun getPaymentDate(yearMonth: YearMonth): LocalDate {
        // Swedish standard: salary paid on 25th (or previous weekday)
        var date = yearMonth.atDay(25)
        while (date.dayOfWeek.value > 5) { // Saturday=6, Sunday=7
            date = date.minusDays(1)
        }
        return date
    }
}
