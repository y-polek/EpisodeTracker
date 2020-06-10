package dev.polek.episodetracker.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun Context.openUrl(url: String) {
    val intent = CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .build()
    intent.launchUrl(this, Uri.parse(url))
}
