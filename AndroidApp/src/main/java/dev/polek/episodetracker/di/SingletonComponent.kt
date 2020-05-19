package dev.polek.episodetracker.di

import dagger.Component
import dev.polek.episodetracker.common.presentation.discover.DiscoverPresenter
import dev.polek.episodetracker.common.presentation.settings.SettingsPresenter
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    ServiceModule::class,
    DatabaseModule::class
])
@Singleton
interface SingletonComponent {

    fun discoverPresenter(): DiscoverPresenter
    fun settingsPresenter(): SettingsPresenter
}
