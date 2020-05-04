import UIKit
import IGListDiffKit
import SwipeCellKit
import SharedCode

class MyShowsViewController: UIViewController {
    
    @IBOutlet weak var tableView: TableView!
    @IBOutlet weak var searchBar: UISearchBar!

    private var lastWeekShows = [MyShowsListItem.UpcomingShowViewModel]()
    private var upcomingShows = [MyShowsListItem.UpcomingShowViewModel]()
    private var tbaShows = [MyShowsListItem.ShowViewModel]()
    private var endedShows = [MyShowsListItem.ShowViewModel]()
    private var archivedShows = [MyShowsListItem.ShowViewModel]()
    
    private let presenter = MyShowsPresenter(
        myShowsRepository: AppDelegate.instance().myShowsRepository,
        showRepository: AppDelegate.instance().showRepository,
        prefs: AppDelegate.instance().preferences)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.setNavigationBarHidden(true, animated: false)
        tableView.register(MyShowsHeaderView.nib, forHeaderFooterViewReuseIdentifier: MyShowsHeaderView.reuseIdentifier)
        
        addHideKeyboardByTapGestureRecognizer()
        
        presenter.attachView(view: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setupRefreshControl()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
    
    @objc func onRefreshRequested() {
        presenter.onRefreshRequested()
    }
    
    private func lastWeekSectionIndex() -> Int {
        return lastWeekShows.isEmpty ? -1 : 0
    }
    
    private func upcomingSectionIndex() -> Int {
        return upcomingShows.isEmpty ? -1 : (1 - (lastWeekShows.isEmpty ? 1 : 0))
    }
    
    private func tbaSectionIndex() -> Int {
        return tbaShows.isEmpty ? -1 : (2 - (lastWeekShows.isEmpty ? 1 : 0) - (upcomingShows.isEmpty ? 1 : 0))
    }
    
    private func endedSectionIndex() -> Int {
        return endedShows.isEmpty ? -1 : (3 - (lastWeekShows.isEmpty ? 1 : 0) - (upcomingShows.isEmpty ? 1 : 0) - (tbaShows.isEmpty ? 1 : 0))
    }
    
    private func archivedSectionIndex() -> Int {
        return archivedShows.isEmpty ? -1 : (4 - (lastWeekShows.isEmpty ? 1 : 0) - (upcomingShows.isEmpty ? 1 : 0) - (tbaShows.isEmpty ? 1 : 0) - (endedShows.isEmpty ? 1 : 0))
    }
    
    private func showAt(_ indexPath: IndexPath) -> MyShowsListItem.ShowViewModel {
        let section = indexPath.section
        let row = indexPath.row
        
        switch section {
        case lastWeekSectionIndex():
            return lastWeekShows[row]
        case upcomingSectionIndex():
            return upcomingShows[row]
        case tbaSectionIndex():
            return tbaShows[row]
        case endedSectionIndex():
            return endedShows[row]
        case archivedSectionIndex():
            return archivedShows[row]
        default:
            fatalError("Unknown section #\(indexPath.section)")
        }
    }
    
    private func setupRefreshControl() {
        let isRefreshing = tableView.refreshControl?.isRefreshing ?? false
        let control = UIRefreshControl()
        control.addTarget(self, action: #selector(onRefreshRequested), for: .valueChanged)
        tableView.refreshControl = control
        if isRefreshing {
            control.beginRefreshing()
        }
    }
}

// MARK: - MyShowsView implementation
extension MyShowsViewController: MyShowsView {
    
    func displayLastWeekShows(shows: [MyShowsListItem.UpcomingShowViewModel], isVisible: Bool) {
        let oldSectionIndex = lastWeekSectionIndex()
        let oldShows = lastWeekShows
        let oldIsExpanded = presenter.isLastWeekExpanded
        
        lastWeekShows = isVisible ? shows : []
        
        updateSection(
            oldSectionIndex: oldSectionIndex,
            newSectionIndex: lastWeekSectionIndex(),
            oldShows: oldShows,
            newShows: lastWeekShows,
            oldIsExpanded: oldIsExpanded,
            newIsExpanded: presenter.isLastWeekExpanded)
    }
    
    func displayUpcomingShows(shows: [MyShowsListItem.UpcomingShowViewModel]) {
        let oldSectionIndex = upcomingSectionIndex()
        let oldShows = upcomingShows
        let oldIsExpanded = presenter.isUpcomingExpanded
        
        upcomingShows = shows
        
        updateSection(
            oldSectionIndex: oldSectionIndex,
            newSectionIndex: upcomingSectionIndex(),
            oldShows: oldShows,
            newShows: upcomingShows,
            oldIsExpanded: oldIsExpanded,
            newIsExpanded: presenter.isUpcomingExpanded)
    }
    
    func displayToBeAnnouncedShows(shows: [MyShowsListItem.ShowViewModel]) {
        let oldSectionIndex = tbaSectionIndex()
        let oldShows = tbaShows
        let oldIsExpanded = presenter.isTbaExpanded
        
        tbaShows = shows
        
        updateSection(
            oldSectionIndex: oldSectionIndex,
            newSectionIndex: tbaSectionIndex(),
            oldShows: oldShows,
            newShows: tbaShows,
            oldIsExpanded: oldIsExpanded,
            newIsExpanded: presenter.isTbaExpanded)
    }
    
    func displayEndedShows(shows: [MyShowsListItem.ShowViewModel]) {
        let oldSectionIndex = endedSectionIndex()
        let oldShows = endedShows
        let oldIsExpanded = presenter.isEndedExpanded
        
        endedShows = shows
        
        updateSection(
            oldSectionIndex: oldSectionIndex,
            newSectionIndex: endedSectionIndex(),
            oldShows: oldShows,
            newShows: endedShows,
            oldIsExpanded: oldIsExpanded,
            newIsExpanded: presenter.isEndedExpanded)
    }
    
    func displayArchivedShows(shows: [MyShowsListItem.ShowViewModel]) {
        let oldSectionIndex = archivedSectionIndex()
        let oldShows = archivedShows
        let oldIsExpanded = presenter.isArchivedExpanded
        
        archivedShows = shows
        
        updateSection(
            oldSectionIndex: oldSectionIndex,
            newSectionIndex: archivedSectionIndex(),
            oldShows: oldShows,
            newShows: archivedShows,
            oldIsExpanded: oldIsExpanded,
            newIsExpanded: presenter.isArchivedExpanded)
    }
    
    func showEmptyMessage(isFiltered: Bool) {
        if isFiltered {
            tableView.emptyText = "No shows found"
            tableView.emptyActionName = "Show All"
            tableView.isEmptyActionHidden = false
            tableView.emptyActionTappedCallback = { [weak self] in
                self?.searchBar.text = ""
                self?.searchBar.resignFirstResponder()
                self?.presenter.onSearchQueryChanged(text: "")
            }
        } else {
            tableView.emptyText = "Add some shows on \"Discover\" tab"
            tableView.emptyActionName = "Discover Shows"
            tableView.isEmptyActionHidden = false
            tableView.emptyActionTappedCallback = { [weak self] in
                self?.tabBarController?.selectedIndex = 2
            }
        }
        tableView.showEmptyView()
    }
    
    func hideEmptyMessage() {
        tableView.hideEmptyView()
    }
    
    func hideRefresh() {
        tableView.refreshControl?.endRefreshing()
    }
    
    func openMyShowDetails(show: MyShowsListItem.ShowViewModel) {
        let vc = ShowDetailsViewController.instantiate(showId: show.id.int, showName: show.name)
        navigationController?.pushViewController(vc, animated: true)
    }
    
    private func updateSection(
        oldSectionIndex: Int,
        newSectionIndex: Int,
        oldShows: [MyShowsListItem.ShowViewModel],
        newShows: [MyShowsListItem.ShowViewModel],
        oldIsExpanded: Bool,
        newIsExpanded: Bool)
    {
        let diff = ListDiffPaths(
            fromSection: oldSectionIndex,
            toSection: newSectionIndex,
            oldArray: oldIsExpanded ? oldShows : [],
            newArray: newIsExpanded ? newShows : [],
            option: .equality)
        
        let sectionDiff = SectionDiff.diff(oldIndex: oldSectionIndex, newIndex: newSectionIndex)
        
        diff.apply(tableView, deletedSections: sectionDiff.deleted, insertedSections: sectionDiff.inserted)
    }
}

// MARK: - TableView datasource
extension MyShowsViewController: UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        let number = (lastWeekShows.isEmpty ? 0 : 1)
            + (upcomingShows.isEmpty ? 0 : 1)
            + (tbaShows.isEmpty ? 0 : 1)
            + (endedShows.isEmpty ? 0 : 1)
            + (archivedShows.isEmpty ? 0 : 1)
        return number
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case lastWeekSectionIndex():
            return presenter.isLastWeekExpanded ? lastWeekShows.count : 0
        case upcomingSectionIndex():
            return presenter.isUpcomingExpanded ? upcomingShows.count : 0
        case tbaSectionIndex():
            return presenter.isTbaExpanded ? tbaShows.count : 0
        case endedSectionIndex():
            return presenter.isEndedExpanded ? endedShows.count : 0
        case archivedSectionIndex():
            return presenter.isArchivedExpanded ? archivedShows.count : 0
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: MyShowsHeaderView.reuseIdentifier) as! MyShowsHeaderView
        
