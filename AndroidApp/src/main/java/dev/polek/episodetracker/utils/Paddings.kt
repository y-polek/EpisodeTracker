package dev.polek.episodetracker.utils

import android.view.View

fun View.setTopPadding(padding: Int) {
    this.setPadding(this.paddingLeft, padding, this.paddingRight, this.paddingBottom)
}

fun View.setBottomPadding(padding: Int) {
    this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, padding)
}
