package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
    totalOBBreakdown: Map<String, Double> = emptyMap()
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
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Semesterersättning (${String.format("%.0f", vacationRate)}%):")
                Text("${String.format("%.2f", totalVacationPay)} kr")
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Totalt före skatt:", fontWeight = FontWeight.Bold)
                Text("${String.format("%.2f", totalPayBeforeTax)} kr", fontWeight = FontWeight.Bold)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Skatt:")
                Text("${String.format("%.2f", totalTax)} kr")
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
                    text = "${String.format("%.2f", totalNetPay)} kr",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000) // Mörk röd färg
                )
            }
        }
    }
}