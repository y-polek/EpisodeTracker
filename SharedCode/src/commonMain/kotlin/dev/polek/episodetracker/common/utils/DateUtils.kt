package dev.polek.episodetracker.common.utils

import io.ktor.util.InternalAPI
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.util.date.Month.*

private val dateRegex = "^([\\d]{4})-([\\d]{1,2})-([\\d]{1,2})$".toRegex()

val now: GMTDate
    get() = GMTDate()

@OptIn(InternalAPI::class)
fun parseDate(dateStr: String): GMTDate? {

    val groups = dateRegex.find(dateStr)?.groups
    if (groups != null && groups.size == 4) {
        val year = groups[1]?.value?.toIntOrNull() ?: return null
        val month = groups[2]?.value?.toIntOrNull() ?: return null
        val day = groups[3]?.value?.toIntOrNull() ?: return null
        return GMTDate(
            year = year,
            month = Month.from(month - 1),
            dayOfMonth = day,
            hours = 0,
            minutes = 0,
            seconds = 0)
    }
    return null
}

fun formatDate(date: GMTDate): String {
    val year = date.year
    val month = date.month.value
    val day = date.dayOfMonth

    return "$month $day, $year"
}

fun formatTimestamp(date: GMTDate = GMTDate()): String {
    val year = date.year
    val month = (date.month.ordinal + 1).fillZero()
    val day = date.dayOfMonth.fillZero()
    val hours = date.hours.fillZero()
    val minutes = date.minutes.fillZero()
    val seconds = date.seconds.fillZero()
    val millis = (date.timestamp % 1000).fillTwoZeroes()

    return "$year-$month-$day $hours:$minutes:$seconds.$millis"
}

fun formatTimestamp(timestampMillis: Long): String = formatTimestamp(GMTDate(timestampMillis))

val Month.fullName: String
    get() = when (this) {
        JANUARY -> "January"
        FEBRUARY -> "February"
        MARCH -> "March"
        APRIL -> "April"
        MAY -> "May"
        JUNE -> "June"
        JULY -> "July"
        AUGUST -> "August"
        SEPTEMBER -> "September"
        OCTOBER -> "October"
        NOVEMBER -> "November"
        DECEMBER -> "December"
    }

private fun Number.fillZero(): String {
    return when (this) {
        in 0..9 -> "0$this"
        else -> this.toString()
    }
}

private fun Number.fillTwoZeroes(): String {
    return when (this) {
        in 0..9 -> "00$this"
        in 10..99 -> "0$this"
        else -> this.toString()
    }
}
