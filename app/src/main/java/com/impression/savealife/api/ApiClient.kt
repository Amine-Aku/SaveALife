package com.impression.savealife.api

import com.impression.savealife.services.AppuserService
import com.impression.savealife.services.AuthenticationService
import com.impression.savealife.services.NotificationService
import com.impression.savealife.services.PostService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val baseUrl = "https://save-a-life-web-server.herokuapp.com/"
    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit{
        if(retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit!!
    }

    fun getAuthenticationService(): AuthenticationService = getRetrofitInstance().create(AuthenticationService::class.java)

    fun getAppuserServices(): AppuserService = getRetrofitInstance().create(AppuserService::class.java)

    fun getPostServices(): PostService = getRetrofitInstance().create(PostService::class.java)

    fun getNotificationServices(): NotificationService = getRetrofitInstance().create(NotificationService::class.java)





}
