package com.bignerdranch.android.blognerdranch.rest

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.utils.POST_ID_PATH
import com.bignerdranch.android.blognerdranch.utils.POST_PATH
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BlogService {

    @GET(POST_PATH)
    suspend fun getPostMetadata(): Response<List<PostMetadata>>

    @GET(POST_ID_PATH)
    suspend fun getPost(@Path("id") id: Int): Response<Post>
}