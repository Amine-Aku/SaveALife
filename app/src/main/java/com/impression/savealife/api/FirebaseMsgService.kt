package com.impression.savealife.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.impression.savealife.R
import com.impression.savealife.activities.HomeActivity

open class FirebaseMsgService() : FirebaseMessagingService() {

    private val TAG = "FirebaseMsgService"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        remoteMessage!!.notification?.let {
            val title = it.title
            val body = it.body
            Log.d(TAG, "onMessageReceived: Title : $title\nBody : $body")
            val data = remoteMessage.data
            if(data.isNotEmpty()){
                Log.d(TAG, "onMessageReceived: msg data payload :  ${remoteMessage.data}")
                val currentUserId = 1
                if(data.containsKey("user_id") && data["user_id"] == currentUserId.toString())
                    Log.d(TAG, "onMessageReceived: msg not destined to this user: $currentUserId")
                else
                    sendNotification(title!!, body!!)
            }
            else
                sendNotification(title!!, body!!)
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "New Post Notification"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_save_a_life)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "onNewToken: $token")
    }
}
