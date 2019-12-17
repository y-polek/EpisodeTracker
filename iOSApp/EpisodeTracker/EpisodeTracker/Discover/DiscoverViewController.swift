import UIKit
import SharedCode

class DiscoverViewController: UIViewController {
    
    private var results: [DiscoverResultViewModel] = []
    
    @IBOutlet weak var tableView: TableView!
    
    private let presenter = DiscoverPresenter(repository: DiscoverRepository(tmdbService: TmdbService()))
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.attachView(view: self)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.detachView()
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
    }
    
    func showEmptyMessage() {
        tableView.showEmptyView()
    }
    
    func hideEmptyMessage() {
        tableView.hideEmptyView()
    }
}

extension DiscoverViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let row = indexPath.row
        let count = tableView.numberOfRows(inSection: indexPath.section)
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "discover_cell", for: indexPath) as! DiscoverResultCell
        cell.bind(result: results[row])
        cell.divider.isHidden = row == (count - 1)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

extension DiscoverViewController: UISearchBarDelegate {
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        presenter.onSearchQuerySubmitted(query: searchBar.text ?? "")
        searchBar.resignFirstResponder()
    }
}
