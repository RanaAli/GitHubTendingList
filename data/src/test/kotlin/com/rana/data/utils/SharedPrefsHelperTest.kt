package com.rana.data.utils

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SharedPrefsHelperTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var sut: SharedPrefsHelper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // TODO: Mock Context and SharedPreferences behavior
        // Mockito.`when`(mockContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockSharedPreferences)
        // Mockito.`when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        sut = SharedPrefsHelper(mockContext)
    }

    @Test
    fun `test case description`() {
        // TODO: Add test logic
        // Given

        // When

        // Then
    }
}