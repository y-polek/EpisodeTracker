package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.database.sqlDriver
import dev.polek.episodetracker.db.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class MyShowsRepositoryTest {

    private lateinit var db: Database

    @BeforeTest
    fun setup() {
        db = Database(sqlDriver)
    }

    @AfterTest
    fun tearDown() {
        sqlDriver.close()
    }
}
