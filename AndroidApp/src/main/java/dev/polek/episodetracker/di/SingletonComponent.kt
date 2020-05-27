package dev.polek.episodetracker.di

import dagger.Component
import dev.polek.episodetracker.common.presentation.app.AppPresenter
import dev.polek.episodetracker.common.presentation.discover.DiscoverPresenter
import dev.polek.episodetracker.common.presentation.main.MainPresenter
import dev.polek.episodetracker.common.presentation.myshows.MyShowsPresenter
import dev.polek.episodetracker.common.presentation.settings.SettingsPresenter
import dev.polek.episodetracker.common.presentation.towatch.ToWatchPresenter
import javax.inject.Singleton

@Component(modules = [
    ContextModule::class,
    ServiceModule::class,
    DatabaseModule::class
])
@Singleton
interface SingletonComponent {

    fun appPresenter(): AppPresenter
    fun mainPresenter(): MainPresenter
    fun myShowsPresenter(): MyShowsPresenter
    fun toWatchPresenter(): ToWatchPresenter
    fun discoverPresenter(): DiscoverPresenter
    fun settingsPresenter(): SettingsPresenter
}
