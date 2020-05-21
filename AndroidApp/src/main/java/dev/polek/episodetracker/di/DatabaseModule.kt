package dev.polek.episodetracker.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dev.polek.episodetracker.common.datasource.db.adapters.ListOfStringsAdapter
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.db.MyShow
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun database(appContext: Context): Database {
        return Database(
            driver = AndroidSqliteDriver(Database.Schema, appContext, "episode_tracker.db"),
            MyShowAdapter = MyShow.Adapter(
                genresAdapter = ListOfStringsAdapter,
                networksAdapter = ListOfStringsAdapter)
        )
    }
}
