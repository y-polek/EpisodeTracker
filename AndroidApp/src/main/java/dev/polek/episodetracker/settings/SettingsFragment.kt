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
import dev.polek.episodetracker.utils.apply
import dev.polek.episodetracker.utils.doOnClick
import dev.polek.episodetracker.utils.openUrl

class SettingsFragment : Fragment(), SettingsView {

    private val presenter = App.instance.di.settingsPresenter()

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private var currentTheme = Theme.SYSTEM_DEFAULT
    private val themeNames: Array<String> by lazy {
        Theme.values().map { context!!.getString(it.nameRes) }.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        _binding = SettingsFragmentBinding.inflate(inflater)

        binding.themeLayout.doOnClick {
            openThemesDialog()
        }

        binding.showLastWeekCheckbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onShowLastWeekSectionChanged(isChecked)
        }

        binding.showBadgeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onShowToWatchBadgeChanged(isChecked)
        }

        binding.showSpecialsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onShowSpecialsChanged(isChecked)
        }

        binding.showSpecialsInToWatchCheckbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onShowSpecialsInToWatchChanged(isChecked)
        }

        binding.tmdbLogo.doOnClick {
            context?.openUrl(getString(R.string.prefs_tmdb_url))
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
        _binding = null
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
        appearance.apply()
        currentTheme = Theme.byAppearance(appearance)
        binding.themeSubtitle.setText(currentTheme.nameRes)
    }

    override fun setShowLastWeekSection(showLastWeekSection: Boolean) {
        binding.showLastWeekCheckbox.isChecked = showLastWeekSection
    }

    override fun setShowToWatchBadge(showBadge: Boolean) {
        binding.showBadgeCheckbox.isChecked = showBadge
    }

    override fun setShowSpecials(showSpecials: Boolean) {
        binding.showSpecialsCheckbox.isChecked = showSpecials
    }

    override fun setShowSpecialsInToWatch(showSpecialsInToWatch: Boolean, isEnabled: Boolean) {
        binding.showSpecialsInToWatchCheckbox.isChecked = showSpecialsInToWatch
        binding.showSpecialsInToWatchCheckbox.isEnabled = isEnabled
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
