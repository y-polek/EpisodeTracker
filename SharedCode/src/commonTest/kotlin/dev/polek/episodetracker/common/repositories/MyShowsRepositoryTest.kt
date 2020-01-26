package dev.polek.episodetracker.common.repositories

import dev.polek.episodetracker.common.coroutines.runBlocking
import dev.polek.episodetracker.common.database.sqlDriver
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.testutils.TmdbShows.THE_ORVILLE
import dev.polek.episodetracker.common.testutils.mockTmdbHttpClient
import dev.polek.episodetracker.db.Database
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class MyShowsRepositoryTest {

    private lateinit var repository: MyShowsRepository

    @BeforeTest
    fun setup() {
        repository = MyShowsRepository(
            db = Database(sqlDriver),
            tmdbService = TmdbService(client = mockTmdbHttpClient)
        )
    }

    @AfterTest
    fun tearDown() {
        sqlDriver.close()
    }

    @Test
    fun `test resources`() = runBlocking {
        repository.addShow(THE_ORVILLE.id)
        assertTrue(repository.isInMyShows(THE_ORVILLE.id))
    }
}
