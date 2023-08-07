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
    val originalFormat = SimpleDateFormat(inputPattern, Locale.US)
    val targetFormat = SimpleDateFormat(outputPattern, Locale.US)
    val requiredFormat = originalFormat.parse(stringDate)
    return targetFormat.format(requiredFormat)
}

fun isTimeWithInInterval(valueToCheckInSeconds: Long, startTime: Long, endTime: Long): Boolean {
    val startTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(startTime)
    val endTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(endTime)
    val valueToCheckInMinutes = TimeUnit.SECONDS.toMinutes(valueToCheckInSeconds)

    Log.d("DateUtil", "${startTimeInMinutes - endTimeInMinutes}")
    Log.d("DateUtil", "${startTimeInMinutes - endTimeInMinutes > valueToCheckInMinutes}")

    return startTimeInMinutes - endTimeInMinutes > valueToCheckInMinutes
}