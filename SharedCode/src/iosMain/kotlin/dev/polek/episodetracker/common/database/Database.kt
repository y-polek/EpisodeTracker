package dev.polek.episodetracker.common.database

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import dev.polek.episodetracker.common.datasource.db.adapters.ListOfStringsAdapter
import dev.polek.episodetracker.db.Database
import dev.polek.episodetracker.db.MyShow

val database: Database = Database(
    driver = NativeSqliteDriver(Database.Schema, "episode_tracker.db"),
    MyShowAdapter = MyShow.Adapter(
        genresAdapter = ListOfStringsAdapter,
        networksAdapter = ListOfStringsAdapter)
)
