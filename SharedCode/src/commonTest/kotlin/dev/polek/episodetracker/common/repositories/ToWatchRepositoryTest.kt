package dev.polek.episodetracker.common.repositories

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.squareup.sqldelight.db.SqlDriver
import dev.polek.episodetracker.common.coroutines.runBlocking
import dev.polek.episodetracker.common.database.createInMemorySqlDriver
import dev.polek.episodetracker.common.datasource.themoviedb.TmdbService
import dev.polek.episodetracker.common.testutils.TmdbShows.THE_ORVILLE
import dev.polek.episodetracker.common.testutils.mockTmdbHttpClient
import dev.polek.episodetracker.common.utils.parseDate
import dev.polek.episodetracker.db.Database
import kotlin.test.*

class ToWatchRepositoryTest {

    private lateinit var sqlDriver: SqlDriver
    private lateinit var db: Database
    private lateinit var myShowsRepository: MyShowsRepository
    private lateinit var toWatchRepository: ToWatchRepository

    @BeforeTest
    fun setup() {
        sqlDriver = createInMemorySqlDriver()
        db = Database(sqlDriver)
        val tmdbService = TmdbService(client = mockTmdbHttpClient)

        myShowsRepository = MyShowsRepository(db, tmdbService)
        toWatchRepository = ToWatchRepository(db)
    }

    @AfterTest
    fun tearDown() {
        sqlDriver.close()
    }

    @Test
    fun `test toWatchShow`() = runBlocking {
        now("2017-09-01") // before S01 E01
        myShowsRepository.addShow(THE_ORVILLE.id)

        var show = toWatchRepository.toWatchShow(THE_ORVILLE.id)
        assertNull(show)

        now("2017-09-15") // between S01 E01 and S01 E02
        show = toWatchRepository.toWatchShow(THE_ORVILLE.id)
        assertNotNull(show)
        assertThat(show.episodeCount).isEqualTo(1)

        now("2017-12-20") // between S01 E12 and S02 E01
        show = toWatchRepository.toWatchShow(THE_ORVILLE.id)
        assertNotNull(show)
        assertThat(show.episodeCount).isEqualTo(12)

        now("2020-01-01") // after S02 E14 (before S03 E01)
        show = toWatchRepository.toWatchShow(THE_ORVILLE.id)
        assertNotNull(show)
        assertThat(show.episodeCount).isEqualTo(26)
    }

    private fun String.millis(): Long = requireNotNull(parseDate(this)).timestamp

    private fun now(date: String) {
        db.timeQueries.mockCurrentTimeMillis(date.millis())
    }
}
