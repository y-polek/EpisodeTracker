package dev.polek.episodetracker.common.presentation.showdetails.model

data class TrailerViewModel(
    val name: String,
    val youtubeKey: String)
{
    val url = "https://www.youtube.com/watch?v=$youtubeKey"
    val previewImageUrl = "https://img.youtube.com/vi/$youtubeKey/mqdefault.jpg"
}
