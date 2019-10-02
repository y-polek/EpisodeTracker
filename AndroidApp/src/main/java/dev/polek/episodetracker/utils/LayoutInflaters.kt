package dev.polek.episodetracker.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

val View.layoutInflater: LayoutInflater
    get() = this.context.layoutInflater
