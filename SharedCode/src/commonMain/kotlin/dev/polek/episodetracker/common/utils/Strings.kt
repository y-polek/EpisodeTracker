package dev.polek.episodetracker.common.utils

fun String.emptyToNull(): String? = if (this.isEmpty()) null else this
fun String.blankToNull(): String? = if (this.isBlank()) null else this
