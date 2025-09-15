package com.rana.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class HttpClientTest {

    @Test
    fun `setupOkhttpClient configures client correctly`() {
        // Given
        val mockHttpLoggingInterceptor = Mockito.mock(HttpLoggingInterceptor::class.java)

        // When
        val okHttpClient = HttpClient.setupOkhttpClient(mockHttpLoggingInterceptor)

        // Then
        // Check if the interceptor is added
        assertTrue("HttpLoggingInterceptor should be added", okHttpClient.interceptors().contains(mockHttpLoggingInterceptor))

        // Check timeouts (OkHttp stores timeouts in milliseconds)
        assertEquals("Connect timeout should be 5 seconds", TimeUnit.SECONDS.toMillis(5), okHttpClient.connectTimeoutMillis().toLong())
        assertEquals("Read timeout should be 5 seconds", TimeUnit.SECONDS.toMillis(5), okHttpClient.readTimeoutMillis().toLong())
    }
}