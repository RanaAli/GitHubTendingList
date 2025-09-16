package com.rana.data.utils

import android.text.format.DateFormat
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DateUtilTest {

    @Before
    fun setup() {
        mockkStatic(DateFormat::class)
    }

    @Test
    fun `formatDate with pattern returns correctly formatted date`() {
        // Given
        val pattern = "yyyy-MM-dd"
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.SEPTEMBER, 16)
        every { DateFormat.format(pattern, calendar) } returns "2025-09-16" as CharSequence

        // When
        val result = formatDate(pattern)

        // Then
        assertEquals("2025-09-16", result)
    }

    @Test
    fun `formatDate handles different patterns`() {
        val patterns = listOf(
            "HH:mm:ss" to "14:30:00",
            "yyyy.MM.dd G 'at' HH:mm:ss z" to "2025.09.16 AD at 14:30:00 GMT",
            "EEE, MMM d, ''yy" to "Tue, Sep 16, '25",
            "h:mm a" to "2:30 PM"
        )

        patterns.forEach { (pattern, expected) ->
            every { DateFormat.format(eq(pattern), any<Calendar>()) } returns expected as CharSequence
            assertEquals(expected, formatDate(pattern))
        }
    }

    @Test
    fun `formatDate handles different locales`() {
        val locales = listOf(
            Locale.US to "September 16, 2025",
            Locale.FRANCE to "16 septembre 2025",
            Locale.GERMANY to "16. September 2025"
        )

        val pattern = "MMMM dd, yyyy"
        locales.forEach { (locale, expected) ->
            Locale.setDefault(locale)
            every { DateFormat.format(eq(pattern), any<Calendar>()) } returns expected as CharSequence
            assertEquals(expected, formatDate(pattern))
        }
    }

    @Test
    fun `formatDate with calendar handles boundary dates`() {
        val calendar = Calendar.getInstance()
        val pattern = "yyyy-MM-dd"

        // Year transition
        calendar.set(2025, Calendar.DECEMBER, 31)
        every { DateFormat.format(pattern, calendar) } returns "2025-12-31" as CharSequence
        assertEquals("2025-12-31", formatDate(calendar, pattern))

        // Month end
        calendar.set(2025, Calendar.SEPTEMBER, 30)
        every { DateFormat.format(pattern, calendar) } returns "2025-09-30" as CharSequence
        assertEquals("2025-09-30", formatDate(calendar, pattern))

        // Leap year
        calendar.set(2024, Calendar.FEBRUARY, 29)
        every { DateFormat.format(pattern, calendar) } returns "2024-02-29" as CharSequence
        assertEquals("2024-02-29", formatDate(calendar, pattern))
    }

    @Test
    fun `convertedDate transforms date string to desired format`() {
        // Given
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd/MM/yyyy"
        val inputDate = "2025-09-16"

        // When
        val result = convertedDate(inputPattern, outputPattern, inputDate)

        // Then
        assertEquals("16/09/2025", result)
    }

    @Test
    fun `convertedDate returns original string when parsing fails`() {
        // Given
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd/MM/yyyy"
        val invalidDate = "invalid-date"

        // When
        val result = convertedDate(inputPattern, outputPattern, invalidDate)

        // Then
        assertEquals(invalidDate, result)
    }

    @Test
    fun `convertedDate handles different locales correctly`() {
        // Given
        val inputPattern = "MMMM dd, yyyy"
        val outputPattern = "dd MMMM yyyy"
        val inputDate = "September 16, 2025"

        // When
        val result = convertedDate(inputPattern, outputPattern, inputDate)

        // Then
        assertEquals("16 September 2025", result)
    }

    @Test
    fun `convertedDate handles invalid patterns`() {
        // Given
        val invalidPattern = "invalid"
        val validDate = "2025-09-16"

        // When & Then
        assertEquals(validDate, convertedDate(invalidPattern, "yyyy-MM-dd", validDate))
        assertEquals(validDate, convertedDate("yyyy-MM-dd", invalidPattern, validDate))
    }

    @Test
    fun `convertedDate handles boundary dates`() {
        val testCases = listOf(
            Triple("yyyy-MM-dd", "dd/MM/yyyy", "2025-12-31" to "31/12/2025"), // Year end
            Triple("yyyy-MM-dd", "dd/MM/yyyy", "2025-01-01" to "01/01/2025"), // Year start
            Triple("yyyy-MM-dd", "dd/MM/yyyy", "2024-02-29" to "29/02/2024"), // Leap year
            Triple("yyyy-MM-dd", "dd/MM/yyyy", "2025-09-30" to "30/09/2025")  // Month end
        )

        testCases.forEach { (inPattern, outPattern, dates) ->
            assertEquals(dates.second, convertedDate(inPattern, outPattern, dates.first))
        }
    }

    @Test
    fun `convertedDate is thread safe`() {
        val threadCount = 10
        val executor = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)
        val results = Collections.synchronizedSet(mutableSetOf<String>())

        repeat(threadCount) {
            executor.submit {
                try {
                    val result = convertedDate("yyyy-MM-dd", "dd/MM/yyyy", "2025-09-16")
                    results.add(result)
                } finally {
                    latch.countDown()
                }
            }
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS))
        executor.shutdown()

        // All results should be identical
        assertEquals(1, results.size)
        assertEquals("16/09/2025", results.first())
    }

    @Test
    fun `isTimeWithInInterval returns true for interval longer than check value`() {
        // Test cases with various intervals
        val testCases = listOf(
            Triple(3600L, 3600000L, 0L), // 1 hour interval vs 1 hour check
            Triple(1800L, 3600000L, 0L), // 1 hour interval vs 30 min check
            Triple(7200L, 3600000L, 0L)  // 1 hour interval vs 2 hour check
        )

        testCases.forEach { (checkSeconds, startMs, endMs) ->
            val result = isTimeWithInInterval(checkSeconds, startMs, endMs)
            val message = "Failed for interval ${startMs - endMs}ms with check ${TimeUnit.SECONDS.toMillis(checkSeconds)}ms"
            assertEquals(message, startMs - endMs > TimeUnit.SECONDS.toMillis(checkSeconds), result)
        }
    }

    @Test
    fun `isTimeWithInInterval handles zero and negative time differences`() {
        // Given
        val now = System.currentTimeMillis()

        // Test zero difference
        assertFalse(isTimeWithInInterval(3600L, now, now))

        // Test negative difference (end time after start time)
        val hourLater = now + TimeUnit.HOURS.toMillis(1)
        assertTrue(isTimeWithInInterval(1800L, now, hourLater)) // 30 min check vs 1 hour difference
    }

    @Test
    fun `isTimeWithInInterval handles negative check values`() {
        // Given
        val now = System.currentTimeMillis()
        val hourAgo = now - TimeUnit.HOURS.toMillis(1)

        // Test with negative check value
        val negativeCheckSeconds = -1800L // -30 minutes
        assertTrue(isTimeWithInInterval(negativeCheckSeconds, now, hourAgo))
    }

    @Test
    fun `isTimeWithInInterval handles edge cases`() {
        // Given
        val now = System.currentTimeMillis()

        // Test with zero check value
        assertFalse(isTimeWithInInterval(0L, now, now))

        // Test with very large time differences
        val farFuture = now + TimeUnit.DAYS.toMillis(365) // 1 year later
        assertTrue(isTimeWithInInterval(TimeUnit.DAYS.toSeconds(1), now, farFuture))

        // Test with very small time differences
        val moment = now + 1000 // 1 second later
        assertFalse(isTimeWithInInterval(TimeUnit.MINUTES.toSeconds(1), now, moment))
    }

    @Test
    fun `formatDate handles invalid or empty pattern gracefully`() {
        // Given
        val pattern = ""
        every { DateFormat.format(eq(pattern), any<Calendar>()) } returns "" as CharSequence

        // When
        val result = formatDate(pattern)

        // Then
        assertEquals("", result)
    }

    @Test
    fun `convertedDate handles empty string input`() {
        // Given
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd/MM/yyyy"
        val inputDate = ""

        // When
        val result = convertedDate(inputPattern, outputPattern, inputDate)

        // Then - should return the original empty string
        assertEquals("", result)
    }

    @Test
    fun `convertedDate handles timezone offsets without throwing`() {
        // Given
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ"
        val outputPattern = "yyyy-MM-dd HH:mm Z"
        val inputDate = "2025-09-16T14:30:00+0200"

        // When - should not throw and should return a non-empty formatted string
        val result = convertedDate(inputPattern, outputPattern, inputDate)

        // Then
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `isTimeWithInInterval handles negative timestamps (pre epoch)`() {
        // Given pre-1970 timestamps (negative milliseconds)
        val startTime = -1_000_000_000L
        val endTime = -2_000_000_000L
        val checkSeconds = 1000L // 1000 seconds

        // When
        val result = isTimeWithInInterval(checkSeconds, startTime, endTime)

        // Then - absolute difference should be compared correctly
        assertEquals(Math.abs(startTime - endTime) > TimeUnit.SECONDS.toMillis(checkSeconds), result)
    }

    @Test
    fun `isTimeWithInInterval handles very large check values without throwing`() {
        // Given
        val now = System.currentTimeMillis()
        val later = now + TimeUnit.SECONDS.toMillis(10)
        val hugeCheck = Long.MAX_VALUE / 2 // very large seconds value

        // When - should not throw
        val result = isTimeWithInInterval(hugeCheck, now, later)

        // Then - difference (10s) is much smaller than huge check so result should be false
        assertFalse(result)
    }

    @Test
    fun `convertedDate large concurrent stress test`() {
        // Stress test: convert a large batch of dates concurrently
        val total = 1000
        val threadCount = 8
        val executor = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(total)
        val results = Collections.synchronizedList(mutableListOf<String>())

        repeat(total) {
            executor.submit {
                try {
                    val r = convertedDate("yyyy-MM-dd", "dd/MM/yyyy", "2025-09-16")
                    results.add(r)
                } finally {
                    latch.countDown()
                }
            }
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS))
        executor.shutdown()

        // All results should match expected
        assertEquals(total, results.size)
        assertTrue(results.all { it == "16/09/2025" })
    }
}
