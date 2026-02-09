package com.example.timereportcalculator.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timereportcalculator.data.*
import com.example.timereportcalculator.timer.WorkSessionManager
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter

@Composable
fun LiveTimerCard(
    sessionManager: WorkSessionManager,
    settings: Settings,
    modifier: Modifier = Modifier,
    onStartShift: () -> Unit = {},
    onStopShift: () -> Unit = {},
    onStartBreak: () -> Unit = {},
    onEndBreak: () -> Unit = {}
) {
    val currentSession by sessionManager.currentSession.collectAsState()
    val timerState by sessionManager.timerState.collectAsState()
    
    // Update session earnings every second when active
    LaunchedEffect(currentSession?.id) {
        while (currentSession != null && timerState != TimerState.STOPPED) {
            try {
                sessionManager.updateSessionEarnings(settings)
                delay(1000)
            } catch (e: Exception) {
                // Skip logging cancellation exceptions
                if (e.message?.contains("LeftComposition") != true) {
                    android.util.Log.e("LiveTimerCard", "Timer update error", e)
                }
                delay(5000) // Wait longer on error
            }
        }
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = when (timerState) {
            TimerState.RUNNING -> MaterialTheme.colors.primary.copy(alpha = 0.1f)
            TimerState.ON_BREAK -> MaterialTheme.colors.secondary.copy(alpha = 0.1f)
            TimerState.PAUSED -> MaterialTheme.colors.error.copy(alpha = 0.1f)
            TimerState.STOPPED -> MaterialTheme.colors.surface
        }
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Timer",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                
                TimerStatusChip(timerState)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (currentSession != null) {
                ActiveSessionDisplay(
                    session = currentSession!!,
                    timerState = timerState,
                    onStartBreak = onStartBreak,
                    onEndBreak = onEndBreak,
                    onStopShift = onStopShift
                )
            } else {
                InactiveTimerDisplay(onStartShift = onStartShift)
            }
        }
    }
}

@Composable
private fun TimerStatusChip(state: TimerState) {
    val (color, text, icon) = when (state) {
        TimerState.RUNNING -> Triple(Color(0xFF4CAF50), "Arbetar", Icons.Default.PlayArrow)
        TimerState.ON_BREAK -> Triple(Color(0xFFFF9800), "Rast", Icons.Default.Coffee)
        TimerState.PAUSED -> Triple(Color(0xFFF44336), "Pausad", Icons.Default.Pause)
        TimerState.STOPPED -> Triple(Color.Gray, "Stoppad", Icons.Default.Stop)
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = color,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ActiveSessionDisplay(
    session: ActiveWorkSession,
    timerState: TimerState,
    onStartBreak: () -> Unit,
    onEndBreak: () -> Unit,
    onStopShift: () -> Unit
) {
    // Work duration display
    val workMinutes = session.getWorkDurationMinutes()
    val hours = workMinutes / 60
    val minutes = workMinutes % 60
    
    // Animated pulse effect for running state
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Time display
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = String.format("%02d:%02d", hours, minutes),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary.copy(
                alpha = if (timerState == TimerState.RUNNING) pulseAlpha else 1f
            )
        )
        
        Text(
            text = "arbetstid",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Earnings display
    EarningsDisplaySection(session.currentEarnings)
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Break information
    if (session.getTotalBreakMinutes() > 0 || session.isOnBreak) {
        BreakInfoSection(session)
        Spacer(modifier = Modifier.height(16.dp))
    }
    
    // Control buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (timerState == TimerState.ON_BREAK) {
            Button(
                onClick = onEndBreak,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Avsluta rast")
            }
        } else {
            Button(
                onClick = onStartBreak,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                )
            ) {
                Icon(Icons.Default.Coffee, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ta rast")
            }
        }
        
        Button(
            onClick = onStopShift,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error
            )
        ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Avsluta")
        }
    }
}

@Composable
private fun EarningsDisplaySection(earnings: SessionEarnings) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EarningsItem("Grundlön", "${String.format("%.2f", earnings.basePay)} kr")
            EarningsItem("OB-tillägg", "${String.format("%.2f", earnings.obPay)} kr")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EarningsItem("Semesterersättning", "${String.format("%.2f", earnings.vacationPay)} kr")
            EarningsItem("Total", "${String.format("%.2f", earnings.totalPay)} kr", isTotal = true)
        }
        
        if (earnings.isCurrentlyEligibleForOB) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "🎯 Aktuell OB: +${(earnings.currentOBRate * 100).toInt()}%",
                    modifier = Modifier.padding(8.dp),
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun EarningsItem(label: String, value: String, isTotal: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium,
            fontSize = if (isTotal) 16.sp else 14.sp,
            color = if (isTotal) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun BreakInfoSection(session: ActiveWorkSession) {
    Surface(
        color = MaterialTheme.colors.secondary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Coffee,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Raster totalt:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                )
            }
            
            Text(
                text = "${session.getTotalBreakMinutes()} min",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun InactiveTimerDisplay(onStartShift: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        Icon(
            Icons.Default.Timer,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Redo att börja arbeta?",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
        )
        
        Text(
            text = "Tryck för att starta din arbetstid med live-beräkningar",
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onStartShift,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Starta arbetspass")
        }
    }
}