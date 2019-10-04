package dev.polek.episodetracker.utils

import android.view.View
import com.google.android.material.appbar.AppBarLayout

/**
 * View must be a child of [AppBarLayout]
 */
var View.scrollFlags: Int
    get() = (this.layoutParams as AppBarLayout.LayoutParams).scrollFlags
    set(value) {
        val layoutParams = this.layoutParams as AppBarLayout.LayoutParams
        layoutParams.scrollFlags = value
        this.layoutParams = layoutParams
    }
