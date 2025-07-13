package com.example.timereportcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timereportcalculator.data.*
import com.example.timereportcalculator.ui.components.TimePickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Hjälpfunktion för att formatera decimaler på ett användarvänligt sätt
private fun formatDecimal(value: Double): String {
    return if (value == value.toInt().toDouble()) {
        value.toInt().toString()
    } else {
        DecimalFormat("#.##", DecimalFormatSymbols(Locale.US)).format(value)
    }
}

@Composable
fun AdvancedSettingsScreen(
    settings: Settings = Settings(),
    onSettingsChange: (Settings) -> Unit = {}
) {
    var expandedSections by remember { mutableStateOf(setOf<String>()) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Avancerade inställningar",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "Avancerade Inställningar",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
        
        item {
            // Basic Settings Section
            SettingsSection(
                title = "💰 Grundinställningar",
                isExpanded = expandedSections.contains("basic"),
                onExpandChanged = { expanded ->
                    expandedSections = if (expanded) {
                        expandedSections + "basic"
                    } else {
                        expandedSections - "basic"
                    }
                }
            ) {
                BasicSettingsContent(
                    settings = settings,
                    onSettingsChange = onSettingsChange
                )
            }
        }
        
        item {
            // Contract Level Section
            SettingsSection(
                title = "📋 Avtalsnivå",
                isExpanded = expandedSections.contains("contract"),
                onExpandChanged = { expanded ->
                    expandedSections = if (expanded) {
                        expandedSections + "contract"
                    } else {
                        expandedSections - "contract"
                    }
                }
            ) {
                ContractLevelContent(
                    settings = settings,
                    onSettingsChange = onSettingsChange
                )
            }
        }
        
        item {
            // OB Rates Section
            SettingsSection(
                title = "🌙 OB-satser",
                isExpanded = expandedSections.contains("ob"),
                onExpandChanged = { expanded ->
                    expandedSections = if (expanded) {
                        expandedSections + "ob"
                    } else {
                        expandedSections - "ob"
                    }
                }
            ) {
                OBRatesContent(
                    settings = settings,
                    onSettingsChange = onSettingsChange
                )
            }
        }
        
        item {
            // Vacation Section
            SettingsSection(
                title = "🏖️ Semesterersättning",
                isExpanded = expandedSections.contains("vacation"),
                onExpandChanged = { expanded ->
                    expandedSections = if (expanded) {
                        expandedSections + "vacation"
                    } else {
                        expandedSections - "vacation"
                    }
                }
            ) {
                VacationContent(
                    settings = settings,
                    onSettingsChange = onSettingsChange
                )
            }
        }
        
        item {
            // Work Time Settings Section
            SettingsSection(
                title = "⏰ Arbetstider & Raster",
                isExpanded = expandedSections.contains("worktime"),
                onExpandChanged = { expanded ->
                    expandedSections = if (expanded) {
                        expandedSections + "worktime"
                    } else {
                        expandedSections - "worktime"
                    }
                }
            ) {
                WorkTimeContent(
                    settings = settings,
                    onSettingsChange = onSettingsChange
                )
            }
        }
        
        item {
            // Holiday Settings Section
            SettingsSection(
                title = "📅 Helgdagar & Kalender",
                isExpanded = expandedSections.contains("holidays"),
                onExpandChanged = { expanded ->
                    expandedSections = if (expanded) {
                        expandedSections + "holidays"
                    } else {
                        expandedSections - "holidays"
                    }
                }
            ) {
                HolidayContent(
                    settings = settings,
                    onSettingsChange = onSettingsChange
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    isExpanded: Boolean,
    onExpandChanged: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { onExpandChanged(!isExpanded) }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Dölj" else "Visa"
                    )
                }
            }
            
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }
    }
}

