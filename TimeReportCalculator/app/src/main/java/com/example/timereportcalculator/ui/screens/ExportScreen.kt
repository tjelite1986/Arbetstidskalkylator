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

@Composable
fun ExportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.FileUpload,
                    contentDescription = "Export",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colors.primary
                )
                
                Text(
                    text = "Export & Backup",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                
                Text(
                    text = "Här kommer du att kunna:",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExportItem("📄", "Exportera till PDF")
                    ExportItem("📊", "Excel-rapporter med diagram")
                    ExportItem("📧", "Skicka via email")
                    ExportItem("☁️", "Automatiska backups")
                    ExportItem("🔄", "Synka mellan enheter")
                    ExportItem("🎨", "Anpassade rapportmallar")
                }
                
                Divider()
                
                Text(
                    text = "Avancerad export kommer snart...",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ExportItem(icon: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}