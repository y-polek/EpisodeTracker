package dev.polek.episodetracker.common.logging

inline fun Any.log(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    printLog(tag = tag, message = message())
}

inline fun Any.logw(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    printWarningLog(tag = tag, message = message())
}

inline fun Any.loge(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    printErrorLog(tag = tag, message = message())
}

expect fun printLog(tag: String, message: String)
expect fun printWarningLog(tag: String, message: String)
expect fun printErrorLog(tag: String, message: String)
