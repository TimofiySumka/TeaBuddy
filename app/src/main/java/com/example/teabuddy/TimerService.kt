package com.example.teabuddy

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import utils.NotificationHelper

class TimerService : Service() {

    companion object {
        const val CHANNEL_ID = "timer_service_channel"
        const val CHANNEL_NAME = "Timer Service"
        const val NOTIFICATION_ID = 2
        const val EXTRA_TIME = "EXTRA_TIME"
        const val EXTRA_TIME_REMAINING = "EXTRA_TIME_REMAINING"
        var working: Boolean = false
        var remainingTime: Long = 0L
    }

    private var timer: CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timeMillis = intent?.getLongExtra(EXTRA_TIME, 0L) ?: 0L
        val remainingTime = intent?.getLongExtra(EXTRA_TIME_REMAINING, 0L) ?: 0L

        if (timeMillis > 0) {
            startTimer(timeMillis)
        } else if (remainingTime > 0) {
            startTimer(remainingTime)
        } else {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startTimer(totalMillis: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        timer?.cancel()
        working = true
        remainingTime = totalMillis
        timer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                // Отправляем обновление через LocalBroadcastManager
                val intent = Intent("com.example.teabuddy.TIMER_UPDATE")

                // Оновлення сповіщення
                updateNotification(millisUntilFinished)
                intent.putExtra("remainingTime", millisUntilFinished)
                LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(intent)
            }

            override fun onFinish() {
                NotificationHelper.sendNotification(
                    this@TimerService,
                    "Ваш чай готов!",
                    "Ваш таймер закінчив відлік! :)"
                )
                working = false
                stopSelf()
            }
        }.start()
    }

    private fun createNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.tealeaf)
            .setContentTitle("Tea Buddy Таймер")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private val handler = Handler(Looper.getMainLooper())
    private fun updateNotification(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        val content = String.format("%02d:%02d залишилось", minutes, seconds)

        handler.post {
            val notification = createNotification(content)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
