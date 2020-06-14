package dev.polek.episodetracker.common.logging

import dev.polek.episodetracker.common.utils.DEBUG_BUILD

inline fun Any.log(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    if (DEBUG_BUILD) {
        printLog(tag = tag, message = message())
    }
}

inline fun Any.logw(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    if (DEBUG_BUILD) {
        printWarningLog(tag = tag, message = message())
    }
}

inline fun Any.loge(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    if (DEBUG_BUILD) {
        printErrorLog(tag = tag, message = message())
    }
}

expect fun printLog(tag: String, message: String)
expect fun printWarningLog(tag: String, message: String)
expect fun printErrorLog(tag: String, message: String)
