package dev.polek.episodetracker.common.utils

import io.ktor.util.InternalAPI
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.util.date.Month.*

private val dateRegex = "^([\\d]{4})-([\\d]{1,2})-([\\d]{1,2})$".toRegex()

@UseExperimental(InternalAPI::class)
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
    val month = date.month.fullName
    val day = date.dayOfMonth

    return "$month $day, $year"
}

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
