package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimeInputField(
    time: String,
    onTimeChange: (String) -> Unit,
    label: String,
    placeholder: String = "00:00",
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var hourPart by remember(time) { mutableStateOf(extractHour(time)) }
    var minutePart by remember(time) { mutableStateOf(extractMinute(time)) }
    
    // Update parent when internal state changes
    LaunchedEffect(hourPart, minutePart) {
        val formattedTime = formatTime(hourPart, minutePart)
        if (formattedTime != time) {
            onTimeChange(formattedTime)
        }
    }
    
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = if (isError) 2.dp else 1.dp,
            backgroundColor = if (isError) MaterialTheme.colors.error.copy(alpha = 0.1f) else MaterialTheme.colors.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hour input
                BasicTextField(
                    value = hourPart,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        if (filtered.length <= 2) {
                            val hourValue = filtered.toIntOrNull()
                            if (hourValue == null || hourValue <= 23) {
                                hourPart = filtered.padStart(2, '0').take(2)
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.width(40.dp)
                ) { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (hourPart.isEmpty()) {
                            Text(
                                text = "00",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
                
                // Fixed colon
                Text(
                    text = ":",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.primary
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                
                // Minute input
                BasicTextField(
                    value = minutePart,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        if (filtered.length <= 2) {
                            val minuteValue = filtered.toIntOrNull()
                            if (minuteValue == null || minuteValue <= 59) {
                                minutePart = filtered.padStart(2, '0').take(2)
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = if (isError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.width(40.dp)
                ) { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (minutePart.isEmpty()) {
                            Text(
                                text = "00",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            }
        }
        
        if (isError) {
            Text(
                text = "Ogiltig tid",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Helper functions
private fun extractHour(time: String): String {
    return if (time.contains(":")) {
        val parts = time.split(":")
        if (parts.isNotEmpty() && parts[0].length <= 2) {
            parts[0].padStart(2, '0')
        } else "00"
    } else if (time.length >= 2) {
        time.substring(0, 2)
    } else {
        time.padStart(2, '0')
    }
}

private fun extractMinute(time: String): String {
    return if (time.contains(":")) {
        val parts = time.split(":")
        if (parts.size >= 2 && parts[1].length <= 2) {
            parts[1].padStart(2, '0')
        } else "00"
    } else if (time.length >= 4) {
        time.substring(2, 4)
    } else "00"
}

private fun formatTime(hour: String, minute: String): String {
    val h = hour.ifEmpty { "00" }.padStart(2, '0')
    val m = minute.ifEmpty { "00" }.padStart(2, '0')
    return "$h:$m"
}