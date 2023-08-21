package com.bignerdranch.android.blognerdranch.usecases

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.rest.NetworkConnection
import com.bignerdranch.android.blognerdranch.rest.PostRepository
import com.bignerdranch.android.blognerdranch.utils.State
import com.google.common.truth.Truth
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PostMetadataUseCaseTest {

    private lateinit var testObject: PostMetadataUseCase
    private val mockRepo = mockk<PostRepository>(relaxed = true)
    private val mockConnection = mockk<NetworkConnection>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        testObject = PostMetadataUseCase(mockRepo, mockConnection)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke posts id details get a success post`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePostById(123) } returns Response.success(mockk())

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.invoke(123).collect {
                states.add(it)
            }
        }

        Truth.assertThat(states).hasSize(1)
        Truth.assertThat(states[0]).isInstanceOf(State.SUCCESS::class.java)
        Truth.assertThat((states[0] as State.SUCCESS).data).isNotNull()

        job.cancel()
    }

    @Test
    fun `invoke posts id details get a network connection error`() {
        every { mockConnection.isNetworkAvailable() } returns false

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.invoke(123).collect {
                states.add(it)
            }
        }

        Truth.assertThat(states).hasSize(1)
        Truth.assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        Truth.assertThat((states[0] as State.ERROR).error.message).isEqualTo("No internet connection available")

        job.cancel()
    }

    @Test
    fun `invoke posts id details get a null body error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePostById(123) } returns Response.success(null)

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.invoke(123).collect {
                states.add(it)
            }
        }

        Truth.assertThat(states).hasSize(1)
        Truth.assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        Truth.assertThat((states[0] as State.ERROR).error.message).isEqualTo("Null body in response")

        job.cancel()
    }

    @Test
    fun `invoke posts id details get a response error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePostById(123) } returns mockk {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "error"
            }
        }

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.invoke(123).collect {
                states.add(it)
            }
        }

        Truth.assertThat(states).hasSize(1)
        Truth.assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        Truth.assertThat((states[0] as State.ERROR).error.message).isEqualTo("error")

        job.cancel()
    }

    @Test
    fun `invoke posts id details throws any exception error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePostById(123) } throws Exception("error")

        val states = mutableListOf<State<Post>>()

        val job = testScope.launch {
            testObject.invoke(123).collect {
                states.add(it)
            }
        }

        Truth.assertThat(states).hasSize(1)
        Truth.assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        Truth.assertThat((states[0] as State.ERROR).error.message).isEqualTo("error")

        job.cancel()
    }
}