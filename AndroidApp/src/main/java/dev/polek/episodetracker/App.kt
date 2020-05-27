package dev.polek.episodetracker

import android.app.Application
import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.presentation.app.AppPresenter
import dev.polek.episodetracker.common.presentation.app.AppView
import dev.polek.episodetracker.di.ContextModule
import dev.polek.episodetracker.di.DaggerSingletonComponent
import dev.polek.episodetracker.di.SingletonComponent
import dev.polek.episodetracker.utils.apply
import javax.inject.Singleton

@Singleton
class App : Application(), AppView {

    lateinit var di: SingletonComponent
    lateinit var presenter: AppPresenter

    override fun onCreate() {
        instance = this
        super.onCreate()

        di = DaggerSingletonComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        presenter = di.appPresenter()
        presenter.attachView(this)
    }

    override fun setAppearance(appearance: Appearance) {
        appearance.apply()
    }

    companion object {
        lateinit var instance: App
    }
}
