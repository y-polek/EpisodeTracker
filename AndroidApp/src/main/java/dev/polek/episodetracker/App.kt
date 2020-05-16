package dev.polek.episodetracker

import android.app.Application
import dev.polek.episodetracker.di.ContextModule
import dev.polek.episodetracker.di.DaggerSingletonComponent
import dev.polek.episodetracker.di.SingletonComponent

class App : Application() {

    lateinit var di: SingletonComponent

    override fun onCreate() {
        instance = this
        super.onCreate()

        di = DaggerSingletonComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
    }
}
