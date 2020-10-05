package com.impression.savealife.api

import com.google.gson.GsonBuilder
import com.impression.savealife.services.AppuserService
import com.impression.savealife.services.AuthenticationService
import com.impression.savealife.services.NotificationService
import com.impression.savealife.services.PostService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ApiClient {

    private const val baseUrl = PrivateAPIs.SERVER_BASE_URL
    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit{
        if(retrofit == null){
            val gson = GsonBuilder().setLenient().create()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }

    fun getAuthenticationService(): AuthenticationService = getRetrofitInstance().create(AuthenticationService::class.java)

    fun getAppuserServices(): AppuserService = getRetrofitInstance().create(AppuserService::class.java)

    fun getPostServices(): PostService = getRetrofitInstance().create(PostService::class.java)

    fun getNotificationServices(): NotificationService = getRetrofitInstance().create(NotificationService::class.java)





}
