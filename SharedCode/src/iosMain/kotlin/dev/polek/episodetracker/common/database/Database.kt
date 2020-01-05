package dev.polek.episodetracker.common.database

import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver
import dev.polek.episodetracker.db.Database

val database: Database = Database(driver = NativeSqliteDriver(Database.Schema, "episode_tracker.db"))
