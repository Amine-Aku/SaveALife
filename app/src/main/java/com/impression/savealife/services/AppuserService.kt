package com.impression.savealife.services

import com.impression.savealife.models.Appuser
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.http.*

interface AppuserService {
    @GET("user/{id}")
    fun getUser(@Path("id") id: Long): Call<List<Appuser>>

    @POST("register")
    fun registerUser(@Body newUser: Appuser): Call<String>

    @PUT("user/update")
    fun updateUser(@Body data: Map<String, String>, @Header("Authorization") token: String?): Call<String>
}
