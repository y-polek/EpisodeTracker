package dev.polek.episodetracker.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

fun <T : View> ViewGroup.findChildOfType(type: Class<T>): T? {
    if (this.childCount == 0) return null

    var result = this.children.find { type.isAssignableFrom(it.javaClass) } as T?
    if (result != null) return result

    for (child in this.children) {
        if (child is ViewGroup) {
            result = child.findChildOfType(type)
            if (result != null) break
        }
    }

    return result
}
