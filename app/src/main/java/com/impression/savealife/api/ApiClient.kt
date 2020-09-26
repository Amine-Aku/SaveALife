package com.impression.savealife.api

import com.impression.savealife.services.AppuserServices
import com.impression.savealife.services.NotificationServices
import com.impression.savealife.services.PostServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://save-a-life-web-server.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAppuserServices(): AppuserServices = getRetrofit().create(AppuserServices::class.java)

    fun getPostServices(): PostServices = getRetrofit().create(PostServices::class.java)

    fun getNotificationServices(): NotificationServices = getRetrofit().create(NotificationServices::class.java)



}
