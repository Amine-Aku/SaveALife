package com.impression.savealife.services

import com.impression.savealife.models.Donation
import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DonationService {
    @GET("user/donation/get")
    fun getDonations( @Header("Authorization") token: String?): Call<List<Donation>>

    @POST("user/donation/add")
    fun addDonation(@Body post: Post, @Header("Authorization") token: String?): Call<String>
}
