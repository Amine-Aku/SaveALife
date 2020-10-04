package com.impression.savealife.api

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.impression.savealife.models.Cst

open class App: Application() {

    override fun onCreate() {
        super.onCreate()

        ApiClient.getRetrofitInstance()
        createNotificationChannels()

    }

    private fun createNotificationChannels(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val channel1 = NotificationChannel(Cst.CHANNEL_1_ID, "New Post Notification", NotificationManager.IMPORTANCE_HIGH)
            channel1.description = "When an user posts a new SOS in your subscribed Topic(city)"
            channel1.importance = NotificationManager.IMPORTANCE_HIGH
            channel1.lightColor = Color.BLUE
            notificationManager.createNotificationChannel(channel1)
        }
    }
}
