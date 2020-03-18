package dev.polek.episodetracker.common.utils

import io.ktor.util.date.GMTDate

fun currentTimeMillis(): Long = GMTDate().timestamp
