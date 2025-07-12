package com.example.timereportcalculator.ui.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import java.util.Calendar

@Composable
fun TimePickerDialog(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    title: String = "Välj tid"
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    // Set initial time
    val initialHour = selectedTime?.hour ?: calendar.get(Calendar.HOUR_OF_DAY)
    val initialMinute = selectedTime?.minute ?: calendar.get(Calendar.MINUTE)
    
    // Show the native Android TimePickerDialog
    LaunchedEffect(Unit) {
        showTimePickerDialog(
            context = context,
            initialHour = initialHour,
            initialMinute = initialMinute,
            onTimeSelected = onTimeSelected,
            onDismiss = onDismiss
        )
    }
}

private fun showTimePickerDialog(
    context: Context,
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            // When user selects a time
            val selectedTime = LocalTime.of(hourOfDay, minute)
            onTimeSelected(selectedTime)
        },
        initialHour,
        initialMinute,
        true // Use 24-hour format
    )
    
    // Handle dialog dismiss
    timePickerDialog.setOnDismissListener {
        onDismiss()
    }
    
    // Add Clear button functionality
    timePickerDialog.setButton(
        TimePickerDialog.BUTTON_NEUTRAL,
        "Rensa"
    ) { _, _ ->
        // Clear the time (you can handle this as needed)
        onDismiss()
    }
    
    timePickerDialog.show()
}

