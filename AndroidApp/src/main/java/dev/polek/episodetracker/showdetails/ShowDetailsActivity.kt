package dev.polek.episodetracker.showdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
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

class ShowDetailsActivity : AppCompatActivity(), ShowDetailsView {

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

        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false

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
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    ///////////////////////////////////////////////////////////////////////////
    // region ShowDetailsView implementation

    override fun displayShowHeader(show: ShowHeaderViewModel) {
        binding.imageView.loadImage(show.imageUrl)
        binding.collapsingToolbar.title = showName
        binding.subtitle.text = show.subhead
        binding.rating.text = show.rating
    }

    override fun showProgress() {
        // TODO("not implemented")
    }

    override fun hideProgress() {
        // TODO("not implemented")
    }

    override fun showError() {
        // TODO("not implemented")
    }

    override fun hideError() {
        // TODO("not implemented")
    }

    override fun displayAddToMyShowsButton() {
        // TODO("not implemented")
    }

    override fun displayAddToMyShowsProgress() {
        // TODO("not implemented")
    }

    override fun hideAddToMyShowsButton() {
        // TODO("not implemented")
    }

    override fun displayArchivedBadge() {
        // TODO("not implemented")
    }

    override fun hideArchivedBadge() {
        // TODO("not implemented")
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
        // TODO("not implemented")
    }

    override fun displayOptionsMenu(isInMyShows: Boolean, isArchived: Boolean) {
        // TODO("not implemented")
    }

    override fun displayRemoveConfirmation(callback: (confirmed: Boolean) -> Unit) {
        // TODO("not implemented")
    }

    override fun shareText(text: String) {
        // TODO("not implemented")
    }

    override fun displayContentRatingInfo(rating: String, text: String) {
        // TODO("not implemented")
    }

    override fun displayShowDetails(show: ShowDetailsViewModel) {
        // TODO("not implemented")
    }

    override fun displayTrailers(trailers: List<TrailerViewModel>) {
        // TODO("not implemented")
    }

    override fun displayCast(castMembers: List<CastMemberViewModel>) {
        // TODO("not implemented")
    }

    override fun displayRecommendations(recommendations: List<RecommendationViewModel>) {
        // TODO("not implemented")
    }

    override fun updateRecommendation(show: RecommendationViewModel) {
        // TODO("not implemented")
    }

    override fun displayRemoveRecommendationConfirmation(
        show: RecommendationViewModel,
        callback: (confirmed: Boolean) -> Unit)
    {
        // TODO("not implemented")
    }

    override fun openRecommendation(show: RecommendationViewModel) {
        // TODO("not implemented")
    }

    override fun displayImdbRating(rating: Float) {
        // TODO("not implemented")
    }

    override fun displayEpisodes(seasons: List<SeasonViewModel>) {
        // TODO("not implemented")
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
        // TODO("not implemented")
    }

    override fun hideRefreshProgress() {
        // TODO("not implemented")
    }
    // endregion
    ///////////////////////////////////////////////////////////////////////////

    companion object {

        private const val KEY_SHOW_ID = "key_show_id"
        private const val KEY_SHOW_NAME = "key_show_name"

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
