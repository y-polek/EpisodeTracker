package dev.polek.episodetracker.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.polek.episodetracker.App
import dev.polek.episodetracker.R
import dev.polek.episodetracker.common.model.Appearance
import dev.polek.episodetracker.common.presentation.settings.SettingsView
import dev.polek.episodetracker.databinding.SettingsFragmentBinding
import dev.polek.episodetracker.utils.doOnClick

class SettingsFragment : Fragment(), SettingsView {

    private val presenter = App.instance.di.settingsPresenter()

    private lateinit var binding: SettingsFragmentBinding
    private var currentTheme = Theme.SYSTEM_DEFAULT
    private val themeNames: Array<String> by lazy {
        Theme.values().map { context!!.getString(it.nameRes) }.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        binding = SettingsFragmentBinding.inflate(inflater)

        binding.themeLayout.doOnClick {
            openThemesDialog()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAppeared()
    }

    override fun onPause() {
        presenter.onViewDisappeared()
        super.onPause()
    }

    override fun setAppearance(appearance: Appearance) {
        currentTheme = Theme.byAppearance(appearance)
        binding.themeSubtitle.setText(currentTheme.nameRes)
    }

    override fun setShowLastWeekSection(showLastWeekSection: Boolean) {
        // TODO("not implemented")
    }

    override fun setShowToWatchBadge(showBadge: Boolean) {
        // TODO("not implemented")
    }

    override fun setShowSpecials(showSpecials: Boolean) {
        // TODO("not implemented")
    }

    override fun setShowSpecialsInToWatch(showSpecialsInToWatch: Boolean, isEnabled: Boolean) {
        // TODO("not implemented")
    }

    private fun openThemesDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.prefs_theme)
            .setSingleChoiceItems(themeNames, currentTheme.ordinal) { dialog, which ->
                val appearance = Theme.values()[which].appearance
                presenter.onAppearanceOptionClicked(appearance)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.action_cancel, null)
            .show()
    }

    companion object {

        fun instance() = SettingsFragment()
    }
}
