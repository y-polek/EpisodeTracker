import UIKit
import SharedCode

class EpisodesViewController: UIViewController {
    
    var showId: Int!
    var presenter: EpisodesPresenter!
    var seasons = [SeasonViewModel]()
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter = EpisodesPresenter(showId: Int32(showId), repository: AppDelegate.instance().episodesRepository)
        presenter.attachView(view: self)
    }
}

extension EpisodesViewController: EpisodesView {
    
    func displaySeasons(seasons: [SeasonViewModel]) {
        self.seasons = seasons
    }
}

extension EpisodesViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return seasons.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let season = seasons[section]
        return season.isExpanded ? season.episodes.count : 0
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let header = SeasonHeaderView()
        
        let season = seasons[section]
        header.title = "Season \(season.number)"
        header.episodeCount = season.watchedEpisodes
        header.isExpanded = season.isExpanded
        header.tapCallback = {
            season.isExpanded = !season.isExpanded
            tableView.reloadSections(IndexSet(arrayLiteral: section), with: .automatic)
        }
        
        return header
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueReusableCell(withIdentifier: "episode_cell", for: indexPath)
    }
    
    
}
