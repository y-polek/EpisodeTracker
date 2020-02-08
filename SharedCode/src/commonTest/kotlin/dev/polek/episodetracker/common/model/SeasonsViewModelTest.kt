package dev.polek.episodetracker.common.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonViewModel
import dev.polek.episodetracker.common.presentation.showdetails.model.SeasonsViewModel
import kotlin.test.Test

class SeasonsViewModelTest {

    private val seasons = SeasonsViewModel(
        listOf(
            SeasonViewModel(
                number = 1,
                episodes = listOf()),
            SeasonViewModel(
                number = 3,
                episodes = listOf()),
            SeasonViewModel(
                number = 4,
                episodes = listOf()),
            SeasonViewModel(
                number = 2,
                episodes = listOf())
        )
    )

    @Test
    fun `test 'get(Int)' operator`() {
        assertThat(seasons[0]).isNull()
        assertThat(seasons[1]?.number).isEqualTo(1)
        assertThat(seasons[2]?.number).isEqualTo(2)
        assertThat(seasons[3]?.number).isEqualTo(3)
        assertThat(seasons[4]?.number).isEqualTo(4)
        assertThat(seasons[42]).isNull()
    }

    @Test
    fun `test get(IntRange)`() {
        assertThat(
            seasons.get(1..4).map(SeasonViewModel::number).toList()
        ).isEqualTo(
            listOf(1, 2, 3, 4)
        )

        assertThat(
            seasons.get(0..42).map(SeasonViewModel::number).toList()
        ).isEqualTo(
            listOf(1, 2, 3, 4)
        )

        assertThat(
            seasons.get(1 until 1).map(SeasonViewModel::number).toList()
        ).isEqualTo(
            emptyList()
        )
    }
}
