package dev.polek.episodetracker.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import dev.polek.episodetracker.db.Database

actual val sqlDriver: SqlDriver = JdbcSqliteDriver(IN_MEMORY).apply {
    Database.Schema.create(this)
}
