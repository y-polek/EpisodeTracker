import UIKit
import SharedCode

class ToWatchViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    
    private let presenter = ToWatchPresenter(repository: AppDelegate.instance().toWatchRepository)
    private var shows = [ToWatchShowViewModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        
        switch segue.identifier {
        case "show_details":
            guard let showId = sender as? Int else {
                fatalError("'sender' must be of type Int for 'show_details' segue")
            }
            segue.setShowDetailsParameters(showId, openEpisodesTabOnStart: true)
        default:
            break
        }
    }
}

extension ToWatchViewController: ToWatchView {
    
    func displayShows(shows: [ToWatchShowViewModel]) {
        self.shows = shows
        tableView.reloadData()
    }
    
    func updateShow(show: ToWatchShowViewModel) {
        if let row = self.shows.firstIndex(where: { $0.id == show.id }) {
            self.shows[row] = show
            tableView.reloadRows(at: [IndexPath(row: row, section: 0)], with: .none)
        }
    }
    
    func removeShow(show: ToWatchShowViewModel) {
        if let row = self.shows.firstIndex(where: { $0.id == show.id }) {
            self.shows.remove(at: row)
            tableView.deleteRows(at: [IndexPath(row: row, section: 0)], with: .none)
        }
    }
    
    func openToWatchShowDetails(showId: Int32) {
        performSegue(withIdentifier: "show_details", sender: Int(showId))
    }
}

extension ToWatchViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return shows.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "to_watch_show_cell") as! ToWatchCell
        let show = shows[indexPath.row]
        cell.bind(show)
        cell.checkButton.tapCallback = {
            self.presenter.onWatchedButtonClicked(show: show)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        let show = shows[indexPath.row]
        presenter.onShowClicked(show: show)
    }
}
