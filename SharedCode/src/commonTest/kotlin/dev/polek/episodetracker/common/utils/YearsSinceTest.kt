package dev.polek.episodetracker.common.utils

import dev.polek.episodetracker.common.testutils.date
import io.ktor.util.date.GMTDate
import kotlin.test.Test
import kotlin.test.assertEquals

class YearsSinceTest {

    @Test
    fun `test less than a year ago`() {
        val now = GMTDate()
        val date = date(year = now.year, month = now.month, day = now.dayOfMonth - 100)

        assertEquals(0, yearsSince(date))
    }

    @Test
    fun `test more than a year ago`() {
        val now = GMTDate()
        val date = date(year = now.year - 1, month = now.month, day = now.dayOfMonth - 10)

        assertEquals(1, yearsSince(date))
    }

    @Test
    fun `test less than a year in future`() {
        val now = GMTDate()
        val date = date(year = now.year, month = now.month, day = now.dayOfMonth + 100)

        assertEquals(0, yearsSince(date))
    }

    @Test
    fun `test future more than a year in future`() {
        val now = GMTDate()
        val date = date(year = now.year + 1, month = now.month, day = now.dayOfMonth + 10)

        assertEquals(-1, yearsSince(date))
    }

    @Test
    fun `test today`() {
        val now = GMTDate()
        assertEquals(0, yearsSince(now))
    }
}
