import UIKit
import SharedCode

class SettingsViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    
    private var presenter: SettingsPresenter!
    private var appearance: Appearance = .automatic
    
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
        tableView.reloadData()
        
        if #available(iOS 13.0, *) {
            AppDelegate.instance().setAppearance(appearance)
        }
    }
}

// MARK: - UITableView DataSource and Delegate
extension SettingsViewController: UITableViewDataSource, UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return PreferenceSection.allCases.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch PreferenceSection(rawValue: section) {
        case .appearance: return 3
        case .myShows: return 1
        case .specials: return 2
        case .none: return 0
        }
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return PreferenceSection(rawValue: section)?.description
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let section = PreferenceSection(rawValue: indexPath.section)
        
        switch section {
        case .appearance:
            return appearanceCell(tableView, indexPath)
        case .myShows:
            return myShowsCell(tableView, indexPath)
        case .specials:
            return specialsCell(tableView, indexPath)
        default:
            return tableView.dequeueReusableCell(withIdentifier: "multichoice_cell", for: indexPath) as! MultichoicePrefereenceCell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch PreferenceSection(rawValue: indexPath.section) {
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "switch_cell", for: indexPath)
        cell.selectionStyle = .none
        return cell
    }
    
    private func specialsCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "switch_cell", for: indexPath)
        cell.selectionStyle = .none
        return cell
    }
}
