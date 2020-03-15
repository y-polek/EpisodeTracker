package dev.polek.episodetracker.common.logging

import io.ktor.util.date.GMTDate

inline fun Any.log(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    println("${timestamp()} $tag: ${message()}")
}

inline fun Any.logw(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    println("${timestamp()} WARNING/$tag: ${message()}")
}

inline fun Any.loge(tag: String = this::class.simpleName.orEmpty(), message: () -> String) {
    println("${timestamp()} ERROR/$tag: ${message()}")
}

fun timestamp(): String {
    val date = GMTDate()
    val year = date.year
    val month = (date.month.ordinal + 1).fillZero()
    val day = date.dayOfMonth.fillZero()
    val hours = date.hours.fillZero()
    val minutes = date.minutes.fillZero()
    val seconds = date.seconds.fillZero()
    val millis = (date.timestamp % 1000).fillTwoZeroes()

    return "$year-$month-$day $hours:$minutes:$seconds.$millis"
}

private fun Number.fillZero(): String {
    return when (this) {
        in 0..9 -> "0$this"
        else -> this.toString()
    }
}

private fun Number.fillTwoZeroes(): String {
    return when (this) {
        in 0..9 -> "00$this"
        in 10..99 -> "0$this"
        else -> this.toString()
    }
}

fun logw(message: String) {
    println("WARNING: $message")
}
