import UIKit
import SharedCode

class ToWatchViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    
    private let presenter = ToWatchPresenter()
    private var shows = [ToWatchShowViewModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.attachView(view: self)
    }
}

extension ToWatchViewController: ToWatchView {
    func displayShows(shows: [ToWatchShowViewModel]) {
        self.shows = shows
        tableView.reloadData()
    }
}

extension ToWatchViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return shows.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "to_watch_show_cell") as! ToWatchCell
        cell.bind(shows[indexPath.row])
        return cell
    }
    
    
}
