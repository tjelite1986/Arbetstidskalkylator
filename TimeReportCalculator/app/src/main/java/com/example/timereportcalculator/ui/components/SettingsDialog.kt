package com.example.timereportcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.timereportcalculator.R
import com.example.timereportcalculator.data.Settings

@Composable
fun SettingsDialog(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit,
    onDismiss: () -> Unit,
    onFilePickerClick: () -> Unit = {}
) {
    var basePayText by remember { mutableStateOf(settings.basePay.toString()) }
    var taxRateText by remember { mutableStateOf(settings.taxRate.toString()) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = basePayText,
                    onValueChange = { newValue ->
                        basePayText = newValue
                        try {
                            val basePay = newValue.toDoubleOrNull() ?: settings.basePay
                            onSettingsChange(settings.copy(basePay = basePay))
                        } catch (e: NumberFormatException) {
                            // Invalid number format, don't update
                        }
                    },
                    label = { Text(stringResource(R.string.base_pay)) },
                    placeholder = { Text("162.98") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = taxRateText,
                    onValueChange = { newValue ->
                        taxRateText = newValue
                        try {
                            val taxRate = newValue.toDoubleOrNull() ?: settings.taxRate
                            onSettingsChange(settings.copy(taxRate = taxRate))
                        } catch (e: NumberFormatException) {
                            // Invalid number format, don't update
                        }
                    },
                    label = { Text(stringResource(R.string.tax_rate)) },
                    placeholder = { Text("30") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = onFilePickerClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filhantering")
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}