package com.bignerdranch.android.blognerdranch.rest

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PostRepositoryImplTest {

    private lateinit var testObject: PostRepository
    private val mockApi = mockk<BlogService>(relaxed = true)

    @Before
    fun setUp() {
        testObject = PostRepositoryImpl(mockApi)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `retrieve posts metadata network call`() = runTest {
        coEvery { mockApi.getPostMetadata() } returns Response.success(listOf(mockk(), mockk()))

        val result = testObject.retrievePosts()

        assertThat(result.body()).hasSize(2)
    }

    @Test
    fun `retrieve post by id network call`() = runTest {
        coEvery { mockApi.getPost(123) } returns Response.success(mockk())

        val result = testObject.retrievePostById(123)

        assertThat(result.body()).isNotNull()
    }
}