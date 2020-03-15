package dev.polek.episodetracker.common.logging

import dev.polek.episodetracker.common.utils.formatTimestamp

inline fun Any.log(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    println("${formatTimestamp()} $tag: ${message()}")
}

inline fun Any.logw(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    println("${formatTimestamp()} WARNING/$tag: ${message()}")
}

inline fun Any.loge(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    println("${formatTimestamp()} ERROR/$tag: ${message()}")
}
