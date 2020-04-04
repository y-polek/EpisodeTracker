import UIKit
import IGListDiffKit
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
        let upcomingDiff = ListDiffPaths(
            fromSection: self.model.upcomingSectionIndex(),
            toSection: model.upcomingSectionIndex(),
            oldArray: self.model.isUpcomingExpanded ? self.model.upcomingShows : [],
            newArray: model.isUpcomingExpanded ? model.upcomingShows : [],
            option: .equality)
        
        let tbaDiff = ListDiffPaths(
            fromSection: self.model.toBeAnnouncedSectionIndex(),
            toSection: model.toBeAnnouncedSectionIndex(),
            oldArray: self.model.isToBeAnnouncedExpanded ? self.model.toBeAnnouncedShows : [],
            newArray: model.isToBeAnnouncedExpanded ? model.toBeAnnouncedShows : [],
            option: .equality)
        
        let endedDiff = ListDiffPaths(
            fromSection: self.model.endedSectionIndex(),
            toSection: model.endedSectionIndex(),
            oldArray: self.model.isEndedExpanded ? self.model.endedShows : [],
            newArray: model.isEndedExpanded ? model.endedShows : [],
            option: .equality)
        
        let archivedDiff = ListDiffPaths(
            fromSection: self.model.archivedSectionIndex(),
            toSection: model.archivedSectionIndex(),
            oldArray: self.model.isArchivedExpanded ? self.model.archivedShows : [],
            newArray: model.isArchivedExpanded ? model.archivedShows : [],
            option: .equality)
        
        var deletedSections = [Int]()
        var insertedSections = [Int]()
        
        let oldUpcomingIdx = self.model.upcomingSectionIndex()
        let newUpcomingIdx = model.upcomingSectionIndex()
        if oldUpcomingIdx >= 0 && newUpcomingIdx < 0 {
            deletedSections.append(oldUpcomingIdx)
        }
        if oldUpcomingIdx < 0 && newUpcomingIdx >= 0 {
            insertedSections.append(newUpcomingIdx)
        }
        
        let oldTbaIdx = self.model.toBeAnnouncedSectionIndex()
        let newTbaIdx = model.toBeAnnouncedSectionIndex()
        if oldTbaIdx >= 0 && newTbaIdx < 0 {
            deletedSections.append(oldTbaIdx)
        }
        if oldTbaIdx < 0 && newTbaIdx >= 0 {
            insertedSections.append(newTbaIdx)
        }
        
        let oldEndedIdx = self.model.endedSectionIndex()
        let newEndedIdx = model.endedSectionIndex()
        if oldEndedIdx >= 0 && newEndedIdx < 0 {
            deletedSections.append(oldEndedIdx)
        }
        if oldEndedIdx < 0 && newEndedIdx >= 0 {
            insertedSections.append(newEndedIdx)
        }
        
        let oldArchivedIdx = self.model.archivedSectionIndex()
        let newArchivedIdx = model.archivedSectionIndex()
        if oldArchivedIdx >= 0 && newArchivedIdx < 0 {
            deletedSections.append(oldArchivedIdx)
        }
        if oldArchivedIdx < 0 && newArchivedIdx >= 0 {
            insertedSections.append(newArchivedIdx)
        }
        
        self.model = model
        
        tableView.beginUpdates()
        tableView.deleteSections(IndexSet(deletedSections), with: .fade)
        tableView.insertSections(IndexSet(insertedSections), with: .fade)
        tableView.deleteRows(at: upcomingDiff.deletes + tbaDiff.deletes + endedDiff.deletes + archivedDiff.deletes, with: .fade)
        tableView.insertRows(at: upcomingDiff.inserts + tbaDiff.inserts + endedDiff.inserts + archivedDiff.inserts, with: .automatic)
        tableView.reloadRows(at: upcomingDiff.updates + tbaDiff.updates + endedDiff.updates + archivedDiff.updates, with: .automatic)
        tableView.endUpdates()
    }
    
    func openMyShowDetails(show: MyShowsListItem.ShowViewModel) {
        let vc = ShowDetailsViewController.instantiate(showId: show.id.int, showName: show.name)
        navigationController?.pushViewController(vc, animated: true)
    }
}

