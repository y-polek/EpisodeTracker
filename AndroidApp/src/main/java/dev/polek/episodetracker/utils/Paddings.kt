package dev.polek.episodetracker.utils

import android.view.View

fun View.setTopPadding(padding: Int) {
    this.setPadding(this.paddingLeft, padding, this.paddingRight, this.paddingBottom)
}
