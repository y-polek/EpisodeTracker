package dev.polek.episodetracker

import android.app.Application
import dev.polek.episodetracker.di.ContextModule
import dev.polek.episodetracker.di.DaggerSingletonComponent
import dev.polek.episodetracker.di.SingletonComponent

class App : Application() {

    lateinit var component: SingletonComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerSingletonComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }
}