// MARK: - TableView datasource and delegate
extension MyShowsViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        let number = (model.upcomingShows.isEmpty ? 0 : 1)
            + (model.toBeAnnouncedShows.isEmpty ? 0 : 1)
            + (model.endedShows.isEmpty ? 0 : 1)
            + (model.archivedShows.isEmpty ? 0 : 1)
        return number
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case model.upcomingSectionIndex():
            return model.isUpcomingExpanded ? model.upcomingShows.count : 0
        case model.toBeAnnouncedSectionIndex():
            return model.isToBeAnnouncedExpanded ? model.toBeAnnouncedShows.count : 0
        case model.endedSectionIndex():
            return model.isEndedExpanded ? model.endedShows.count : 0
        case model.archivedSectionIndex():
            return model.isArchivedExpanded ? model.archivedShows.count : 0
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: MyShowsHeaderView.reuseIdentifier) as! MyShowsHeaderView
        
        switch section {
        case model.upcomingSectionIndex():
            header.title = "Upcoming"
            header.isExpanded = model.isUpcomingExpanded
            header.tapCallback = {
                self.model.toggleUpcomingExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: self.model.upcomingSectionIndex()), with: .automatic)
            }
        case model.toBeAnnouncedSectionIndex():
            header.title = "To Be Announced"
            header.isExpanded = model.isToBeAnnouncedExpanded
            header.tapCallback = {
                self.model.toggleToBeAnnouncedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: self.model.toBeAnnouncedSectionIndex()), with: .automatic)
            }
        case model.endedSectionIndex():
            header.title = "Ended"
            header.isExpanded = model.isEndedExpanded
            header.tapCallback = {
                self.model.toggleEndedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: self.model.endedSectionIndex()), with: .automatic)
            }
        case model.archivedSectionIndex():
            header.title = "Archived"
            header.isExpanded = model.isArchivedExpanded
            header.tapCallback = {
                self.model.toggleArchivedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: self.model.archivedSectionIndex()), with: .automatic)
            }
        default:
            break
        }
        
        return header
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case model.upcomingSectionIndex():
            return upcomingShowCell(tableView, indexPath)
        case model.toBeAnnouncedSectionIndex():
            return toBeAnnouncedShowCell(tableView, indexPath)
        case model.endedSectionIndex():
            return endedShowCell(tableView, indexPath)
        case model.archivedSectionIndex():
            return archivedShowCell(tableView, indexPath)
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
        case model.upcomingSectionIndex():
            show = model.upcomingShows[row]
        case model.toBeAnnouncedSectionIndex():
            show = model.toBeAnnouncedShows[row]
        case model.endedSectionIndex():
            show = model.endedShows[row]
        case model.archivedSectionIndex():
            show = model.archivedShows[row]
        default:
            fatalError("Unknown section #\(indexPath.section)")
        }
        presenter.onShowClicked(show: show)
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
    
    private func archivedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.bind(show: model.archivedShows[indexPath.row])
        return cell
    }
}

extension MyShowsViewModel {
    
    func upcomingSectionIndex() -> Int {
        return upcomingShows.isEmpty ? -1 : 0
    }
    
    func toBeAnnouncedSectionIndex() -> Int {
        return toBeAnnouncedShows.isEmpty ? -1 : (1 - (upcomingShows.isEmpty ? 1 : 0))
    }
    
    func endedSectionIndex() -> Int {
        return endedShows.isEmpty ? -1 : (2 - (upcomingShows.isEmpty ? 1 : 0) - (toBeAnnouncedShows.isEmpty ? 1 : 0))
    }
    
    func archivedSectionIndex() -> Int {
        return archivedShows.isEmpty ? -1 : (3 - (upcomingShows.isEmpty ? 1 : 0) - (toBeAnnouncedShows.isEmpty ? 1 : 0) - (endedShows.isEmpty ? 1 : 0))
    }
}
