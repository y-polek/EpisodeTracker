import UIKit
import IGListKit
import SwipeCellKit
import SharedCode

class ToWatchViewController: UIViewController {

    @IBOutlet weak var tableView: TableView!
    @IBOutlet weak var searchBar: UISearchBar!
    
    private let presenter = ToWatchPresenter(
        toWatchRepository: AppDelegate.instance().toWatchRepository,
        episodesRepository: AppDelegate.instance().episodesRepository,
        myShowsRepository: AppDelegate.instance().myShowsRepository)
    private var shows: [ToWatchShowViewModel]? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.setNavigationBarHidden(true, animated: false)
        
        addHideKeyboardByTapGestureRecognizer()
        
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

// MARK: - ToWatchView implementation
extension ToWatchViewController: ToWatchView {
    
    func displayShows(shows: [ToWatchShowViewModel]) {
        if self.shows == nil {
            self.shows = shows
            tableView.reloadData()
        } else {
            let diff = ListDiffPaths(fromSection: 0, toSection: 0, oldArray: self.shows, newArray: shows, option: .equality)
            self.shows = shows
            diff.apply(tableView, deletedSections: [], insertedSections: [])
        }
    }
    
    func showEmptyMessage(isFiltered: Bool) {
        if isFiltered {
            tableView.emptyText = "No shows found"
            tableView.emptyActionName = "Show All"
            tableView.isEmptyActionHidden = false
            tableView.emptyActionTappedCallback = { [weak self] in
                self?.cancelSearch()
            }
        } else {
            tableView.emptyText = "No episodes to watch"
            tableView.isEmptyActionHidden = true
        }
        tableView.showEmptyView()
    }
    
    func hideEmptyMessage() {
        tableView.hideEmptyView()
    }
    
    func openToWatchShowDetails(show: ToWatchShowViewModel, episode: EpisodeNumber) {
        let vc = ShowDetailsViewController.instantiate(
            showId: show.id.int,
            showName: show.name,
            openEpisodesTabOnStart: true,
            scrollToEpisodeOnStart: episode)
        navigationController?.pushViewController(vc, animated: true)
    }
    
    private func cancelSearch() {
        searchBar.text = ""
        searchBar.resignFirstResponder()
        searchBar.setShowsCancelButton(false, animated: true)
        presenter.onSearchQueryChanged(text: "")
    }
}

// MARK: - UITableView datasource
extension ToWatchViewController: UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return shows?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "to_watch_show_cell", for: indexPath) as! ToWatchCell
        cell.delegate = self
        let show = shows![indexPath.row]
        cell.bind(show)
        cell.checkButton.tapCallback = { [weak self] in
            self?.presenter.onWatchedButtonClicked(show: show)
        }
        return cell
    }
}

// MARK: - UITableView delegate
extension ToWatchViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        presenter.onShowClicked(show: shows![indexPath.row])
    }
}

// MARK: - SwipeTableViewCellDelegate
extension ToWatchViewController: SwipeTableViewCellDelegate {
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath, for orientation: SwipeActionsOrientation) -> [SwipeAction]? {
        let show = shows![indexPath.row]
        
        switch orientation {
        case .right:
            let markWatched = SwipeAction(style: .default, title: "Mark All Watched") { [weak self] (action, indexPath) in
                self?.presenter.onMarkAllWatchedClicked(show: show)
            }
            markWatched.image = UIImage(named: "ic-check-all")
            markWatched.backgroundColor = .darkGray
            markWatched.textColor = .textColorPrimaryInverse
            return [markWatched]
        case .left:
            let archive = SwipeAction(style: .default, title: "Archive") { [weak self] (action, indexPath) in
                self?.presenter.onArchiveShowClicked(show: show)
            }
            archive.image = UIImage(named: "ic-archive")
            archive.backgroundColor = .darkGray
            archive.textColor = .textColorPrimaryInverse
            return [archive]
        }
    }
}

// MARK: - UISearchBarDelegate implementation
extension ToWatchViewController: UISearchBarDelegate {
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchBar.setShowsCancelButton(true, animated: true)
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        presenter.onSearchQueryChanged(text: searchText)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        cancelSearch()
    }
}

// MARK: - Scrollable implementation
extension ToWatchViewController: Scrollable {
    
    func scrollToTop() {
        tableView.scrollToTop(animated: true)
    }
}
