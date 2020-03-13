import UIKit
import SharedCode

class MyShowsViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    
    private var model: MyShowsViewModel = MyShowsViewModel.Companion().EMPTY
    
    private let presenter = MyShowsPresenter(repository: AppDelegate.instance().myShowsRepository)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.setNavigationBarHidden(true, animated: false)
        tableView.register(MyShowsHeaderView.nib, forHeaderFooterViewReuseIdentifier: MyShowsHeaderView.reuseIdentifier)
        
        presenter.attachView(view: self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
}

// MARK: - MyShowsView implementation
extension MyShowsViewController: MyShowsView {
    
    func updateShows(model: MyShowsViewModel) {
        self.model = model
        tableView.reloadData()
    }
    
    func openMyShowDetails(show: MyShowsListItem.ShowViewModel) {
        let vc = ShowDetailsViewController.instantiate(showId: show.id.int, showName: show.name)
        navigationController?.pushViewController(vc, animated: true)
    }
}

extension MyShowsViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        let number = (model.upcomingShows.isEmpty ? 0 : 1)
            + (model.toBeAnnouncedShows.isEmpty ? 0 : 1)
            + (model.endedShows.isEmpty ? 0 : 1)
        return number
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case upcomingSectionIndex():
            return model.isUpcomingExpanded ? model.upcomingShows.count : 0
        case toBeAnnouncedSectionIndex():
            return model.isToBeAnnouncedExpanded ? model.toBeAnnouncedShows.count : 0
        case endedSectionIndex():
            return model.isEndedExpanded ? model.endedShows.count : 0
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = tableView.dequeueReusableHeaderFooterView(withIdentifier: MyShowsHeaderView.reuseIdentifier) as? MyShowsHeaderView
        
        if view == nil {
            return nil
        }
        
        
        let header = view!
        
        
        switch section {
        case upcomingSectionIndex():
            header.title = "Upcoming"
            header.isExpanded = model.isUpcomingExpanded
            header.tapCallback = {
                self.model.toggleUpcomingExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: section), with: .automatic)
            }
        case toBeAnnouncedSectionIndex():
            header.title = "To Be Announced"
            header.isExpanded = model.isToBeAnnouncedExpanded
            header.tapCallback = {
                self.model.toggleToBeAnnouncedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: section), with: .automatic)
            }
        case endedSectionIndex():
            header.title = "Ended"
            header.isExpanded = model.isEndedExpanded
            header.tapCallback = {
                self.model.toggleEndedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: section), with: .automatic)
            }
        default:
            break
        }
        
        return header
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case upcomingSectionIndex():
            return upcomingShowCell(tableView, indexPath)
        case toBeAnnouncedSectionIndex():
            return toBeAnnouncedShowCell(tableView, indexPath)
        case endedSectionIndex():
            return endedShowCell(tableView, indexPath)
        default:
            fatalError("Unknown section #\(indexPath.section)")
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        let section = indexPath.section
        let row = indexPath.row
        
        var show: MyShowsListItem.ShowViewModel
        switch section {
        case upcomingSectionIndex():
            show = model.upcomingShows[row]
        case toBeAnnouncedSectionIndex():
            show = model.toBeAnnouncedShows[row]
        case endedSectionIndex():
            show = model.endedShows[row]
        default:
            fatalError("Unknown section #\(indexPath.section)")
        }
        presenter.onShowClicked(show: show)
    }
    
    private func upcomingSectionIndex() -> Int {
        return model.upcomingShows.isEmpty ? -1 : 0
    }
    
    private func toBeAnnouncedSectionIndex() -> Int {
        return model.toBeAnnouncedShows.isEmpty ? -1 : (1 - (model.upcomingShows.isEmpty ? 1 : 0))
    }
    
    private func endedSectionIndex() -> Int {
        return model.endedShows.isEmpty ? -1 : (2 - (model.upcomingShows.isEmpty ? 1 : 0) - (model.toBeAnnouncedShows.isEmpty ? 1 : 0))
    }
    
    private func upcomingShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UpcomingShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "upcoming_show_cell") as! UpcomingShowCell
        cell.bind(show: model.upcomingShows[indexPath.row])
        return cell
    }
    
    private func toBeAnnouncedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.bind(show: model.toBeAnnouncedShows[indexPath.row])
        return cell
    }
    
    private func endedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.bind(show: model.endedShows[indexPath.row])
        return cell
    }
}
