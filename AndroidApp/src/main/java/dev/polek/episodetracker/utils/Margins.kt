package dev.polek.episodetracker.utils

import android.view.View
import android.view.ViewGroup

fun View.setTopMargin(margin: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = margin
    this.layoutParams = layoutParams
}
