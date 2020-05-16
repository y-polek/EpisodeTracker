package dev.polek.episodetracker.di

import dagger.Module
import dagger.Provides
import dev.polek.episodetracker.common.datasource.omdb.OmdbService
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun tmdbService(): TmdbService = TmdbService()

    @Provides
    @Singleton
    fun omdbService(): OmdbService = OmdbService()
}
