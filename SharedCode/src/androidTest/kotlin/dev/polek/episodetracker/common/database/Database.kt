package dev.polek.episodetracker.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import dev.polek.episodetracker.db.Database

actual fun createInMemorySqlDriver(): SqlDriver {
    val driver = JdbcSqliteDriver(IN_MEMORY)
    Database.Schema.create(driver)
    return driver
}
