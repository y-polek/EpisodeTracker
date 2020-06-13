package dev.polek.episodetracker.common.logging

import platform.Foundation.NSLog

actual fun printLog(tag: String, message: String) {
    NSLog("$tag: $message")
}

actual fun printWarningLog(tag: String, message: String) {
    NSLog("⚠️ WARNING/$tag: $message")
}

actual fun printErrorLog(tag: String, message: String) {
    NSLog("❌️ ERROR/$tag: $message")
}