@Composable
private fun BasicSettingsContent(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    var basePayText by remember(settings.basePay) { mutableStateOf(formatDecimal(settings.basePay)) }
    var taxRateText by remember(settings.taxRate) { mutableStateOf(settings.taxRate.toString()) }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = basePayText,
            onValueChange = { newValue ->
                basePayText = newValue
                // Tillåt tomma strängar och giltiga decimal-format
                if (newValue.isEmpty()) {
                    // Tillåt tom sträng för redigering
                } else if (newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    newValue.toDoubleOrNull()?.let { basePay ->
                        onSettingsChange(settings.copy(basePay = basePay))
                    }
                }
            },
            label = { Text("Grundlön (kr/tim)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        
        OutlinedTextField(
            value = taxRateText,
            onValueChange = { newValue ->
                taxRateText = newValue
                if (newValue.isEmpty()) {
                    // Tillåt tom sträng för redigering
                } else {
                    newValue.toDoubleOrNull()?.let { taxRate ->
                        onSettingsChange(settings.copy(taxRate = taxRate))
                    }
                }
            },
            label = { Text("Skattesats (%)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}

@Composable
private fun ContractLevelContent(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "📋 Detaljhandelsavtalet 2025-2026",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Minimilöner enligt gällande kollektivavtal mellan Handelsanställdas förbund och Svensk Handel",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        Text(
            text = "Åldersbaserade löner:",
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        // Age-based wages
        val ageLevels = listOf(
            ContractLevel.AGE_16, ContractLevel.AGE_17, 
            ContractLevel.AGE_18, ContractLevel.AGE_19
        )
        
        ageLevels.forEach { level ->
            ContractLevelItem(
                level = level,
                isSelected = settings.contractLevel == level,
                onSelect = {
                    onSettingsChange(settings.copy(
                        contractLevel = level,
                        basePay = level.minimumWage
                    ))
                }
            )
        }
        
        Text(
            text = "Erfarenhetsbaserade löner (från 18 år):",
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        
        // Experience-based wages
        val experienceLevels = listOf(
            ContractLevel.EXPERIENCE_1_YEAR, ContractLevel.EXPERIENCE_2_YEARS,
            ContractLevel.EXPERIENCE_3_PLUS_YEARS, ContractLevel.CUSTOM
        )
        
        experienceLevels.forEach { level ->
            ContractLevelItem(
                level = level,
                isSelected = settings.contractLevel == level,
                onSelect = {
                    onSettingsChange(settings.copy(
                        contractLevel = level,
                        basePay = level.minimumWage
                    ))
                }
            )
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
        ) {
            Text(
                text = "💡 När du väljer en avtalsnivå uppdateras automatiskt grundlönen till minimilönen för den nivån. Löner enligt Detaljhandelsavtalet 2025-2026.",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun ContractLevelItem(
    level: ContractLevel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        backgroundColor = if (isSelected) 
            MaterialTheme.colors.primary.copy(alpha = 0.1f) 
        else 
            MaterialTheme.colors.surface,
        elevation = if (isSelected) 4.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "${level.displayName} - ${String.format("%.2f", level.minimumWage)} kr/tim",
                    style = MaterialTheme.typography.body1,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
                Text(
                    text = level.description,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun OBRatesContent(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Workplace type selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "🏢 Välj arbetsplatstyp",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "OB-satserna varierar mellan butik och lager enligt Handelsavtalet",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(WorkplaceType.BUTIK, WorkplaceType.LAGER).forEach { workplaceType ->
                Button(
                    onClick = {
                        onSettingsChange(settings.copy(
                            obRates = settings.obRates.copy(workplaceType = workplaceType)
                        ))
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (settings.obRates.workplaceType == workplaceType) 
                            MaterialTheme.colors.primary 
                        else 
                            MaterialTheme.colors.surface
                    )
                ) {
                    Text(
                        text = workplaceType.displayName,
                        color = if (settings.obRates.workplaceType == workplaceType) 
                            Color.White 
                        else 
                            MaterialTheme.colors.onSurface
                    )
                }
            }
        }
        
        // Show OB rates based on workplace type
        when (settings.obRates.workplaceType) {
            WorkplaceType.BUTIK -> {
                ButikOBRatesSection(settings, onSettingsChange)
            }
            WorkplaceType.LAGER -> {
                LagerOBRatesSection(settings, onSettingsChange)
            }
        }
        
        Button(
            onClick = {
                // Reset to defaults
                onSettingsChange(settings.copy(obRates = OBRates()))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Återställ till standard")
        }
    }
}

@Composable
private fun ButikOBRatesSection(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    val butikOBFields = listOf(
        "Måndag-fredag 18:15-20:00" to settings.obRates.butikWeekday1815to2000,
        "Måndag-fredag efter 20:00" to settings.obRates.butikWeekdayAfter2000,
        "Lördag efter 12:00" to settings.obRates.butikSaturdayAfter1200,
        "Söndag hela dagen" to settings.obRates.butikSundayAllDay,
        "Helgdagar hela dagen" to settings.obRates.butikRedDayAllDay,
        "Coop: Måndag-lördag 05:00-06:00" to settings.obRates.butikCoopMorning0500to0600
    )
    
    // Lokala textstängar för varje fält
    val textValues = remember(settings.obRates) {
        butikOBFields.map { (_, value) -> mutableStateOf((value * 100).toString()) }
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "🛍️ OB-satser för butik enligt Handelsavtalet:",
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )
        
        butikOBFields.forEachIndexed { index, (label, _) ->
            OutlinedTextField(
                value = textValues[index].value,
                onValueChange = { newValue ->
                    textValues[index].value = newValue
                    if (newValue.isEmpty()) {
                        // Tillåt tom sträng för redigering
                    } else {
                        newValue.toDoubleOrNull()?.let { percentage ->
                            val rate = percentage / 100
                            val newRates = when (index) {
                                0 -> settings.obRates.copy(butikWeekday1815to2000 = rate)
                                1 -> settings.obRates.copy(butikWeekdayAfter2000 = rate)
                                2 -> settings.obRates.copy(butikSaturdayAfter1200 = rate)
                                3 -> settings.obRates.copy(butikSundayAllDay = rate)
                                4 -> settings.obRates.copy(butikRedDayAllDay = rate)
                                5 -> settings.obRates.copy(butikCoopMorning0500to0600 = rate)
                                else -> settings.obRates
                            }
                            onSettingsChange(settings.copy(obRates = newRates))
                        }
                    }
                },
                label = { Text(label) },
                trailingIcon = { Text("%") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
        }
    }
}

@Composable
private fun LagerOBRatesSection(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    val lagerOBFields = listOf(
        "Måndag 00:00-06:00" to settings.obRates.lagerMondayNight0000to0600,
        "Måndag-fredag 06:00-07:00" to settings.obRates.lagerWeekdayMorning0600to0700,
        "Måndag-fredag 18:00-23:00" to settings.obRates.lagerWeekdayEvening1800to2300,
        "Måndag-fredag 23:00-06:00" to settings.obRates.lagerWeekdayNight2300to0600,
        "Lördag 00:00-06:00" to settings.obRates.lagerSaturdayNight0000to0600,
        "Lördag 06:00-23:00" to settings.obRates.lagerSaturdayDay0600to2300,
        "Lördag 23:00-24:00" to settings.obRates.lagerSaturdayNight2300to2400,
        "Söndag hela dagen" to settings.obRates.lagerSundayAllDay,
        "Helgdagar hela dagen" to settings.obRates.lagerRedDayAllDay
    )
    
    // Lokala textstängar för varje fält
    val textValues = remember(settings.obRates) {
        lagerOBFields.map { (_, value) -> mutableStateOf((value * 100).toString()) }
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "📦 OB-satser för lager enligt Handelsavtalet:",
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )
        
        lagerOBFields.forEachIndexed { index, (label, _) ->
            OutlinedTextField(
                value = textValues[index].value,
                onValueChange = { newValue ->
                    textValues[index].value = newValue
                    if (newValue.isEmpty()) {
                        // Tillåt tom sträng för redigering
                    } else {
                        newValue.toDoubleOrNull()?.let { percentage ->
                            val rate = percentage / 100
                            val newRates = when (index) {
                                0 -> settings.obRates.copy(lagerMondayNight0000to0600 = rate)
                                1 -> settings.obRates.copy(lagerWeekdayMorning0600to0700 = rate)
                                2 -> settings.obRates.copy(lagerWeekdayEvening1800to2300 = rate)
                                3 -> settings.obRates.copy(lagerWeekdayNight2300to0600 = rate)
                                4 -> settings.obRates.copy(lagerSaturdayNight0000to0600 = rate)
                                5 -> settings.obRates.copy(lagerSaturdayDay0600to2300 = rate)
                                6 -> settings.obRates.copy(lagerSaturdayNight2300to2400 = rate)
                                7 -> settings.obRates.copy(lagerSundayAllDay = rate)
                                8 -> settings.obRates.copy(lagerRedDayAllDay = rate)
                                else -> settings.obRates
                            }
                            onSettingsChange(settings.copy(obRates = newRates))
                        }
                    }
                },
                label = { Text(label) },
                trailingIcon = { Text("%") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
        }
    }
}

@Composable
private fun VacationContent(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    var vacationRateText by remember(settings.vacationRate) { mutableStateOf(settings.vacationRate.toString()) }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Semesterersättning enligt kollektivavtal:",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
        )
        
        OutlinedTextField(
            value = vacationRateText,
            onValueChange = { newValue ->
                vacationRateText = newValue
                if (newValue.isEmpty()) {
                    // Tillåt tom sträng för redigering
                } else {
                    newValue.toDoubleOrNull()?.let { rate ->
                        onSettingsChange(settings.copy(vacationRate = rate))
                    }
                }
            },
            label = { Text("Semesterersättning") },
            trailingIcon = { Text("%") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "ℹ️ Information om semesterersättning:",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Standard enligt Handelsavtalet: 12%\n• Beräknas på all intjänad lön\n• Utbetalas när semester tas ut eller vid anställningens slut",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun WorkTimeContent(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Default work times
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "⏰ Standard arbetstider",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "Dessa tider används som standardvärden vid nya tidrapporter",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Start time picker
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Starttid",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedButton(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Välj starttid",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = settings.workTimeSettings.defaultStartTime,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            
            // End time picker
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sluttid",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedButton(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Välj sluttid",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = settings.workTimeSettings.defaultEndTime,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
        
        // Automatic breaks
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🍽️ Automatiska raster",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = settings.workTimeSettings.automaticBreaks,
                        onCheckedChange = { enabled ->
                            onSettingsChange(settings.copy(
                                workTimeSettings = settings.workTimeSettings.copy(
                                    automaticBreaks = enabled
                                )
                            ))
                        }
                    )
                }
                Text(
                    text = "Beräkna automatiskt raster baserat på arbetslängd. Vid längre arbetspass väljs den största tillämpliga rasten.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        if (settings.workTimeSettings.automaticBreaks) {
            var firstThresholdText by remember(settings.workTimeSettings.firstBreakThreshold) { mutableStateOf(settings.workTimeSettings.firstBreakThreshold.toString()) }
            var firstMinutesText by remember(settings.workTimeSettings.firstBreakMinutes) { mutableStateOf(settings.workTimeSettings.firstBreakMinutes.toString()) }
            var secondThresholdText by remember(settings.workTimeSettings.secondBreakThreshold) { mutableStateOf(settings.workTimeSettings.secondBreakThreshold.toString()) }
            var secondMinutesText by remember(settings.workTimeSettings.secondBreakMinutes) { mutableStateOf(settings.workTimeSettings.secondBreakMinutes.toString()) }
            var thirdThresholdText by remember(settings.workTimeSettings.thirdBreakThreshold) { mutableStateOf(settings.workTimeSettings.thirdBreakThreshold.toString()) }
            var thirdMinutesText by remember(settings.workTimeSettings.thirdBreakMinutes) { mutableStateOf(settings.workTimeSettings.thirdBreakMinutes.toString()) }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Rast-inställningar:",
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold
                )
                
                // Första rast
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = firstThresholdText,
                        onValueChange = { newValue ->
                            firstThresholdText = newValue
                            if (newValue.isEmpty()) {
                                // Tillåt tom sträng för redigering
                            } else {
                                newValue.toDoubleOrNull()?.let { threshold ->
                                    onSettingsChange(settings.copy(
                                        workTimeSettings = settings.workTimeSettings.copy(
                                            firstBreakThreshold = threshold
                                        )
                                    ))
                                }
                            }
                        },
                        label = { Text("Första rast vid (tim)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = firstMinutesText,
                        onValueChange = { newValue ->
                            firstMinutesText = newValue
                            if (newValue.isEmpty()) {
                                // Tillåt tom sträng för redigering
                            } else {
                                newValue.toIntOrNull()?.let { minutes ->
                                    onSettingsChange(settings.copy(
                                        workTimeSettings = settings.workTimeSettings.copy(
                                            firstBreakMinutes = minutes
                                        )
                                    ))
                                }
                            }
                        },
                        label = { Text("Minuter") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
                
                // Andra rast
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = secondThresholdText,
                        onValueChange = { newValue ->
                            secondThresholdText = newValue
                            if (newValue.isEmpty()) {
                                // Tillåt tom sträng för redigering
                            } else {
                                newValue.toDoubleOrNull()?.let { threshold ->
                                    onSettingsChange(settings.copy(
                                        workTimeSettings = settings.workTimeSettings.copy(
                                            secondBreakThreshold = threshold
                                        )
                                    ))
                                }
                            }
                        },
                        label = { Text("Andra rast vid (tim)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = secondMinutesText,
                        onValueChange = { newValue ->
                            secondMinutesText = newValue
                            if (newValue.isEmpty()) {
                                // Tillåt tom sträng för redigering
                            } else {
                                newValue.toIntOrNull()?.let { minutes ->
                                    onSettingsChange(settings.copy(
                                        workTimeSettings = settings.workTimeSettings.copy(
                                            secondBreakMinutes = minutes
                                        )
                                    ))
                                }
                            }
                        },
                        label = { Text("Minuter") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
                
                // Tredje rast
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = thirdThresholdText,
                        onValueChange = { newValue ->
                            thirdThresholdText = newValue
                            if (newValue.isEmpty()) {
                                // Tillåt tom sträng för redigering
                            } else {
                                newValue.toDoubleOrNull()?.let { threshold ->
                                    onSettingsChange(settings.copy(
                                        workTimeSettings = settings.workTimeSettings.copy(
                                            thirdBreakThreshold = threshold
                                        )
                                    ))
                                }
                            }
                        },
                        label = { Text("Tredje rast vid (tim)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = thirdMinutesText,
                        onValueChange = { newValue ->
                            thirdMinutesText = newValue
                            if (newValue.isEmpty()) {
                                // Tillåt tom sträng för redigering
                            } else {
                                newValue.toIntOrNull()?.let { minutes ->
                                    onSettingsChange(settings.copy(
                                        workTimeSettings = settings.workTimeSettings.copy(
                                            thirdBreakMinutes = minutes
                                        )
                                    ))
                                }
                            }
                        },
                        label = { Text("Minuter") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
                
                // Information om automatiska raster
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "ℹ️ Hur automatiska raster fungerar:",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.secondary
                        )
                        Text(
                            text = "• Vid 4 timmar arbete → ${settings.workTimeSettings.firstBreakMinutes} min rast\n" +
                                   "• Vid 6 timmar arbete → ${settings.workTimeSettings.secondBreakMinutes} min rast\n" +
                                   "• Vid 8 timmar arbete → ${settings.workTimeSettings.thirdBreakMinutes} min rast\n\n" +
                                   "Systemet väljer automatiskt den längsta rasten som gäller för arbetstiden.",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
    
    // Time pickers
    if (showStartTimePicker) {
        TimePickerDialog(
            selectedTime = try { 
                java.time.LocalTime.parse(settings.workTimeSettings.defaultStartTime) 
            } catch (e: Exception) { 
                java.time.LocalTime.of(8, 0) 
            },
            onTimeSelected = { selectedTime ->
                onSettingsChange(settings.copy(
                    workTimeSettings = settings.workTimeSettings.copy(
                        defaultStartTime = selectedTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                    )
                ))
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false },
            title = "Välj starttid"
        )
    }
    
    if (showEndTimePicker) {
        TimePickerDialog(
            selectedTime = try { 
                java.time.LocalTime.parse(settings.workTimeSettings.defaultEndTime) 
            } catch (e: Exception) { 
                java.time.LocalTime.of(17, 0) 
            },
            onTimeSelected = { selectedTime ->
                onSettingsChange(settings.copy(
                    workTimeSettings = settings.workTimeSettings.copy(
                        defaultEndTime = selectedTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                    )
                ))
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false },
            title = "Välj sluttid"
        )
    }
}

@Composable
private fun HolidayContent(
    settings: Settings,
    onSettingsChange: (Settings) -> Unit
) {
    var showAddHolidayDialog by remember { mutableStateOf(false) }
    var newHolidayDate by remember { mutableStateOf("") }
    var newHolidayName by remember { mutableStateOf("") }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Automatic holiday detection
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🇸🇪 Svenska helgdagar",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = settings.automaticHolidayDetection,
                        onCheckedChange = { enabled ->
                            onSettingsChange(settings.copy(automaticHolidayDetection = enabled))
                        }
                    )
                }
                Text(
                    text = "Hämta automatiskt svenska helgdagar (Nyår, Påsk, Midsommar, etc.)",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        // Calendar settings
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "📅 Kalenderinställningar",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vecka börjar på måndag")
                    Switch(
                        checked = settings.calendarSettings.weekStartsOnMonday,
                        onCheckedChange = { enabled ->
                            onSettingsChange(settings.copy(
                                calendarSettings = settings.calendarSettings.copy(
                                    weekStartsOnMonday = enabled
                                )
                            ))
                        }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Månadsvy som standard")
                    Switch(
                        checked = settings.calendarSettings.monthViewAsDefault,
                        onCheckedChange = { enabled ->
                            onSettingsChange(settings.copy(
                                calendarSettings = settings.calendarSettings.copy(
                                    monthViewAsDefault = enabled
                                )
                            ))
                        }
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Visa veckonummer")
                    Switch(
                        checked = settings.calendarSettings.showWeekNumbers,
                        onCheckedChange = { enabled ->
                            onSettingsChange(settings.copy(
                                calendarSettings = settings.calendarSettings.copy(
                                    showWeekNumbers = enabled
                                )
                            ))
                        }
                    )
                }
            }
        }
        
        // Custom holidays
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏢 Företagsspecifika helgdagar",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { showAddHolidayDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lägg till")
                    }
                }
                
                Text(
                    text = "Lägg till företagsspecifika lediga dagar",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                if (settings.customHolidays.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    settings.customHolidays.forEach { holiday ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            backgroundColor = MaterialTheme.colors.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = holiday.name,
                                        style = MaterialTheme.typography.body1,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = holiday.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val updatedHolidays = settings.customHolidays.filter { it != holiday }
                                        onSettingsChange(settings.copy(customHolidays = updatedHolidays))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Ta bort",
                                        tint = MaterialTheme.colors.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Add holiday dialog
    if (showAddHolidayDialog) {
        AlertDialog(
            onDismissRequest = { showAddHolidayDialog = false },
            title = { Text("Lägg till helgdag") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newHolidayDate,
                        onValueChange = { newHolidayDate = it },
                        label = { Text("Datum (YYYY-MM-DD)") },
                        placeholder = { Text("2025-12-24") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newHolidayName,
                        onValueChange = { newHolidayName = it },
                        label = { Text("Namn") },
                        placeholder = { Text("Företagets årsdag") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            val date = LocalDate.parse(newHolidayDate)
                            if (newHolidayName.isNotBlank()) {
                                val newHoliday = Holiday(date, newHolidayName, isCustom = true)
                                val updatedHolidays = settings.customHolidays + newHoliday
                                onSettingsChange(settings.copy(customHolidays = updatedHolidays))
                                newHolidayDate = ""
                                newHolidayName = ""
                                showAddHolidayDialog = false
                            }
                        } catch (e: Exception) {
                            // Invalid date format - could show error message
                        }
                    }
                ) {
                    Text("Lägg till")
                }
            },
            dismissButton = {
                Button(
                    onClick = { 
                        showAddHolidayDialog = false
                        newHolidayDate = ""
                        newHolidayName = ""
                    }
                ) {
                    Text("Avbryt")
                }
            }
        )
    }
}