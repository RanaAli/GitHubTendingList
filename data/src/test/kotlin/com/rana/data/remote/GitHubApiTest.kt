package com.rana.data.remote

// import okhttp3.mockwebserver.MockWebServer // You might need this for testing Retrofit interfaces
// import retrofit2.Retrofit
// import retrofit2.converter.gson.GsonConverterFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class GitHubApiTest {

    // private lateinit var mockWebServer: MockWebServer
    // private lateinit var api: GitHubApi

    @Before
    fun setUp() {
        // mockWebServer = MockWebServer()
        // mockWebServer.start()
        // api = Retrofit.Builder()
        //     .baseUrl(mockWebServer.url("/"))
        //     .addConverterFactory(GsonConverterFactory.create())
        //     .build()
        //     .create(GitHubApi::class.java)
    }

    @After
    fun tearDown() {
        // mockWebServer.shutdown()
    }

    @Test
    fun `test getTrendingRepositories endpoint`() {
        // TODO: Add test logic using MockWebServer to mock API responses
        // Given
        // val mockResponse = MockResponse()
        //    .setResponseCode(200)
        //    .setBody("{ \"items\": [] }") // Your expected JSON response
        // mockWebServer.enqueue(mockResponse)

        // When
        // val response = runBlocking { api.getTrendingRepositories("kotlin", "stars", "desc") }

        // Then
        // assertNotNull(response)
        // assertTrue(response.isSuccessful)
        // assertNotNull(response.body()?.items)
        // ... more assertions
    }
}