package dev.polek.episodetracker.common.presentation.showdetails.model

import io.ktor.http.URLBuilder

data class CastMemberViewModel(
    val name: String,
    val character: String,
    val portraitImageUrl: String?)
{
    val wikipediaUrl: String by lazy {
        URLBuilder("https://m.wikipedia.org/wiki/$name").buildString()
    }
}
