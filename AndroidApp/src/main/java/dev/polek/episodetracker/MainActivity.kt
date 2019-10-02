package dev.polek.episodetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.polek.episodetracker.databinding.MainActivityBinding
import dev.polek.episodetracker.discover.DiscoverFragment
import dev.polek.episodetracker.myshows.MyShowsFragment
import dev.polek.episodetracker.settings.SettingsFragment
import dev.polek.episodetracker.towatch.ToWatchFragment

class MainActivity : AppCompatActivity() {

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
            binding.pager.setCurrentItem(pagePosition, false)
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onDestroy() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(null)
        super.onDestroy()
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
