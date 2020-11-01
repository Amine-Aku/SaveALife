package com.impression.savealife.api

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.google.firebase.iid.FirebaseInstanceId
import com.impression.savealife.R
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
            val channel1 = NotificationChannel(Cst.CHANNEL_1_ID_NewPost, getString(R.string.channel1_title), NotificationManager.IMPORTANCE_HIGH)
            channel1.description = getString(R.string.channel1_description)
            channel1.importance = NotificationManager.IMPORTANCE_HIGH
            channel1.lightColor = Color.BLUE

            val channel2 = NotificationChannel(Cst.CHANNEL_2_ID_Donation, getString(R.string.channel2_title), NotificationManager.IMPORTANCE_HIGH)
            channel2.description = getString(R.string.channel2_description)
            channel2.importance = NotificationManager.IMPORTANCE_HIGH
            channel2.lightColor = Color.BLUE

            val channel3 = NotificationChannel(Cst.CHANNEL_3_ID_uCanDonate, getString(R.string.channel3_title), NotificationManager.IMPORTANCE_HIGH)
            channel3.description = getString(R.string.channel3_description)
            channel3.importance = NotificationManager.IMPORTANCE_HIGH
            channel3.lightColor = Color.BLUE

            val channels = listOf(channel1, channel2, channel3)
            notificationManager.createNotificationChannels(channels)
        }
    }
}
