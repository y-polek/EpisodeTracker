@file:JvmName("CoroutinesActual")
package dev.polek.episodetracker.common.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val ui: CoroutineContext = Dispatchers.Main

actual val io: CoroutineContext = Dispatchers.IO

actual fun runBlocking(block: suspend () -> Unit) = kotlinx.coroutines.runBlocking { block() }
