package dev.polek.episodetracker.common.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodeViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.EpisodesViewModel
import kotlin.test.Test

class EpisodesViewModelTest {

    private val episodes = EpisodesViewModel(
        listOf(
            episode(1),
            episode(3),
            episode(4),
            episode(2)
        )
    )

    @Test
    fun `test 'get(Int)' operator`() {
        assertThat(episodes[0]).isNull()
        assertThat(episodes[1]?.number?.episode).isEqualTo(1)
        assertThat(episodes[2]?.number?.episode).isEqualTo(2)
        assertThat(episodes[3]?.number?.episode).isEqualTo(3)
        assertThat(episodes[4]?.number?.episode).isEqualTo(4)
        assertThat(episodes[42]).isNull()
    }

    @Test
    fun `test get(IntRange)`() {
        assertThat(
            episodes.get(1..4).map { it.number.episode }.toList()
        ).isEqualTo(
            listOf(1, 2, 3, 4)
        )

        assertThat(
            episodes.get(0..42).map { it.number.episode }.toList()
        ).isEqualTo(
            listOf(1, 2, 3, 4)
        )

        assertThat(
            episodes.get(1 until 1).map { it.number.episode }.toList()
        ).isEqualTo(
            emptyList()
        )
    }

    private fun episode(number: Int) = EpisodeViewModel(
        episodeNumber = number,
        seasonNumber = 1,
        name = "",
        airDate = "",
        imageUrl = null,
        isWatched = false,
        isAired = false,
        timeLeftToRelease = ""
    )
}
