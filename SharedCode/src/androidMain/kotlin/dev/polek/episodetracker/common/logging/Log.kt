@file:JvmName("LogImpl")
package dev.polek.episodetracker.common.logging

import android.util.Log

actual fun printLog(tag: String, message: String) {
    Log.d(tag, message)
}

actual fun printWarningLog(tag: String, message: String) {
    Log.w(tag, message)
}

actual fun printErrorLog(tag: String, message: String) {
    Log.e(tag, message)
}
