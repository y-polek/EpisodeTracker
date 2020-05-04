package dev.polek.episodetracker.common.utils

import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import io.ktor.util.date.Month.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatTimeBetweenTest {

    @Test
    fun `test same day`() {
        val start = date(2020, JANUARY, 26, 2)
        val end = date(2020, JANUARY, 26, 19)

        val formatted = formatTimeBetween(start, end)

        assertEquals("Today", formatted)
    }

    @Test
    fun `test day before more than 24h`() {
        val start = date(2020, JANUARY, 25, 12)
        val end = date(2020, JANUARY, 26, 19)

        val formatted = formatTimeBetween(start, end)

        assertEquals("Tomorrow", formatted)
    }

    @Test
    fun `test day before less than 24h`() {
        val start = date(2020, JANUARY, 25, 22)
        val end = date(2020, JANUARY, 26, 19)

        val formatted = formatTimeBetween(start, end)

        assertEquals("Tomorrow", formatted)
    }

    @Test
    fun `test 2 days before more than 48h`() {
        val start = date(2020, JANUARY, 24, 12)
        val end = date(2020, JANUARY, 26, 19)

        val formatted = formatTimeBetween(start, end)

        assertEquals("2 days", formatted)
    }

    @Test
    fun `test 2 days before less than 48h`() {
        val start = date(2020, JANUARY, 24, 23)
        val end = date(2020, JANUARY, 26, 19)

        val formatted = formatTimeBetween(start, end)

        assertEquals("2 days", formatted)
    }

    @Test
    fun `test 1 month before`() {
        val start = date(2020, APRIL, 4, 12)
        val end = date(2020, MAY, 4, 18)

        val formatted = formatTimeBetween(start, end)

        assertEquals("30 days", formatted)
    }

    @Test
    fun `test same day after end date`() {
        val start = date(2020, MAY, 4, 20)
        val end = date(2020, MAY, 4, 18)

        val formatted = formatTimeBetween(start, end)

        assertEquals("Today", formatted)
    }

    @Test
    fun `test next day less than 24h`() {
        val start = date(2020, MAY, 5, 12)
        val end = date(2020, MAY, 4, 18)

        val formatted = formatTimeBetween(start, end)

        assertEquals("Yesterday", formatted)
    }

    @Test
    fun `test next day more than 24h`() {
        val start = date(2020, MAY, 5, 21)
        val end = date(2020, MAY, 4, 18)

        val formatted = formatTimeBetween(start, end)

        assertEquals("Yesterday", formatted)
    }

    @Test
    fun `test 2 days after end date`() {
        val start = date(2020, MAY, 6, 12)
        val end = date(2020, MAY, 4, 18)

        val formatted = formatTimeBetween(start, end)

        assertEquals("May 4", formatted)
    }

    @Test
    fun `test week after end date`() {
        val start = date(2020, JUNE, 13, 12)
        val end = date(2020, JUNE, 6, 1)

        val formatted = formatTimeBetween(start, end)

        assertEquals("June 6", formatted)
    }

    private fun date(year: Int, month: Month, day: Int, hours: Int = 0): GMTDate {
        return GMTDate(
            year = year,
            month = month,
            dayOfMonth = day,
            hours = hours,
            minutes = 0,
            seconds = 0)
    }
}
