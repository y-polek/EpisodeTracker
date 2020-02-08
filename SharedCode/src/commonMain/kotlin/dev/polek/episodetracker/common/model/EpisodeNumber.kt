package dev.polek.episodetracker.common.model

import kotlin.math.sign

data class EpisodeNumber(val season: Int, val episode: Int) {

    operator fun compareTo(other: EpisodeNumber): Int {
        if (this.season < other.season) return -1
        if (this.season > other.season) return 1
        return (this.episode - other.episode).sign
    }
}
