package com.impression.savealife.services

import com.impression.savealife.models.Appuser
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface AppuserService {
    @GET("user/{id}")
    fun getUser(@Path("id") id: Long): Call<List<Appuser>>

    @POST("register")
    fun registerUser(@Body newUser: Appuser): Call<String>

    @PUT("user/update")
    fun updateUser(@Body data: Map<String, String>, @Header("Authorization") token: String?): Call<String>

    @PUT("user/token")
    fun updateToken(@Body newToken: String?, @Header("Authorization") token: String?): Call<String>

    @GET("user/lastDonation")
    fun getLastDonation(@Header("Authorization") token: String?): Call<Date>
}
