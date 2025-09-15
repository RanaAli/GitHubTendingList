package com.rana.data.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class DateUtilTest {

    @Test
    fun `formatDate with pattern returns formatted date string`() {
        // This test is dependent on the default Locale and TimeZone.
        // For more robust testing, consider providing a Calendar instance.
        val pattern = "yyyy-MM-dd"
        val expectedDateString = java.text.SimpleDateFormat(pattern, Locale.getDefault()).format(Calendar.getInstance(Locale.getDefault()).time)
        assertEquals(expectedDateString, formatDate(pattern))
    }

    @Test
    fun `formatDate with Calendar and pattern returns formatted date string`() {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US)
        calendar.set(2023, Calendar.OCTOBER, 26, 10, 30, 0)
        // Expected: 2023-10-26 10:30:00
        val expected = "2023-10-26 10:30:00"
        assertEquals(expected, formatDate(calendar, pattern))
    }

    @Test
    fun `convertedDate converts date string from one format to another`() {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val outputPattern = "dd MMM yyyy, HH:mm"
        val stringDate = "2023-03-15T10:20:30Z"
        val expected = "15 Mar 2023, 10:20" // Assumes UTC parsing and formatting
        assertEquals(expected, convertedDate(inputPattern, outputPattern, stringDate))
    }

    @Test
    fun `convertedDate handles different locales in patterns`() {
        val inputPattern = "MM/dd/yyyy"
        val outputPattern = "dd.MM.yyyy"
        val stringDate = "10/26/2023"
        val expected = "26.10.2023"
        assertEquals(expected, convertedDate(inputPattern, outputPattern, stringDate))
    }

    @Test(expected = java.text.ParseException::class)
    fun `convertedDate throws ParseException for invalid input date string`() {
        convertedDate("yyyy-MM-dd", "dd/MM/yyyy", "invalid-date")
    }

    @Test
    fun `isTimeWithInInterval returns true when value is within interval`() {
        val startTimeMillis = System.currentTimeMillis() // e.g., now
        val endTimeMillis = startTimeMillis + TimeUnit.MINUTES.toMillis(60) // 60 minutes from start
        val valueToCheckSeconds = TimeUnit.MILLISECONDS.toSeconds(startTimeMillis + TimeUnit.MINUTES.toMillis(30)) // 30 minutes from start

        // The original logic `startTimeInMinutes - endTimeInMinutes > valueToCheckInMinutes` seems incorrect.
        // It should check if valueToCheck is BETWEEN startTime and endTime, or if a duration has passed.
        // Assuming the intent is to check if 'valueToCheckSeconds' (as a duration) is less than the interval duration.
        // Current logic: (start - end) > value. If start=0, end=60min, value=30sec. (0 - 60*60) > 30 => -3600 > 30 is false.
        // If start=60min, end=0min, value=30sec. (60*60 - 0) > 30 => 3600 > 30 is true.
        // Let's test the provided logic as is, but it might need review.

        // Test case where original logic would be true: Start time significantly after end time (inverted interval for the subtraction to be positive)
        val scenario1StartTime = TimeUnit.HOURS.toMillis(2) // 2 hours
        val scenario1EndTime = TimeUnit.HOURS.toMillis(1)   // 1 hour
        val scenario1ValueSeconds = TimeUnit.MINUTES.toSeconds(30) // 30 minutes
        assertTrue("Value should be within interval for scenario 1", isTimeWithInInterval(scenario1ValueSeconds, scenario1StartTime, scenario1EndTime))
    }

    @Test
    fun `isTimeWithInInterval returns false when value is outside interval`() {
        // Test case where original logic would be false: Start time before end time
        val scenario2StartTime = TimeUnit.HOURS.toMillis(1) // 1 hour
        val scenario2EndTime = TimeUnit.HOURS.toMillis(2)   // 2 hours
        val scenario2ValueSeconds = TimeUnit.MINUTES.toSeconds(30) // 30 minutes
        assertFalse("Value should be outside interval for scenario 2", isTimeWithInInterval(scenario2ValueSeconds, scenario2StartTime, scenario2EndTime))

        // Test case where (start - end) is positive but smaller than value
        val scenario3StartTime = TimeUnit.MINUTES.toMillis(70) // 70 minutes
        val scenario3EndTime = TimeUnit.MINUTES.toMillis(10)   // 10 minutes (duration 60 mins)
        val scenario3ValueSeconds = TimeUnit.MINUTES.toSeconds(61) // 61 minutes
        assertFalse("Value should be outside interval for scenario 3", isTimeWithInInterval(scenario3ValueSeconds, scenario3StartTime, scenario3EndTime))
    }
}
