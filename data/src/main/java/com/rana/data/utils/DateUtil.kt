/*
 * @author   Anoop Maddasseri <anoopmaddasseri@gmail.com>
 * @version  1
 * @since    19th Feb 2020
 *
 * P.S. Increment version when editing
 */

@file:JvmName("DateUtil")

package com.rana.data.utils


import android.text.format.DateFormat
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun formatDate(pattern: String): String {
    val calendar = Calendar.getInstance(Locale.getDefault())
    return DateFormat.format(pattern, calendar).toString()
}

fun formatDate(calendar: Calendar, pattern: String): String =
    DateFormat.format(pattern, calendar).toString()

fun convertedDate(inputPattern: String, outputPattern: String, stringDate: String): String {
    return try {
        val originalFormat = SimpleDateFormat(inputPattern, Locale.US).apply {
            isLenient = false // Strict parsing
        }
        val targetFormat = SimpleDateFormat(outputPattern, Locale.US)
        synchronized(originalFormat) {
            originalFormat.parse(stringDate)?.let { date ->
                synchronized(targetFormat) {
                    targetFormat.format(date)
                }
            }
        } ?: stringDate
    } catch (e: Exception) {
        // Handle both IllegalArgumentException (invalid pattern) and ParseException (invalid date)
        stringDate
    }
}

fun isTimeWithInInterval(valueToCheckInSeconds: Long, startTime: Long, endTime: Long): Boolean {
    val valueToCheckInMillis = TimeUnit.SECONDS.toMillis(valueToCheckInSeconds)
    val timeDifference = Math.abs(startTime - endTime)
    return timeDifference > valueToCheckInMillis
}