package com.impression.savealife.api

import com.google.gson.GsonBuilder
import com.impression.savealife.services.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create


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

    fun getDonationServices(): DonationService = getRetrofitInstance().create(DonationService::class.java)

    fun getPlaceServices(): PlaceService = getRetrofitInstance().create(PlaceService::class.java)





}
