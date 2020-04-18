package dev.polek.episodetracker.common.testutils

import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month

fun date(year: Int, month: Month, day: Int, hours: Int = 0): GMTDate {
    return GMTDate(
        year = year,
        month = month,
        dayOfMonth = day,
        hours = hours,
        minutes = 0,
        seconds = 0)
}
