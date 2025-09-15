package com.rana.data

import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LoggingInterceptorTest {

    @Test
    fun `create returns HttpLoggingInterceptor with BODY level`() {
        // When
        val interceptor = LoggingInterceptor.create()

        // Then
        assertNotNull("Interceptor should not be null", interceptor)
        assertEquals("Interceptor log level should be BODY", HttpLoggingInterceptor.Level.BODY, interceptor.level)
    }
}