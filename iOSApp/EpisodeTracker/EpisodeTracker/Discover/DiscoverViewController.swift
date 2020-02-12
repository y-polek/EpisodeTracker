import UIKit
import SharedCode

class DiscoverViewController: UIViewController {
    
    private var results: [DiscoverResultViewModel] = []
    
    @IBOutlet weak var tableView: TableView!
    
    private let presenter = DiscoverPresenter(
        discoverRepository: AppDelegate.instance().discoverRepository,
        myShowsRepository: AppDelegate.instance().myShowsRepository)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.setNavigationBarHidden(true, animated: false)
        presenter.attachView(view: self)
    }
}

extension DiscoverViewController: DiscoverView {
    
    func showPrompt() {
        tableView.showPromptView()
    }
    
    func hidePrompt() {
        tableView.hidePromptView()
    }
    
    func showProgress() {
        tableView.showProgress()
    }
    
    func hideProgress() {
        tableView.hideProgress()
    }
    
    func showSearchResults(results: [DiscoverResultViewModel]) {
        self.results = results
        tableView.reloadData()
        tableView.scrollToTop()
    }
    
    func updateSearchResult(result: DiscoverResultViewModel) {
        if let row = self.results.firstIndex(where: { $0.id == result.id }) {
            self.results[row] = result
            tableView.reloadRows(at: [IndexPath(row: row, section: 0)], with: .none)
        }
    }
    
    func showEmptyMessage() {
        tableView.showEmptyView()
    }
    
    func hideEmptyMessage() {
        tableView.hideEmptyView()
    }
    
    func openDiscoverShow(showId: Int32) {
        let vc = ShowDetailsViewController.instantiate(showId: Int(showId), openEpisodesTabOnStart: false)
        navigationController?.pushViewController(vc, animated: true)
    }
}

extension DiscoverViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let row = indexPath.row
        let count = tableView.numberOfRows(inSection: indexPath.section)
        let result = results[row]
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "discover_cell", for: indexPath) as! DiscoverResultCell
        cell.bind(result: result)
        cell.divider.isHidden = row == (count - 1)
        cell.imageButton.isActivityIndicatorHidden = !result.isAddInProgress
        cell.imageButton.tapCallback = {
            if result.isInMyShows {
                self.presenter.onRemoveButtonClicked(show: result)
            } else {
                self.presenter.onAddButtonClicked(show: result)
            }
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        presenter.onShowClicked(show: results[indexPath.row])
    }
}

extension DiscoverViewController: UISearchBarDelegate {
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        presenter.onSearchQuerySubmitted(query: searchBar.text ?? "")
        searchBar.resignFirstResponder()
    }
}
