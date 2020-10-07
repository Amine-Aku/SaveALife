package com.impression.savealife.services

import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PostService {

    @GET("home/posts")
    fun getPosts(): Call<List<Post>>

    @GET("user/post/get")
    fun getUserPosts(): Call<List<Post>>

    @POST("user/post/add")
    fun addPost(@Body post: Post, @Header("Authorization") token: String?): Call<Post>
}
