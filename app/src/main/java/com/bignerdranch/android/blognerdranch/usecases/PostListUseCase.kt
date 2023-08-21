package com.bignerdranch.android.blognerdranch.usecases

import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.rest.NetworkConnection
import com.bignerdranch.android.blognerdranch.rest.PostRepository
import com.bignerdranch.android.blognerdranch.utils.State
import com.bignerdranch.android.blognerdranch.utils.makeNetworkCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostListUseCase @Inject constructor(
    private val repository: PostRepository,
    private val networkConnection: NetworkConnection
) {

    operator fun invoke(): Flow<State<List<PostMetadata>>> = flow {
        makeNetworkCall(
            isNetworkAvailable = { networkConnection.isNetworkAvailable() },
            request = { repository.retrievePosts() },
            success = { emit(it) },
            error = { emit(it) }
        )
    }
}