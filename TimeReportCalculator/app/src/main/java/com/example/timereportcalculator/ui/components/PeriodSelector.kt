package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.PeriodFilter
import com.example.timereportcalculator.data.PeriodType
import java.time.LocalDate

@Composable
fun PeriodSelector(
    selectedPeriod: PeriodFilter,
    onPeriodChanged: (PeriodFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDropdown by remember { mutableStateOf(false) }
    var showCustomDatePicker by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        // Period dropdown button
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp
        ) {
            TextButton(
                onClick = { showDropdown = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Period: ${selectedPeriod.getDisplayText()}",
                        style = MaterialTheme.typography.body1
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Välj period"
                    )
                }
            }
        }
        
        // Dropdown menu
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false }
        ) {
            PeriodType.values().forEach { periodType ->
                DropdownMenuItem(
                    onClick = {
                        showDropdown = false
                        if (periodType == PeriodType.CUSTOM) {
                            showCustomDatePicker = true
                        } else {
                            onPeriodChanged(PeriodFilter(type = periodType))
                        }
                    }
                ) {
                    Text(periodType.displayName)
                }
            }
        }
    }
    
    // Custom date range picker dialog
    if (showCustomDatePicker) {
        CustomDateRangeDialog(
            startDate = selectedPeriod.customStartDate ?: LocalDate.now().withDayOfMonth(1),
            endDate = selectedPeriod.customEndDate ?: LocalDate.now(),
            onDateRangeSelected = { startDate, endDate ->
                onPeriodChanged(
                    PeriodFilter(
                        type = PeriodType.CUSTOM,
                        customStartDate = startDate,
                        customEndDate = endDate
                    )
                )
                showCustomDatePicker = false
            },
            onDismiss = { showCustomDatePicker = false }
        )
    }
}

@Composable
private fun CustomDateRangeDialog(
    startDate: LocalDate,
    endDate: LocalDate,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentStartDate by remember { mutableStateOf(startDate) }
    var currentEndDate by remember { mutableStateOf(endDate) }
    var isSelectingStartDate by remember { mutableStateOf(true) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Välj anpassad period") },
        text = {
            Column {
                Text(
                    text = "Välj start- och slutdatum för perioden",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Start date button
                OutlinedButton(
                    onClick = {
                        isSelectingStartDate = true
                        showDatePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Startdatum: $currentStartDate")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // End date button  
                OutlinedButton(
                    onClick = {
                        isSelectingStartDate = false
                        showDatePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Slutdatum: $currentEndDate")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(currentStartDate, currentEndDate)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Avbryt")
            }
        }
    )
    
    // Date picker for start/end date
    if (showDatePicker) {
        DatePickerDialog(
            selectedDate = if (isSelectingStartDate) currentStartDate else currentEndDate,
            onDateSelected = { selectedDate ->
                if (isSelectingStartDate) {
                    currentStartDate = selectedDate
                    if (selectedDate.isAfter(currentEndDate)) {
                        currentEndDate = selectedDate
                    }
                } else {
                    currentEndDate = selectedDate
                    if (selectedDate.isBefore(currentStartDate)) {
                        currentStartDate = selectedDate
                    }
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}