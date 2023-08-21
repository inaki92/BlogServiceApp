package com.bignerdranch.android.blognerdranch.rest

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import retrofit2.Response
import javax.inject.Inject

interface PostRepository {
    suspend fun retrievePosts(): Response<List<PostMetadata>>
    suspend fun retrievePostById(id: Int): Response<Post>
}

class PostRepositoryImpl @Inject constructor(
    private val blogService: BlogService
) : PostRepository {
    override suspend fun retrievePosts(): Response<List<PostMetadata>> =
        blogService.getPostMetadata()

    override suspend fun retrievePostById(id: Int): Response<Post> =
        blogService.getPost(id)
}