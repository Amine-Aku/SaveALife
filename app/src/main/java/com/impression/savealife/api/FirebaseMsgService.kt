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
import com.impression.savealife.activities.WelcomeActivity
import com.impression.savealife.models.Cst
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FirebaseMsgService() : FirebaseMessagingService() {

    private val TAG = "FirebaseMsgService"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
//        receiveNotification(remoteMessage)
        receiveNotificationDataOnly(remoteMessage)
    }


    private fun receiveNotificationDataOnly(remoteMessage: RemoteMessage?){
        remoteMessage!!.data?.let {
            Log.d(TAG, "onMessageReceived: msg data payload :  ${remoteMessage.data}")
            var title = it["title"]
            var body = it["body"]


            if(it.containsKey("token")
                && it["token"] != null){
                if(title == "welcome back"){
                    title = getString(R.string.welcomeback_notification_title)
                    body = getString(R.string.welcomeback_notification_body)
                    Cst.currentUser!!.hasDonated = false
                    Cst.saveData(this)
                    sendNotification(title!!, body!!, Cst.CHANNEL_3_ID_uCanDonate)
                }
                sendNotification(title!!, body!!, Cst.CHANNEL_2_ID_Donation)
            }
            else if( Cst.currentUser != null
                && it.containsKey("user_id")
                && it["user_id"] != null
                && it["user_id"] == Cst.currentUser!!.id.toString())
                Log.d(TAG, "onMessageReceived: msg not destined to this user: ${Cst.currentUser!!.id}")
            else
            {
                sendNotification(title!!, body!!, Cst.CHANNEL_1_ID_NewPost)
            }
        }
    }

    private fun sendNotification(title: String, messageBody: String, channel: String) {
        val notificationManager = NotificationManagerCompat.from(this)
        val intent = if(Cst.loadData(this)){
            Intent(this, HomeActivity::class.java)
        } else {
            Intent(this, NotificationsActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(this, channel)
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
