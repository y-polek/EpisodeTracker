package dev.polek.episodetracker.showdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.polek.episodetracker.R
import dev.polek.episodetracker.databinding.ShowDetailsMenuDialogBinding
import dev.polek.episodetracker.utils.DebouncingOnClickListener

class ShowDetailsMenuDialog : BottomSheetDialogFragment() {

    private val listener: Listener?
        get() = activity as? Listener

    private val clickListener = object : DebouncingOnClickListener() {
        override fun doClick(view: View) {
            when (view.id) {
                R.id.menu_share -> listener?.onShareMenuClicked()
                R.id.menu_mark_watched -> listener?.onMarkWatchedMenuClicked()
                R.id.menu_archive -> listener?.onArchiveMenuClicked()
                R.id.menu_unarchive -> listener?.onUnarchiveMenuClicked()
                R.id.menu_add -> listener?.onAddMenuClicked()
                R.id.menu_remove -> listener?.onRemoveMenuClicked()
            }
            view.postDelayed(::dismiss, 200)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        val binding = ShowDetailsMenuDialogBinding.inflate(inflater, container, false)

        binding.menuShare.setOnClickListener(clickListener)
        binding.menuMarkWatched.setOnClickListener(clickListener)
        binding.menuArchive.setOnClickListener(clickListener)
        binding.menuUnarchive.setOnClickListener(clickListener)
        binding.menuAdd.setOnClickListener(clickListener)
        binding.menuRemove.setOnClickListener(clickListener)

        val isInMyShows = requireNotNull(arguments?.getBoolean(KEY_IS_IN_MY_SHOWS))
        val isArchived = requireNotNull(arguments?.getBoolean(KEY_IS_ARCHIVED))

        binding.menuArchive.isVisible = isInMyShows && !isArchived
        binding.menuUnarchive.isVisible = isInMyShows && isArchived
        binding.menuAdd.isVisible = !isInMyShows
        binding.menuRemove.isVisible = isInMyShows

        return binding.root
    }

    companion object {

        private const val KEY_IS_IN_MY_SHOWS = "key_is_in_my_shows"
        private const val KEY_IS_ARCHIVED = "key_is_archived"

        fun instance(isInMyShows: Boolean, isArchived: Boolean): ShowDetailsMenuDialog {
            val dialog = ShowDetailsMenuDialog()
            dialog.arguments = Bundle().apply {
                putBoolean(KEY_IS_IN_MY_SHOWS, isInMyShows)
                putBoolean(KEY_IS_ARCHIVED, isArchived)
            }
            return dialog
        }
    }

    interface Listener {
        fun onShareMenuClicked()
        fun onMarkWatchedMenuClicked()
        fun onArchiveMenuClicked()
        fun onUnarchiveMenuClicked()
        fun onAddMenuClicked()
        fun onRemoveMenuClicked()
    }
}
