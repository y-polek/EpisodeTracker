package dev.polek.episodetracker.common.utils

import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.util.date.plus

fun currentTimeMillis(): Long = GMTDate().timestamp

fun date(year: Int, month: Month, day: Int, hours: Int = 0): GMTDate {
    return GMTDate(
        year = year,
        month = month,
        dayOfMonth = day,
        hours = hours,
        minutes = 0,
        seconds = 0)
}

fun millisUntilMidnight(): Long {
    val now = GMTDate()
    val tomorrow = now + 24 * 60 * 60 * 1000
    val midnight = date(year = tomorrow.year, month = tomorrow.month, day = tomorrow.dayOfMonth)

    return midnight.timestamp - now.timestamp
}
