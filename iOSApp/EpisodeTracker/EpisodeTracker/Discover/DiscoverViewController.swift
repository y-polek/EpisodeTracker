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
        tableView.promptText = string(R.str.discover_prompt_message)
        tableView.emptyText = string(R.str.discover_empty_message)
        tableView.errorText = string(R.str.discover_error_message)
        presenter.attachView(view: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.onViewAppeared()
        
        tableView.retryTappedCallback = {
            self.presenter.onRetryButtonClicked()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
        
        tableView.retryTappedCallback = nil
    }
}

// MARK: - DiscoverView implementation
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
    
    func updateSearchResults() {
        tableView.reloadData()
    }
    
    func showEmptyMessage() {
        tableView.showEmptyView()
    }
    
    func hideEmptyMessage() {
        tableView.hideEmptyView()
    }
    
    func showError() {
        tableView.showErrorView()
    }
    
    func hideError() {
        tableView.hideErrorView()
    }
    
    func displayRemoveShowConfirmation(result: DiscoverResultViewModel, callback: @escaping (KotlinBoolean) -> Void) {
        let alert = UIAlertController(title: nil, message: "Are you sure you want to remove \"\(result.name)\"?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Remove", style: .destructive, handler: { _ in callback(true) }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { _ in callback(false) }))
        present(alert, animated: true, completion: nil)
    }
    
    func openDiscoverShow(show: DiscoverResultViewModel) {
        let vc = ShowDetailsViewController.instantiate(showId: show.id.int, showName: show.name)
        navigationController?.pushViewController(vc, animated: true)
    }
}

// MARK: - UITableView delegate and datasource
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
        cell.imageButton.tapCallback = { [weak self] in
            if result.isInMyShows {
                self?.presenter.onRemoveButtonClicked(show: result)
            } else {
                self?.presenter.onAddButtonClicked(show: result)
            }
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        presenter.onShowClicked(show: results[indexPath.row])
    }
}

// MARK: - UISearchBar delegate
extension DiscoverViewController: UISearchBarDelegate {
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        presenter.onSearchQuerySubmitted(query: searchBar.text ?? "")
        searchBar.resignFirstResponder()
    }
}

// MARK: - Scrollable implementation
extension DiscoverViewController: Scrollable {
    
    func scrollToTop() {
        tableView.scrollToTop(animated: true)
    }
}