        switch section {
        case lastWeekSectionIndex():
            header.title = "Last Week"
            header.isExpanded = presenter.isLastWeekExpanded
            header.tapCallback = { [weak self] in
                if let vc = self {
                    vc.presenter.isLastWeekExpanded.toggle()
                    tableView.reloadSections(IndexSet(arrayLiteral: vc.lastWeekSectionIndex()), with: .automatic)
                }
            }
        case upcomingSectionIndex():
            header.title = "Upcoming"
            header.isExpanded = presenter.isUpcomingExpanded
            header.tapCallback = { [weak self] in
                if let vc = self {
                    vc.presenter.isUpcomingExpanded.toggle()
                    tableView.reloadSections(IndexSet(arrayLiteral: vc.upcomingSectionIndex()), with: .automatic)
                }
            }
        case tbaSectionIndex():
            header.title = "To Be Announced"
            header.isExpanded = presenter.isTbaExpanded
            header.tapCallback = { [weak self] in
                if let vc = self {
                    vc.presenter.isTbaExpanded.toggle()
                    tableView.reloadSections(IndexSet(arrayLiteral: vc.tbaSectionIndex()), with: .automatic)
                }
            }
        case endedSectionIndex():
            header.title = "Ended"
            header.isExpanded = presenter.isEndedExpanded
            header.tapCallback = { [weak self] in
                if let vc = self {
                    vc.presenter.isEndedExpanded.toggle()
                    tableView.reloadSections(IndexSet(arrayLiteral: vc.endedSectionIndex()), with: .automatic)
                }
            }
        case archivedSectionIndex():
            header.title = "Archived"
            header.isExpanded = presenter.isArchivedExpanded
            header.tapCallback = { [weak self] in
                if let vc = self {
                    vc.presenter.isArchivedExpanded.toggle()
                    tableView.reloadSections(IndexSet(arrayLiteral: vc.archivedSectionIndex()), with: .automatic)
                }
            }
        default:
            break
        }
        
