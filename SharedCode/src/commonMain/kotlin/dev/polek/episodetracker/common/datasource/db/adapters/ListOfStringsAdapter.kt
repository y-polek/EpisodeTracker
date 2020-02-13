package dev.polek.episodetracker.common.datasource.db.adapters

import com.squareup.sqldelight.ColumnAdapter

object ListOfStringsAdapter : ColumnAdapter<List<String>, String> {

    override fun decode(databaseValue: String): List<String> {
        return databaseValue.split(',')
    }

    override fun encode(value: List<String>): String {
        return value.joinToString(separator = ",")
    }
}
