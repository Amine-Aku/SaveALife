package com.impression.savealife.services

import com.impression.savealife.models.Appuser
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppuserService {
    @GET("user/{id}")
    fun getUser(@Path("id") id: Long): Call<List<Appuser>>

    @POST("register")
    fun registerUser(@Body newUser: Appuser): Call<String>
}
