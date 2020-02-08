import UIKit
import SharedCode

class EpisodesViewController: UIViewController {
    
    var showId: Int!
    var presenter: EpisodesPresenter!
    var seasons = [SeasonViewModel]()
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.register(SeasonHeaderView.nib, forHeaderFooterViewReuseIdentifier: SeasonHeaderView.reuseIdentifier)
        
        presenter = EpisodesPresenter(showId: Int32(showId), repository: AppDelegate.instance().episodesRepository)
        presenter.attachView(view: self)
    }
}

extension EpisodesViewController: EpisodesView {
    
    func displaySeasons(seasons: [SeasonViewModel]) {
        self.seasons = seasons
    }
    
    func showCheckAllPreviousEpisodesPrompt(callback: @escaping (KotlinBoolean) -> Void) {
        let alert = UIAlertController(title: nil, message: "Check all previous episodes as watched?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Check One", style: .default, handler: { action in
            callback(false)
        }))
        alert.addAction(UIAlertAction(title: "Check All", style: .default, handler: { action in
            callback(true)
        }))
        present(alert, animated: true, completion: nil)
    }
    
    func reloadSeason(season: Int32) {
        let section = Int(season) - 1
        reloadSection(section)
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
        let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: SeasonHeaderView.reuseIdentifier) as! SeasonHeaderView
        
        let season = seasons[section]
        header.title = "Season \(season.number)"
        header.episodeCount = season.watchedEpisodes
        header.checkbox.isChecked = season.isWatched
        header.isExpanded = season.isExpanded
        header.tapCallback = {
            season.isExpanded = !season.isExpanded
            self.reloadSection(section)
        }
        header.checkbox.checkedChangeCallback = { isChecked in
            if season.isWatched == isChecked {
                return
            }
            self.presenter.onSeasonWatchedStateToggled(season: season)
        }
        
        return header
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let section = indexPath.section
        let row = indexPath.row
        let season = seasons[section]
        let episode = season.episodes[row]
        let isLastInSeason = row == (season.episodes.count - 1)
        let isLast = section == (seasons.count - 1) && isLastInSeason
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "episode_cell", for: indexPath) as! EpisodeCell
        cell.bind(episode: episode)
        
        cell.middleDivider.isHidden = isLastInSeason
        cell.fullDivider.isHidden = !isLastInSeason || isLast
        
        cell.checkbox.checkedChangeCallback = { isChecked in
            if episode.isWatched == isChecked {
                return
            }
            self.presenter.onEpisodeWatchedStateToggled(episode: episode)
        }
        
        return cell
    }
    
    private func reloadSection(_ section: Int, animated: Bool = false) {
        tableView.reloadSections(IndexSet(arrayLiteral: section), with: animated ? .automatic : .none)
    }
}
