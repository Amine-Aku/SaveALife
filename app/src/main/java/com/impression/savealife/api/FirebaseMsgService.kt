package com.impression.savealife.api

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.impression.savealife.R
import com.impression.savealife.activities.HomeActivity
import com.impression.savealife.activities.NotificationsActivity
import com.impression.savealife.models.Cst

open class FirebaseMsgService() : FirebaseMessagingService() {

    private val TAG = "FirebaseMsgService"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
//        receiveNotification(remoteMessage)
        receiveNotificationDataOnly(remoteMessage)
    }

    private fun receiveNotification(remoteMessage: RemoteMessage?) {
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
                {
                    sendNotification(title!!, body!!)
                }
            }
            else
            {
                sendNotification(title!!, body!!)
            }
        }
    }

    private fun receiveNotificationDataOnly(remoteMessage: RemoteMessage?){
        remoteMessage!!.data?.let {
            Log.d(TAG, "onMessageReceived: msg data payload :  ${remoteMessage.data}")
            val title = it["title"]
            val body = it["body"]
            if(it.containsKey("user_id") && it["user_id"] == Cst.USER_ID.toString())
                Log.d(TAG, "onMessageReceived: msg not destined to this user: ${Cst.USER_ID}")
            else
            {
                sendNotification(title!!, body!!)
            }
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val notificationManager = NotificationManagerCompat.from(this)
        val intent = Intent(this, NotificationsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(this, Cst.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_heart)
            .setColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // WHY CUSTOM NOTIF ONLY COMES WHEN THE APP IS RUNNING
        // WHEN THE APP IS RUNNING THE NOTIF IS YELLOW (BACKEND SETTINGS)

        notificationManager.notify(0 /* ID of notification */, notification)
    }



    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "onNewToken: $token")
    }
}
