package dev.polek.episodetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.polek.episodetracker.common.presentation.app.AppPresenter
import dev.polek.episodetracker.common.presentation.main.MainPresenter
import dev.polek.episodetracker.common.presentation.main.MainView
import dev.polek.episodetracker.databinding.MainActivityBinding
import dev.polek.episodetracker.discover.DiscoverFragment
import dev.polek.episodetracker.myshows.MyShowsFragment
import dev.polek.episodetracker.settings.SettingsFragment
import dev.polek.episodetracker.towatch.ToWatchFragment

class MainActivity : AppCompatActivity(), MainView {

    private val presenter: MainPresenter = App.instance.di.mainPresenter()
    private val appPresenter: AppPresenter = App.instance.di.appPresenter()
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter = PageAdapter(this)
        binding.pager.offscreenPageLimit = NUMBER_OF_PAGES - 1
        binding.pager.isUserInputEnabled = false

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val pagePosition = when (menuItem.itemId) {
                R.id.action_my_shows -> 0
                R.id.action_to_watch -> 1
                R.id.action_discover -> 2
                R.id.action_settings -> 3
                else -> throw NotImplementedError("Unknown menu item: $menuItem")
            }
            if (binding.pager.currentItem != pagePosition) {
                binding.pager.setCurrentItem(pagePosition, false)
            } else {
                scrollToTop(pagePosition)
            }
            return@setOnNavigationItemSelectedListener true
        }

        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()
        binding.bottomNavigation.setOnNavigationItemSelectedListener(null)
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        appPresenter.onViewAppeared()
        presenter.onViewAppeared()
    }

    override fun onStop() {
        presenter.onViewDisappeared()
        appPresenter.onViewDisappeared()
        super.onStop()
    }

    fun openDiscoverTab() {
        binding.bottomNavigation.selectedItemId = R.id.action_discover
        findFragmentOfType<DiscoverFragment>()?.focusSearch()
    }

    ///////////////////////////////////////////////////////////////////////////
    // region MainView implementation

    override fun showToWatchBadge(count: Int) {
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.action_to_watch)
        badge.number = count
    }

    override fun hideToWatchBadge() {
        binding.bottomNavigation.removeBadge(R.id.action_to_watch)
    }
    // endregion
    ///////////////////////////////////////////////////////////////////////////

    private fun scrollToTop(pagePosition: Int) {
        when (pagePosition) {
            0 -> findFragmentOfType<MyShowsFragment>()?.scrollToTop()
            1 -> findFragmentOfType<ToWatchFragment>()?.scrollToTop()
            2 -> findFragmentOfType<DiscoverFragment>()?.scrollToTop()
        }
    }

    private inline fun <reified T: Fragment> findFragmentOfType(): T? {
        return supportFragmentManager.fragments.firstOrNull { it is T } as T?
    }

    private class PageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = NUMBER_OF_PAGES

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> MyShowsFragment.instance()
            1 -> ToWatchFragment.instance()
            2 -> DiscoverFragment.instance()
            3 -> SettingsFragment.instance()
            else -> throw NotImplementedError("Page #$position not implemented")
        }
    }

    companion object {
        private const val NUMBER_OF_PAGES = 4
    }
}
