package com.impression.savealife.services

import com.impression.savealife.models.Notification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationServices {

    @GET("notification/get")
    fun getNotifications(): Call<List<Notification>>

    @POST("notification/add")
    fun addNotification(@Body notification: Notification): Call<Notification>

    @POST("notification/topic")
    fun sendPushNotificationToTopic(@Body notification: Notification): Call<Notification>

    @POST("notification/token")
    fun sendPushNotificationToToken(@Body notification: Notification): Call<Notification>
}
