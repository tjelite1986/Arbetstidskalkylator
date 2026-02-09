package com.example.timereportcalculator.timer

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timereportcalculator.MainActivity
import com.example.timereportcalculator.R
import com.example.timereportcalculator.data.TimerState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class TimerNotificationService : Service() {
    
    companion object {
        const val CHANNEL_ID = "timer_service_channel"
        const val NOTIFICATION_ID = 1
        
        const val ACTION_START_TIMER = "START_TIMER"
        const val ACTION_STOP_TIMER = "STOP_TIMER"
        const val ACTION_START_BREAK = "START_BREAK"
        const val ACTION_END_BREAK = "END_BREAK"
        
        // Shared settings for earnings calculation
        var sharedSettings: com.example.timereportcalculator.data.Settings? = null
        
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Arbetstimer",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Visar pågående arbetstid och kontroller"
                    setShowBadge(false)
                    setSound(null, null)
                }
                
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
    
    private lateinit var workSessionManager: WorkSessionManager
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    override fun onCreate() {
        super.onCreate()
        // Use singleton instance
        workSessionManager = WorkSessionManager.getInstance()
        createNotificationChannel(this)
        startForegroundService()
        observeTimerState()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TIMER -> {
                // Timer already started in UI, just ensure service is running
                android.util.Log.d("TimerService", "Service started for timer")
            }
            ACTION_STOP_TIMER -> {
                workSessionManager.stopWorkSession(sharedSettings)
                android.util.Log.d("TimerService", "Timer stopped via notification")
                stopSelf()
            }
            ACTION_START_BREAK -> {
                workSessionManager.startBreak()
                android.util.Log.d("TimerService", "Break started via notification")
            }
            ACTION_END_BREAK -> {
                workSessionManager.endBreak()
                android.util.Log.d("TimerService", "Break ended via notification")
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
    
    private fun startForegroundService() {
        try {
            val notification = createNotification("Arbetstimer", "Redo att starta")
            startForeground(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            // Fallback for older Android versions or permission issues
            e.printStackTrace()
        }
    }
    
    private fun observeTimerState() {
        serviceScope.launch {
            // Update notification every second while service is running
            while (true) {
                val session = workSessionManager.currentSession.value
                val state = workSessionManager.timerState.value
                
                // Stop service if no active session
                if (session == null && state == TimerState.STOPPED) {
                    android.util.Log.d("TimerService", "No active session, stopping service")
                    stopSelf()
                    break
                }
                
                // Update session earnings if we have settings
                sharedSettings?.let { settings ->
                    workSessionManager.updateSessionEarnings(settings)
                }
                
                updateNotification(session, state)
                delay(1000)
            }
        }
    }
    
    private fun updateNotification(session: com.example.timereportcalculator.data.ActiveWorkSession?, state: TimerState) {
        val notification = when (state) {
            TimerState.RUNNING -> {
                val workTime = session?.let { 
                    val minutes = it.getWorkDurationMinutes()
                    "${minutes / 60}:${String.format("%02d", minutes % 60)}"
                } ?: "00:00"
                val earnings = session?.currentEarnings?.totalPay ?: 0.0
                createNotification(
                    title = "Arbetar • $workTime",
                    content = "Intjänat: ${String.format("%.0f", earnings)} kr",
                    showStopAction = true,
                    showBreakAction = true
                )
            }
            TimerState.ON_BREAK -> {
                val workTime = session?.let { 
                    val minutes = it.getWorkDurationMinutes()
                    "${minutes / 60}:${String.format("%02d", minutes % 60)}"
                } ?: "00:00"
                val breakTime = session?.getCurrentBreakDurationMinutes() ?: 0
                val earnings = session?.currentEarnings?.totalPay ?: 0.0
                createNotification(
                    title = "Rast • ${breakTime} min",
                    content = "$workTime arbetstid • ${String.format("%.0f", earnings)} kr",
                    showStopAction = true,
                    showEndBreakAction = true
                )
            }
            TimerState.PAUSED -> {
                val earnings = session?.currentEarnings?.totalPay ?: 0.0
                createNotification(
                    title = "Pausad",
                    content = "Intjänat: ${String.format("%.0f", earnings)} kr"
                )
            }
            TimerState.STOPPED -> {
                createNotification(
                    title = "Arbetstimer",
                    content = "Redo att starta"
                )
            }
        }
        
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
    }
    
    private fun createNotification(
        title: String,
        content: String,
        showStopAction: Boolean = false,
        showBreakAction: Boolean = false,
        showEndBreakAction: Boolean = false
    ): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer) // You'll need to add this icon
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(openAppPendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        
        // Add action buttons
        if (showStopAction) {
            val stopIntent = Intent(this, TimerNotificationService::class.java).apply {
                action = ACTION_STOP_TIMER
            }
            val stopPendingIntent = PendingIntent.getService(
                this, 1, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(
                R.drawable.ic_stop, // You'll need to add this icon
                "Stoppa",
                stopPendingIntent
            )
        }
        
        if (showBreakAction) {
            val breakIntent = Intent(this, TimerNotificationService::class.java).apply {
                action = ACTION_START_BREAK
            }
            val breakPendingIntent = PendingIntent.getService(
                this, 2, breakIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(
                R.drawable.ic_coffee, // You'll need to add this icon
                "Rast",
                breakPendingIntent
            )
        }
        
        if (showEndBreakAction) {
            val endBreakIntent = Intent(this, TimerNotificationService::class.java).apply {
                action = ACTION_END_BREAK
            }
            val endBreakPendingIntent = PendingIntent.getService(
                this, 3, endBreakIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(
                R.drawable.ic_play, // You'll need to add this icon
                "Fortsätt",
                endBreakPendingIntent
            )
        }
        
        return builder.build()
    }
}