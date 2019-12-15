package dev.polek.episodetracker.common.coroutines

import kotlin.coroutines.CoroutineContext

expect val ui: CoroutineContext

expect val io: CoroutineContext

expect fun runBlocking(block: suspend () -> Unit)
