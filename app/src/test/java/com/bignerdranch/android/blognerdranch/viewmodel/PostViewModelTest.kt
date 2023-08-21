package com.bignerdranch.android.blognerdranch.viewmodel

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.usecases.UseCaseWrapper
import com.bignerdranch.android.blognerdranch.utils.State
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    private lateinit var testObject: PostViewModel
    private val mockCasesWrapper = mockk<UseCaseWrapper>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        every { mockCasesWrapper.postMetadataUseCase} returns mockk {
            every { this@mockk(123) } returns flowOf(
                State.SUCCESS(
                    mockk {
                        every { id } returns 123
                    }
                )
            )
        }

        testObject = PostViewModel(mockCasesWrapper)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `get post details when selected post and not cache post found`() {
        every { mockCasesWrapper.postMetadataUseCase} returns mockk {
            every { this@mockk(321) } returns flowOf(
                State.SUCCESS(
                    mockk {
                        every { id } returns 321
                    }
                )
            )
        }

        testObject.selectedPost = mockk {
            every { postId } returns 321
        }

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.postMetadata.collect {
                states.add(it)
            }
        }

        testObject.getPostDetails()

        print(states)
        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.SUCCESS::class.java)
        assertThat((states[1] as State.SUCCESS).data).isNotNull()
        assertThat((states[1] as State.SUCCESS).data.id).isEqualTo(321)

        job.cancel()
    }

    @Test
    fun `get post details when selected post and cache post found`() {
        testObject.selectedPost = mockk {
            every { postId } returns 123
        }

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.postMetadata.collect {
                states.add(it)
            }
        }

        testObject.getPostDetails()

        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.SUCCESS::class.java)
        assertThat((states[1] as State.SUCCESS).data).isNotNull()
        assertThat((states[1] as State.SUCCESS).data.id).isEqualTo(123)

        testObject.getPostDetails()

        job.cancel()
    }

    @Test
    fun `get post details error when selected post has no id`() {
        testObject.selectedPost = mockk {
            every { postId } returns null
        }

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.postMetadata.collect {
                states.add(it)
            }
        }

        testObject.getPostDetails()

        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[1] as State.ERROR).error.message).isEqualTo("No ID found for the selected post")

        job.cancel()
    }

    @Test
    fun `get post details error when no post has been selected`() {
        testObject.selectedPost = null

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.postMetadata.collect {
                states.add(it)
            }
        }

        testObject.getPostDetails()

        assertThat(states).hasSize(2)
        assertThat(states[0]).isInstanceOf(State.LOADING::class.java)
        assertThat(states[1]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[1] as State.ERROR).error.message).isEqualTo("No post selected to retrieve details")

        job.cancel()
    }
}