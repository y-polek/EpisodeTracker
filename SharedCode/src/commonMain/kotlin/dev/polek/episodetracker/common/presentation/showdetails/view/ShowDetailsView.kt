package dev.polek.episodetracker.common.presentation.showdetails.view

import dev.polek.episodetracker.common.presentation.showdetails.model.ShowHeaderViewModel

interface ShowDetailsView {

    fun displayShowHeader(show: ShowHeaderViewModel)
    fun showProgress()
    fun hideProgress()
    fun showError()
    fun hideError()
    fun displayAddToMyShowsButton()
    fun displayAddToMyShowsProgress()
    fun hideAddToMyShowsButton()
    fun close()
}
