package com.bignerdranch.android.blognerdranch.viewmodel

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.usecases.UseCaseWrapper
import com.bignerdranch.android.blognerdranch.utils.BaseViewModel
import com.bignerdranch.android.blognerdranch.utils.NoPostIdFoundException
import com.bignerdranch.android.blognerdranch.utils.NoPostSelectedException
import com.bignerdranch.android.blognerdranch.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val useCaseWrapper: UseCaseWrapper
) : BaseViewModel() {

    var selectedPost: PostMetadata? = null

    private val postVisited = mutableListOf<Post>()

    private val _posts: MutableStateFlow<State<List<PostMetadata>>> = MutableStateFlow(State.LOADING)
    val posts: StateFlow<State<List<PostMetadata>>> get() = _posts

    private val _postMetadata: MutableStateFlow<State<Post>> = MutableStateFlow(State.LOADING)
    val postMetadata: StateFlow<State<Post>> get() = _postMetadata

    init {
        getPosts()
    }

    private fun getPosts() {
        safeViewModelScope.launch {
            useCaseWrapper.postListUseCase().collect {
                _posts.value = it
            }
        }
    }

    fun getPostDetails() {
        _postMetadata.value = State.LOADING

        selectedPost?.let { post ->
            post.postId?.let { id ->
                val offLinePost = postVisited.firstOrNull { it.id == id }
                if (offLinePost == null) {
                    safeViewModelScope.launch {
                        useCaseWrapper.postMetadataUseCase(id).collect { state ->
                            if (state is State.SUCCESS) {
                                postVisited.add(state.data)
                            }
                            _postMetadata.value = state
                        }
                    }
                } else {
                    _postMetadata.value = State.SUCCESS(offLinePost)
                }
            } ?: let {
                _postMetadata.value = State.ERROR(NoPostIdFoundException())
            }
        } ?: let {
            _postMetadata.value = State.ERROR(NoPostSelectedException())
        }
    }

    fun retryGetPosts() {
        getPosts()
    }
}