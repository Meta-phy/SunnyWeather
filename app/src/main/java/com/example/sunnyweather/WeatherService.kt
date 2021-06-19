package com.example.sunnyweather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.sunnyweather.logic.model.getSky
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    override fun onCreate() {
        super.onCreate()

    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val placeName=intent.getStringExtra("placeName") ?: ""
        val temp=intent.getStringExtra("temp") ?: ""
        val text=intent.getStringExtra("text") ?: ""
        val icon=intent.getStringExtra("icon") ?: ""
        Log.d("WeatherService", "onCreate executed")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("my_service", "前台Service通知",
                NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "my_service")
            .setContentTitle("$placeName $text $temp")
            .setContentText("更新时间:"+getTime())
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, getSky(icon).icon))
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    fun getTime(): String? {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }
}