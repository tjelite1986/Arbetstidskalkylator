package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.calculator.TaxTable2025
import com.example.timereportcalculator.calculator.TaxTable2026
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.data.VacationPaymentMode

@Composable
fun TotalSummaryCard(
    totalHours: Double,
    totalBasePay: Double,
    totalOBPay: Double,
    totalGrossPay: Double,
    totalVacationPay: Double,
    totalPayBeforeTax: Double,
    totalTax: Double,
    totalNetPay: Double,
    vacationRate: Double = 12.0,
    totalOBBreakdown: Map<String, Double> = emptyMap(),
    settings: Settings = Settings(),
    periodYear: Int = java.time.LocalDate.now().year
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.secondary,
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Totalt för period",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total arbetstid:")
                Text("${String.format("%.2f", totalHours)} tim", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Grundlön:")
                Text("${String.format("%.2f", totalBasePay)} kr")
            }
            
            // Show individual OB-tillägg if any exist
            if (totalOBBreakdown.isNotEmpty()) {
                totalOBBreakdown.forEach { (description, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(description)
                        Text("${String.format("%.2f", amount)} kr")
                    }
                }
            } else if (totalOBPay > 0) {
                // Fallback for entries without breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("OB-tillägg:")
                    Text("${String.format("%.2f", totalOBPay)} kr")
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Bruttolön:")
                Text("${String.format("%.2f", totalGrossPay)} kr")
            }
            
            // Semesterersättning - visa olika information beroende på betalningssätt
            when (settings.vacationPaymentMode) {
                VacationPaymentMode.INCLUDED_IN_SALARY -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Semesterersättning (${String.format("%.0f", vacationRate)}%):")
                        Text("${String.format("%.2f", totalVacationPay)} kr")
                    }
                }
                VacationPaymentMode.SEPARATE_ACCUMULATION -> {
                    // Beräkna vad semesterersättningen skulle ha varit för denna period
                    val calculatedVacationPay = totalGrossPay * (vacationRate / 100.0)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Semesterersättning (separat sparad):")
                        Text("${String.format("%.2f", calculatedVacationPay)} kr", color = MaterialTheme.colors.primary)
                    }
                }
            }
            
            // Visa total ackumulerad semesterersättning om separat ackumulering är valt
            if (settings.vacationPaymentMode == VacationPaymentMode.SEPARATE_ACCUMULATION) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total ackumulerad semesterersättning:", fontWeight = FontWeight.Bold)
                    Text("${String.format("%.2f", settings.accumulatedVacationPay)} kr", 
                         fontWeight = FontWeight.Bold,
                         color = MaterialTheme.colors.primary)
                }
                
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Totalt före skatt:", fontWeight = FontWeight.Bold)
                Text("${String.format("%.2f", totalPayBeforeTax)} kr", fontWeight = FontWeight.Bold)
            }
            
            val displayTax: Double
            val displayNet: Double
            val taxLabel: String

            if (settings.useTaxTable) {
                displayTax = when (periodYear) {
                    2026 -> TaxTable2026.lookupMonthlyTax(totalPayBeforeTax)
                    else -> TaxTable2025.lookupMonthlyTax(totalPayBeforeTax)
                }
                displayNet = totalPayBeforeTax - displayTax
                taxLabel = "Skatt (Tabell 35, $periodYear):"
            } else {
                displayTax = totalTax
                displayNet = totalNetPay
                taxLabel = "Skatt:"
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(taxLabel)
                Text("${String.format("%.2f", displayTax)} kr")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Totalt netto:",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format("%.2f", displayNet)} kr",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000) // Mörk röd färg
                )
            }
        }
    }
}