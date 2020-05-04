package dev.polek.episodetracker.common.utils

import dev.polek.episodetracker.common.model.EpisodeNumber
import io.ktor.util.date.GMTDate
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.time.DurationUnit.MILLISECONDS
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

fun formatEpisodeNumber(season: Int, episode: Int): String {
    val seasonStr = if (season < 10) "0$season" else season.toString()
    val episodeStr = if (episode < 10) "0$episode" else episode.toString()
    return "S$seasonStr E$episodeStr"
}

fun formatEpisodeNumber(number: EpisodeNumber): String {
    return formatEpisodeNumber(season = number.season, episode = number.episode)
}

@OptIn(ExperimentalTime::class)
fun formatTimeBetween(startDate: GMTDate, endDate: GMTDate): String {
    val diffInDays = (endDate.timestamp - startDate.timestamp).toDuration(MILLISECONDS).inDays
    val startMinutes = startDate.hours * 60 + startDate.minutes
    val endMinutes = endDate.hours * 60 + endDate.minutes

    val days = (if (startMinutes > endMinutes) ceil(diffInDays) else floor(diffInDays)).toInt()

    return when (days) {
        -1 -> "Yesterday"
        0 -> "Today"
        1 -> "Tomorrow"
        in 2..Int.MAX_VALUE -> "$days days"
        in Int.MIN_VALUE..-2 -> "${endDate.month.fullName} ${endDate.dayOfMonth}"
        else -> "$days days"
    }
}
