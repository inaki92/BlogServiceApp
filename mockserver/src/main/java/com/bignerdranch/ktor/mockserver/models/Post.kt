package com.bignerdranch.ktor.mockserver.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val metadata: PostMetadata,
    val body: String
)
