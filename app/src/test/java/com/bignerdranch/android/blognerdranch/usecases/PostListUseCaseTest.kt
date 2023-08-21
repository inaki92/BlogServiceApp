package com.bignerdranch.android.blognerdranch.usecases

import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.rest.NetworkConnection
import com.bignerdranch.android.blognerdranch.rest.PostRepository
import com.bignerdranch.android.blognerdranch.utils.State
import com.google.common.truth.Truth.assertThat
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
class PostListUseCaseTest {

    private lateinit var testObject: PostListUseCase
    private val mockRepo = mockk<PostRepository>(relaxed = true)
    private val mockConnection = mockk<NetworkConnection>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        testObject = PostListUseCase(mockRepo, mockConnection)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke posts list  get a success list`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePosts() } returns Response.success(listOf(mockk(), mockk()))

        val states = mutableListOf<State<List<PostMetadata>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.SUCCESS::class.java)
        assertThat((states[0] as State.SUCCESS).data).hasSize(2)

        job.cancel()
    }

    @Test
    fun `invoke posts list  get a network connection error`() {
        every { mockConnection.isNetworkAvailable() } returns false

        val states = mutableListOf<State<List<PostMetadata>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("No internet connection available")

        job.cancel()
    }

    @Test
    fun `invoke posts list  get a null body error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePosts() } returns Response.success(null)

        val states = mutableListOf<State<List<PostMetadata>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("Null body in response")

        job.cancel()
    }

    @Test
    fun `invoke posts list  get a response error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePosts() } returns mockk {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "error"
            }
        }

        val states = mutableListOf<State<List<PostMetadata>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("error")

        job.cancel()
    }

    @Test
    fun `invoke posts list throws any exception error`() {
        every { mockConnection.isNetworkAvailable() } returns true
        coEvery { mockRepo.retrievePosts() } throws Exception("error")

        val states = mutableListOf<State<List<PostMetadata>>>()

        val job = testScope.launch {
            testObject.invoke().collect {
                states.add(it)
            }
        }

        assertThat(states).hasSize(1)
        assertThat(states[0]).isInstanceOf(State.ERROR::class.java)
        assertThat((states[0] as State.ERROR).error.message).isEqualTo("error")

        job.cancel()
    }
}