package dev.polek.episodetracker.common.presentation.showdetails.model

data class ShowHeaderViewModel(
    val name: String,
    val imageUrl: String?,
    val years: String,
    val network: String,
    val networkUrl: String?
)
