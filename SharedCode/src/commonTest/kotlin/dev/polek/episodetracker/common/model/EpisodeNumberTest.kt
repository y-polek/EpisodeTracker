package dev.polek.episodetracker.common.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EpisodeNumberTest {

    @Test
    fun `test compareTo() same season`() {
        assertEquals(
            e(1, 7), e(1, 7)
        )
        assertTrue(
            e(1, 5) < e(1, 6)
        )
        assertTrue(
            e(3, 5) < e(4, 1)
        )
        assertTrue(
            e(10, 4) > e(10, 1)
        )
        assertTrue(
            e(20, 4) > e(1, 25)
        )
    }

    companion object {
        fun e(season: Int, episode: Int) = EpisodeNumber(season, episode)
    }
}