        return header
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case lastWeekSectionIndex():
            return lastWeekShowCell(tableView, indexPath)
        case upcomingSectionIndex():
            return upcomingShowCell(tableView, indexPath)
        case tbaSectionIndex():
            return tbaShowCell(tableView, indexPath)
        case endedSectionIndex():
            return endedShowCell(tableView, indexPath)
        case archivedSectionIndex():
            return archivedShowCell(tableView, indexPath)
        default:
            fatalError("Unknown section #\(indexPath.section)")
        }
    }
    
    private func lastWeekShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UpcomingShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "upcoming_show_cell") as! UpcomingShowCell
        cell.delegate = self
        cell.bind(show: lastWeekShows[indexPath.row])
        return cell
    }
    
    private func upcomingShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UpcomingShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "upcoming_show_cell") as! UpcomingShowCell
        cell.delegate = self
        cell.bind(show: upcomingShows[indexPath.row])
        return cell
    }
    
    private func tbaShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.delegate = self
        cell.bind(show: tbaShows[indexPath.row])
        return cell
    }
    
    private func endedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.delegate = self
        cell.bind(show: endedShows[indexPath.row])
        return cell
    }
    
    private func archivedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.delegate = self
        cell.bind(show: archivedShows[indexPath.row])
        return cell
    }
}

// MARK: - TableView delegate
extension MyShowsViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        presenter.onShowClicked(show: showAt(indexPath))
    }
}

// MARK: - SwipeTableViewCellDelegate
extension MyShowsViewController: SwipeTableViewCellDelegate {
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath, for orientation: SwipeActionsOrientation) -> [SwipeAction]? {
        guard orientation == .right else { return nil }
        
        let show = showAt(indexPath)
        
        let remove = SwipeAction(style: .destructive, title: "Remove") { [weak self] (action, indexPath) in
            self?.confirmRemoval(show: show)
        }
        remove.image = UIImage(named: "ic-remove")
        
        if indexPath.section == archivedSectionIndex() {
            let unarchive = SwipeAction(style: .default, title: "Unarchive") { [weak self] (action, indexPath) in
                self?.presenter.onUnarchiveShowClicked(show: show)
            }
            unarchive.image = UIImage(named: "ic-unarchive")
            return [unarchive, remove]
        } else {
            let archive = SwipeAction(style: .default, title: "Archive") { [weak self] (action, indexPath) in
                self?.presenter.onArchiveShowClicked(show: show)
            }
            archive.image = UIImage(named: "ic-archive")
            return [archive, remove]
        }
    }
    
    private func confirmRemoval(show: MyShowsListItem.ShowViewModel) {
        let alert = UIAlertController(title: nil, message: "Are you sure you want to remove \"\(show.name)\"?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Remove", style: .destructive, handler: { [weak self] _ in
            self?.presenter.onRemoveShowClicked(show: show)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        present(alert, animated: true, completion: nil)
    }
}

// MARK: - UISearchBarDelegate implementation
extension MyShowsViewController: UISearchBarDelegate {
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        presenter.onSearchQueryChanged(text: searchText)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
}
