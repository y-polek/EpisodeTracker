package dev.polek.episodetracker.common.utils

import kotlinx.coroutines.*

object Timer {

    fun CoroutineScope.doAtMidnight(callback: () -> Unit): Job {
        return launch {
            while (true) {
                delay(millisUntilMidnight())
                callback()
            }
        }
    }
}
