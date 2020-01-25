package dev.polek.episodetracker.common.utils

import dev.polek.episodetracker.common.model.EpisodeNumber

fun formatEpisodeNumber(season: Int, episode: Int): String {
    val seasonStr = if (season < 10) "0$season" else season.toString()
    val episodeStr = if (episode < 10) "0$episode" else episode.toString()
    return "S$seasonStr E$episodeStr"
}

fun EpisodeNumber.format(): String = formatEpisodeNumber(season = this.season, episode = this.episode)
