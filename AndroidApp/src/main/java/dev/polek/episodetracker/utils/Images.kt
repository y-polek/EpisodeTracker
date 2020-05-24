package dev.polek.episodetracker.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import dev.polek.episodetracker.R

fun ImageView.loadImage(url: String?) {
    val requestManager = (this.getTag(R.id.glide_request_manager_tag) as? RequestManager)
        ?: Glide.with(this)
    this.setTag(R.id.glide_request_manager_tag, requestManager)

    requestManager
        .load(url)
        .transition(DrawableTransitionOptions.with(CrossFadeRemoteTransitionFactory))
        .into(this)
}

private object CrossFadeRemoteTransitionFactory : TransitionFactory<Drawable> {

    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> {
        return when (dataSource) {
            DataSource.REMOTE -> {
                DrawableCrossFadeFactory.Builder(300)
                    .build()
                    .build(dataSource, isFirstResource)
            }
            else -> NoTransition.get()
        }
    }
}
