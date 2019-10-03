package dev.polek.episodetracker.utils

import android.content.Context
import androidx.annotation.Px
import android.util.TypedValue

@Px
fun Context.dp2px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics).toInt()
}
