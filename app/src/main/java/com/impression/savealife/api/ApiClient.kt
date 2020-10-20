package com.impression.savealife.api

import com.google.gson.GsonBuilder
import com.impression.savealife.services.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


object ApiClient {

    private const val baseUrl = PrivateAPIs.SERVER_BASE_URL
    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit{
        if(retrofit == null){
            val gson = GsonBuilder().setLenient().create()

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }
        return retrofit!!
    }

    fun getAuthenticationService(): AuthenticationService = getRetrofitInstance().create(AuthenticationService::class.java)

    fun getAppuserServices(): AppuserService = getRetrofitInstance().create(AppuserService::class.java)

    fun getPostServices(): PostService = getRetrofitInstance().create(PostService::class.java)

    fun getNotificationServices(): NotificationService = getRetrofitInstance().create(NotificationService::class.java)

    fun getDonationServices(): DonationService = getRetrofitInstance().create(DonationService::class.java)

    fun getPlaceServices(): PlaceService = getRetrofitInstance().create(PlaceService::class.java)





}
