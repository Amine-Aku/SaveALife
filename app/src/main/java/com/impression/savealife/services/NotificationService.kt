package com.impression.savealife.services

import com.impression.savealife.models.Cst
import com.impression.savealife.models.Notification
import retrofit2.Call
import retrofit2.http.*

interface NotificationService {

    @GET("user/notification/get")
    fun getNotifications(@Header("Authorization") token: String?): Call<List<Notification>>

    @POST("user/notification/add")
    fun addNotification(@Body notification: Notification, @Header("Authorization") token: String?): Call<Notification>

    @POST("user/notification/add/token")
    fun addNotificationToToken(@Body notification: Notification, @Header("Authorization") token: String?): Call<Notification>

    @POST("notification/topic")
    fun sendPushNotificationToTopic(@Body notification: Notification): Call<Notification>

    @POST("notification/token")
    fun sendPushNotificationToToken(@Body notification: Notification): Call<Notification>

    @POST("notification/data")
    fun sendPushNotificationWithData(@Body notification: Notification): Call<Notification>
}
