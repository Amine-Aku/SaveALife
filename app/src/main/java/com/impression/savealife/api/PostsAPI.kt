package com.impression.savealife.api

import com.impression.savealife.models.Post
import retrofit2.Call
import retrofit2.http.GET

interface PostsAPI {
    @GET("/home/posts")
    fun getPosts(): Call<List<Post>>
}
