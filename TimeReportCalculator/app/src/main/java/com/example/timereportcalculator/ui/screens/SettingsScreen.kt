package com.example.timereportcalculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.Settings

@Composable
fun SettingsScreen(
    settings: Settings = Settings(),
    onSettingsChange: (Settings) -> Unit = {}
) {
    var showAdvancedSettings by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    
    if (showAdvancedSettings) {
        AdvancedSettingsScreen(
            settings = settings,
            onSettingsChange = onSettingsChange
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.primary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colors.primary.copy(alpha = 0.3f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Inställningar",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Inställningar",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    text = "Anpassa din tidsrapportering",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Quick Settings Section
        Text(
            text = "Snabbinställningar",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Settings Items
        SettingsItem(
            icon = Icons.Default.AttachMoney,
            title = "Grundlön & Skatt",
            subtitle = "${String.format("%.0f", settings.basePay)} kr/tim • ${String.format("%.1f", settings.taxRate)}% skatt",
            iconColor = Color(0xFF4CAF50),
            onClick = { showAdvancedSettings = true }
        )

        SettingsItem(
            icon = Icons.Default.BusinessCenter,
            title = "Avtalsnivå",
            subtitle = settings.contractLevel.displayName,
            iconColor = Color(0xFFFF9800),
            onClick = { showAdvancedSettings = true }
        )

        SettingsItem(
            icon = Icons.Default.Schedule,
            title = "OB-satser",
            subtitle = "Butik • ${String.format("%.0f", settings.obRates.butikWeekdayAfter2000 * 100)}% kvällar",
            iconColor = Color(0xFF9C27B0),
            onClick = { showAdvancedSettings = true }
        )

        SettingsItem(
            icon = Icons.Default.AccessTime,
            title = "Arbetstider & Raster",
            subtitle = "${settings.workTimeSettings.defaultStartTime} - ${settings.workTimeSettings.defaultEndTime}",
            iconColor = Color(0xFF00BCD4),
            onClick = { showAdvancedSettings = true }
        )

        SettingsItem(
            icon = Icons.Default.Palette,
            title = "Tema & Utseende",
            subtitle = "Ljust tema • Standard",
            iconColor = Color(0xFF673AB7),
            onClick = { showThemeDialog = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Advanced Settings Button
        Button(
            onClick = { showAdvancedSettings = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Öppna alla inställningar")
        }

        // Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.7f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Om inställningarna\nAlla inställningar följer Detaljhandelsavtalet 2025-2026 och sparas automatiskt. Tryck på en kategori för att ändra inställningar.",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }

    // Theme Dialog
    if (showThemeDialog) {
        com.example.timereportcalculator.ui.components.ThemeSelectionDialog(
            isOpen = showThemeDialog,
            currentThemeSettings = settings.themeSettings,
            onThemeSettingsChange = { newThemeSettings ->
                onSettingsChange(settings.copy(themeSettings = newThemeSettings))
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}