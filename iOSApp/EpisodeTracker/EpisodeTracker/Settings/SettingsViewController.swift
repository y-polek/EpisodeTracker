import UIKit
import SharedCode

class SettingsViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    
    private var presenter: SettingsPresenter!
    private var appearance: Appearance = .automatic
    private var showLastWeekSection: Bool = false
    private var showSpecials: Bool = false
    private var showSpecialsInToWatch: Bool = false
    private var showSpecialsInToWatchEnabled: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.navigationBar.topItem?.title = "Settings"
        
        presenter = SettingsPresenter(prefs: AppDelegate.instance().preferences)
        presenter.attachView(view: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
}

// MARK: - SettingsView implementation
extension SettingsViewController: SettingsView {
    
    func setAppearance(appearance: Appearance) {
        self.appearance = appearance
        tableView.reloadSections(IndexSet(arrayLiteral: PreferenceSection.appearance.index), with: .automatic)
        
        if #available(iOS 13.0, *) {
            AppDelegate.instance().setAppearance(appearance)
        }
    }
    
    func setShowLastWeekSection(showLastWeekSection: Bool) {
        self.showLastWeekSection = showLastWeekSection
        let indexPath = IndexPath(row: MyShowsOption.showLastWeekSection.rawValue, section: PreferenceSection.myShows.index)
        tableView.reloadRows(at: [indexPath], with: .automatic)
    }
    
    func setShowSpecials(showSpecials: Bool) {
        self.showSpecials = showSpecials
        let indexPath = IndexPath(row: SpecialsOption.showSpecials.rawValue, section: PreferenceSection.specials.index)
        tableView.reloadRows(at: [indexPath], with: .automatic)
    }
    
    func setShowSpecialsInToWatch(showSpecialsInToWatch: Bool, isEnabled: Bool) {
        self.showSpecialsInToWatch = showSpecialsInToWatch
        self.showSpecialsInToWatchEnabled = isEnabled
        let indexPath = IndexPath(row: SpecialsOption.showSpecialsInToWatch.rawValue, section: PreferenceSection.specials.index)
        tableView.reloadRows(at: [indexPath], with: .automatic)
    }
}

// MARK: - UITableView DataSource and Delegate
extension SettingsViewController: UITableViewDataSource, UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return PreferenceSection.allSections.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch PreferenceSection.atIndex(section) {
        case .appearance: return AppearanceOption.allCases.count
        case .myShows: return MyShowsOption.allCases.count
        case .specials: return SpecialsOption.allCases.count
        }
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return PreferenceSection.atIndex(section).description
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let section = PreferenceSection.atIndex(indexPath.section)
        
        switch section {
        case .appearance:
            return appearanceCell(tableView, indexPath)
        case .myShows:
            return myShowsCell(tableView, indexPath)
        case .specials:
            return specialsCell(tableView, indexPath)
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        let section = PreferenceSection.atIndex(indexPath.section)
        
        switch section {
        case .appearance:
            if let option = AppearanceOption(rawValue: indexPath.row) {
                presenter.onAppearanceOptionClicked(appearance: option.appearance)
            }
        default:
            break
        }
    }
    
    private func appearanceCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UITableViewCell {
        let option = AppearanceOption(rawValue: indexPath.row)
        let cell = tableView.dequeueReusableCell(withIdentifier: "multichoice_cell", for: indexPath) as! MultichoicePrefereenceCell
        cell.nameLabel.text = option?.description
        cell.isChecked = appearance == option?.appearance
        return cell
    }
    
    private func myShowsCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "switch_cell", for: indexPath) as! SwitchPreferenceCell
        cell.selectionStyle = .none
        
        let option = MyShowsOption(rawValue: indexPath.row)
        cell.nameLabel.text = option?.description
        switch option {
        case .showLastWeekSection:
            cell.checkbox.isOn = showLastWeekSection
            cell.switchCallback = { [weak self] isOn in
                self?.presenter.onShowLastWeekSectionChanged(isChecked: isOn)
            }
        case .none:
            break
        }
        
        return cell
    }
    
    private func specialsCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "switch_cell", for: indexPath) as! SwitchPreferenceCell
        cell.selectionStyle = .none
        
        let option = SpecialsOption(rawValue: indexPath.row)
        cell.nameLabel.text = option?.description
        switch option {
        case .showSpecials:
            cell.checkbox.isOn = showSpecials
            cell.isEnabled = true
            cell.switchCallback = { [weak self] isOn in
                self?.presenter.onShowSpecialsChanged(isChecked: isOn)
            }
        case .showSpecialsInToWatch:
            cell.checkbox.isOn = showSpecialsInToWatch
            cell.isEnabled = showSpecialsInToWatchEnabled
            cell.switchCallback = { [weak self] isOn in
                self?.presenter.onShowSpecialsInToWatchChanged(isChecked: isOn)
            }
        case .none:
            break
        }
        
        return cell
    }
}
