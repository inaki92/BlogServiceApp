package com.bignerdranch.android.blognerdranch.usecases

import javax.inject.Inject

data class UseCaseWrapper @Inject constructor(
    val postListUseCase: PostListUseCase,
    val postMetadataUseCase: PostMetadataUseCase
)
