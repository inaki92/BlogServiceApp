package com.bignerdranch.ktor.mockserver.models

import kotlinx.serialization.Serializable

@Serializable
data class PostMetadata(
    val id: String,
    val title: String,
    val publishDate: String,
    val postId: String,
    val summary: String,
    val author: Author
)

@Serializable
data class Author(
    val name: String,
    val image: String,
    val title: String
)
