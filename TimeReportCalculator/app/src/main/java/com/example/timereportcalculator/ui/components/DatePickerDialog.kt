package com.example.timereportcalculator.ui.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DatePickerDialog(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    // Set initial date
    val initialYear = selectedDate.year
    val initialMonth = selectedDate.monthValue - 1 // Calendar months are 0-based
    val initialDay = selectedDate.dayOfMonth
    
    // Show the native Android DatePickerDialog
    LaunchedEffect(selectedDate) {
        showDatePickerDialog(
            context = context,
            initialYear = initialYear,
            initialMonth = initialMonth,
            initialDay = initialDay,
            onDateSelected = onDateSelected,
            onDismiss = onDismiss
        )
    }
}

private fun showDatePickerDialog(
    context: Context,
    initialYear: Int,
    initialMonth: Int,
    initialDay: Int,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    try {
        // Använd standard context först, sedan försök att förbättra med svenska inställningar
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // When user selects a date
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // Convert back to 1-based month
                onDateSelected(selectedDate)
            },
            initialYear,
            initialMonth,
            initialDay
        )
        
        // Försök att ställa in måndag som första veckodag
        try {
            val datePicker = datePickerDialog.datePicker
            datePicker.firstDayOfWeek = Calendar.MONDAY
        } catch (e: Exception) {
            // Ignorera om vi inte kan ställa in första veckodag
        }
        
        // Handle dialog dismiss
        datePickerDialog.setOnDismissListener {
            onDismiss()
        }
        
        // Add "Idag" (Today) button functionality
        datePickerDialog.setButton(
            DatePickerDialog.BUTTON_NEUTRAL,
            "Idag"
        ) { _, _ ->
            val today = LocalDate.now()
            onDateSelected(today)
        }
        
        datePickerDialog.show()
        
    } catch (e: Exception) {
        // Fallback - om DatePickerDialog crashar helt, bara avsluta
        onDismiss()
    }
}