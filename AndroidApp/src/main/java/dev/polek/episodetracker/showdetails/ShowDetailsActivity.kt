package dev.polek.episodetracker.showdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import dev.icerock.moko.resources.desc.StringDesc
import dev.polek.episodetracker.App
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.presentation.showdetails.model.*
import dev.polek.episodetracker.common.presentation.showdetails.presenter.ShowDetailsPresenter
import dev.polek.episodetracker.common.presentation.showdetails.view.ShowDetailsView
import dev.polek.episodetracker.databinding.ShowDetailsActivityBinding
import dev.polek.episodetracker.showdetails.about.AboutShowFragment
import dev.polek.episodetracker.showdetails.episodes.EpisodesFragment
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.loadImage

class ShowDetailsActivity : AppCompatActivity(), ShowDetailsView,
    AboutShowFragment.Listener, EpisodesFragment.Listener,
    ShowDetailsMenuDialog.Listener
{

    private val showId: Int by lazy {
        intent.getIntExtra(KEY_SHOW_ID, -1)
    }
    private val showName: String by lazy {
        requireNotNull(intent.getStringExtra(KEY_SHOW_NAME))
    }
    private val presenter: ShowDetailsPresenter by lazy {
        App.instance.di.showDetailsPresenterFactory().create(showId)
    }
    private lateinit var binding: ShowDetailsActivityBinding
    private var aboutFragment: AboutShowFragment? = null
    private var episodesFragment: EpisodesFragment? = null

    private var show: ShowDetailsViewModel? = null
    private var trailers: List<TrailerViewModel>? = null
    private var cast: List<CastMemberViewModel>? = null
    private var recommendations: List<RecommendationViewModel>? = null
    private var imdbRating: Float? = null
    private var seasons: List<SeasonViewModel>? = null

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position == 1) {
                presenter.onEpisodesTabSelected()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.collapsingToolbar.title = showName

        binding.rating.doOnClick {
            presenter.onContentRatingClicked()
        }

        binding.addButton.doOnClick {
            presenter.onAddToMyShowsClicked()
        }

        binding.retryButton.doOnClick {
            presenter.onRetryButtonClicked()
        }

        binding.archivedBadge.doOnClick {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.unarchive_show_confirmation_title)
                .setMessage(R.string.unarchive_show_confirmation_message)
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.action_unarchive) { _, _ ->
                    presenter.onUnarchiveShowClicked()
                }
                .show()
        }

        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            val textResId = when (position) {
                0 -> R.string.tab_about
                1 -> R.string.tab_episodes
                else -> throw NotImplementedError("Unknown tab position: $position")
            }
            tab.setText(textResId)
        }.attach()

        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is AboutShowFragment -> {
                aboutFragment = fragment
                show?.let(fragment::displayShowDetails)
                trailers?.let(fragment::displayTrailers)
                cast?.let(fragment::displayCast)
                recommendations?.let(fragment::displayRecommendations)
                imdbRating?.let(fragment::displayImdbRating)
            }
            is EpisodesFragment -> {
                episodesFragment = fragment
                seasons?.let(fragment::displayEpisodes)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (binding.addButtonLayout.isVisible) {
            setBottomPadding()
        } else {
            removeBottomPadding()
        }

        presenter.onViewAppeared()
    }

    override fun onStop() {
        presenter.onViewDisappeared()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.show_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_more -> {
                presenter.onMenuClicked()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onRecommendationClicked(show: RecommendationViewModel) {
        presenter.onRecommendationClicked(show)
    }

    override fun onAddRecommendationClicked(show: RecommendationViewModel) {
        presenter.onAddRecommendationClicked(show)
    }

    override fun onRemoveRecommendationClicked(show: RecommendationViewModel) {
        presenter.onRemoveRecommendationClicked(show)
    }

    override fun onShowSwipeRefresh() {
        presenter.onRefreshRequested()
    }

    override fun onSeasonWatchedStateToggled(season: SeasonViewModel) {
        presenter.onSeasonWatchedStateToggled(season)
    }

    override fun onEpisodeWatchedStateToggled(episode: EpisodeViewModel) {
        presenter.onEpisodeWatchedStateToggled(episode)
    }

    override fun onShareMenuClicked() {
        presenter.onShareShowClicked()
    }

    override fun onMarkWatchedMenuClicked() {
        presenter.onMarkWatchedClicked()
    }

    override fun onArchiveMenuClicked() {
        presenter.onArchiveShowClicked()
    }

    override fun onUnarchiveMenuClicked() {
        presenter.onUnarchiveShowClicked()
    }

    override fun onAddMenuClicked() {
        presenter.onAddToMyShowsClicked()
    }

    override fun onRemoveMenuClicked() {
        presenter.onRemoveShowClicked()
    }

    ///////////////////////////////////////////////////////////////////////////
    // region ShowDetailsView implementation

    override fun displayShowHeader(show: ShowHeaderViewModel) {
        binding.imageView.loadImage(show.imageUrl)
        binding.collapsingToolbar.title = showName
        binding.subtitle.text = show.subhead
        binding.rating.text = show.rating
        binding.contentLayout.isVisible = true
    }

    override fun showProgress() {
        binding.contentLayout.isVisible = false
        binding.progressbar.isVisible = true
    }

    override fun hideProgress() {
        binding.progressbar.isVisible = false
    }

    override fun showError() {
        binding.contentLayout.isVisible = false
        binding.errorView.isVisible = true
    }

    override fun hideError() {
        binding.errorView.isVisible = false
    }

    override fun displayAddToMyShowsButton() {
        binding.addButtonLayout.isVisible = true
        binding.addButtonProgress.isVisible = false
        binding.addButton.isEnabled = true
        setBottomPadding()
    }

    override fun displayAddToMyShowsProgress() {
        binding.addButtonProgress.isVisible = true
        binding.addButton.isEnabled = false
    }

    override fun hideAddToMyShowsButton() {
        binding.addButtonLayout.isVisible = false
        removeBottomPadding()
    }

    override fun displayArchivedBadge() {
        binding.archivedBadge.isVisible = true
    }

    override fun hideArchivedBadge() {
        binding.archivedBadge.isVisible = false
    }

    override fun displayAddToMyShowsConfirmation(
        showName: String,
        callback: (confirmed: Boolean) -> Unit)
    {
        // TODO("not implemented")
    }

    override fun showCheckAllPreviousEpisodesPrompt(
        onCheckAllPrevious: () -> Unit,
        onCheckOnlyThis: () -> Unit,
        onCancel: () -> Unit)
    {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.episodes_check_previous_confirmation)
            .setPositiveButton(R.string.action_check_all_previous) { _, _ ->
                onCheckAllPrevious()
            }
            .setNegativeButton(R.string.action_check_one) { _, _ ->
                onCheckOnlyThis()
            }
            .setOnCancelListener {
                onCancel()
            }
            .show()
    }

    override fun displayOptionsMenu(isInMyShows: Boolean, isArchived: Boolean) {
        ShowDetailsMenuDialog.instance(isInMyShows = isInMyShows, isArchived = isArchived)
            .show(supportFragmentManager, TAG_MENU_DIALOG)
    }

    override fun displayRemoveConfirmation(callback: (confirmed: Boolean) -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.remove_show_confirmation, showName))
            .setPositiveButton(R.string.action_remove) { _, _ -> callback(true) }
            .setNegativeButton(R.string.action_cancel) { _, _ -> callback(false) }
            .show()
    }

    override fun shareText(text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    override fun displayContentRatingInfo(rating: String, text: StringDesc) {
        MaterialAlertDialogBuilder(this)
            .setTitle(rating)
            .setMessage(text.toString(this))
            .setPositiveButton(R.string.action_ok, null)
            .show()
    }

    override fun displayShowDetails(show: ShowDetailsViewModel) {
        this.show = show
        aboutFragment?.displayShowDetails(show)
    }

    override fun displayTrailers(trailers: List<TrailerViewModel>) {
        this.trailers = trailers
        aboutFragment?.displayTrailers(trailers)
    }

    override fun displayCast(castMembers: List<CastMemberViewModel>) {
        this.cast = castMembers
        aboutFragment?.displayCast(castMembers)
    }

    override fun displayRecommendations(recommendations: List<RecommendationViewModel>) {
        this.recommendations = recommendations
        aboutFragment?.displayRecommendations(recommendations)
    }

    override fun updateRecommendation(show: RecommendationViewModel) {
        aboutFragment?.updateRecommendation(show)
    }

    override fun displayRemoveRecommendationConfirmation(
        show: RecommendationViewModel,
        callback: (confirmed: Boolean) -> Unit)
    {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.remove_show_confirmation, show.name))
            .setPositiveButton(R.string.action_remove) { _, _ -> callback(true) }
            .setNegativeButton(R.string.action_cancel) { _, _ -> callback(false) }
            .show()
    }

    override fun openRecommendation(show: RecommendationViewModel) {
        startActivity(intent(this, show.showId, show.name))
    }

    override fun displayImdbRating(rating: Float) {
        this.imdbRating = rating
        aboutFragment?.displayImdbRating(rating)
    }

    override fun displayEpisodes(seasons: List<SeasonViewModel>) {
        this.seasons = seasons
        episodesFragment?.displayEpisodes(seasons)
    }

    override fun showEpisodesProgress() {
        // TODO("not implemented")
    }

    override fun hideEpisodesProgress() {
        // TODO("not implemented")
    }

    override fun showEpisodesError() {
        // TODO("not implemented")
    }

    override fun hideEpisodesError() {
        // TODO("not implemented")
    }

    override fun reloadSeason(number: Int) {
        episodesFragment?.reloadSeason(number)
    }

    override fun hideRefreshProgress() {
        aboutFragment?.hideRefreshProgress()
        episodesFragment?.hideRefreshProgress()
    }
    // endregion
    ///////////////////////////////////////////////////////////////////////////

    private fun setBottomPadding() {
        val padding = resources.getDimensionPixelOffset(R.dimen.add_button_layout_height)
        aboutFragment?.setBottomPadding(padding)
        episodesFragment?.setBottomPadding(padding)
    }

    private fun removeBottomPadding() {
        val padding = resources.getDimensionPixelOffset(R.dimen.scroll_view_bottom_padding)
        aboutFragment?.setBottomPadding(padding)
        episodesFragment?.setBottomPadding(padding)
    }

    companion object {

        private const val KEY_SHOW_ID = "key_show_id"
        private const val KEY_SHOW_NAME = "key_show_name"

        private const val TAG_MENU_DIALOG = "tag_menu_dialog"

        fun intent(context: Context, showId: Int, showName: String): Intent {
            return Intent(context, ShowDetailsActivity::class.java)
                .putExtra(KEY_SHOW_ID, showId)
                .putExtra(KEY_SHOW_NAME, showName)
        }
    }

    private class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AboutShowFragment.instance()
                1 -> EpisodesFragment.instance()
                else -> throw NotImplementedError("Unknown tab position: $position")
            }
        }
    }
}
