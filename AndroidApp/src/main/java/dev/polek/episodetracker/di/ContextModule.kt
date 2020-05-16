package dev.polek.episodetracker.di

import android.content.Context
import androidx.preference.PreferenceManager
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import dev.polek.episodetracker.common.network.Connectivity
import dev.polek.episodetracker.utils.ConnectivityImpl

@Module
class ContextModule(private val appContext: Context) {

    @Provides
    fun appContext(): Context = appContext

    @Provides
    fun connectivity(impl: ConnectivityImpl): Connectivity = impl

    @Provides
    fun settings(context: Context): Settings {
        return AndroidSettings(PreferenceManager.getDefaultSharedPreferences(context))
    }
}
