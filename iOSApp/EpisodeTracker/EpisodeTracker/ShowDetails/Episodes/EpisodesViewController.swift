import UIKit
import SharedCode

class EpisodesViewController: UIViewController {
    
    var showId: Int!
    var presenter: EpisodesPresenter!
    var seasons = [SeasonViewModel]()
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.register(SeasonHeaderView.nib, forHeaderFooterViewReuseIdentifier: SeasonHeaderView.reuseIdentifier)
        
        showActivityIndicator()
        
        let app = AppDelegate.instance()
        presenter = EpisodesPresenter(
            showId: Int32(showId),
            myShowsRepository: app.myShowsRepository,
            episodesRepository: app.episodesRepository,
            showRepository: app.showRepository)
        presenter.attachView(view: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
    
    func setBottomInset(_ inset: CGFloat) {
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: inset, right: 0)
    }
    
    private func showActivityIndicator() {
        tableView.isHidden = true
        activityIndicator.startAnimating()
    }
    
    private func hideActivityIndicator() {
        tableView.isHidden = false
        activityIndicator.stopAnimating()
    }
}

extension EpisodesViewController: EpisodesView {
    
    func displaySeasons(seasons: [SeasonViewModel]) {
        self.seasons = seasons
        tableView.reloadData()
        hideActivityIndicator()
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
