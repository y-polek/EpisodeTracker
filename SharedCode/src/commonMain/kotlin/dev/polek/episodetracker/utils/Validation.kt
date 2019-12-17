package dev.polek.episodetracker.utils

fun allNotNull(vararg values: Any?): Boolean {
    for (value in values) {
        value ?: return false
    }
    return true
}
