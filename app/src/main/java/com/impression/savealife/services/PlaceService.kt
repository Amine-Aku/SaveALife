package com.impression.savealife.services

import com.impression.savealife.models.Place
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PlaceService {

    @GET("user/places/cityName/get")
    fun getCityNames(@Header("Authorization") token: String?): Call<List<String>>

    @GET("user/place/get/{city}")
    fun getDonationCenters(@Path("city") city: String, @Header("Authorization") token: String?): Call<List<Place>>

}
