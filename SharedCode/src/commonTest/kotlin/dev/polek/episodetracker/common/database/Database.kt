package dev.polek.episodetracker.common.database

import com.squareup.sqldelight.db.SqlDriver

expect fun createInMemorySqlDriver(): SqlDriver
