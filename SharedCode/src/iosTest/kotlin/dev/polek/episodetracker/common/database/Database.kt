package dev.polek.episodetracker.common.database

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver
import com.squareup.sqldelight.drivers.ios.wrapConnection
import dev.polek.episodetracker.db.Database

private val configuration = DatabaseConfiguration(
    inMemory = true,
    name = "episode_tracker_test_db",
    version = Database.Schema.version,
    create = { connection ->
        wrapConnection(connection) { driver ->
            Database.Schema.create(driver)
        }
    }
)

actual val sqlDriver: SqlDriver = NativeSqliteDriver(configuration)
