package com.impression.savealife.api

import com.impression.savealife.services.PostServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://save-a-life-web-server.herokuapp.com/home/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPostServices(): PostServices{
        return getRetrofit().create(PostServices::class.java)
    }

}
