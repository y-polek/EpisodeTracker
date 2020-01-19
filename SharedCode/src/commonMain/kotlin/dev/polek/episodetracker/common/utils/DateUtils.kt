package dev.polek.episodetracker.common.utils

import dev.polek.episodetracker.common.logging.log
import io.ktor.util.InternalAPI
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

private val dateRegex = "^([\\d]{4})-([\\d]{1,2})-([\\d]{1,2})$".toRegex()

@UseExperimental(InternalAPI::class)
fun parseDate(dateStr: String): GMTDate? {

    val groups = dateRegex.find(dateStr)?.groups
    if (groups != null && groups.size == 4) {
        val year = groups[1]?.value?.toIntOrNull() ?: return null
        val month = groups[2]?.value?.toIntOrNull() ?: return null
        val day = groups[3]?.value?.toIntOrNull() ?: return null
        val date = GMTDate(
            year = year,
            month = Month.from(month - 1),
            dayOfMonth = day,
            hours = 0,
            minutes = 0,
            seconds = 0)
        log("$dateStr = $date")
        return date
    }
    return null
}

fun Long.millisToDate(): GMTDate = GMTDate(this)

@UseExperimental(ExperimentalTime::class)
fun millisToDays(millis: Long): Long {
    return millis.toDuration(DurationUnit.MILLISECONDS).inDays.toLong()
}
