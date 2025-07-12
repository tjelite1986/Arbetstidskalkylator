package com.example.timereportcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timereportcalculator.data.TimeEntry
import com.example.timereportcalculator.data.Settings
import com.example.timereportcalculator.ui.components.CalendarView

@Composable
fun StatisticsScreen(
    timeEntries: List<TimeEntry> = emptyList(),
    settings: Settings = Settings()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = "Kalender",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colors.primary
                )
                
                Column {
                    Text(
                        text = "Kalendervy",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    
                    Text(
                        text = "Visa dina arbetstider i kalender",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Calendar View
        CalendarView(
            timeEntries = timeEntries,
            settings = settings,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        )
    }
}