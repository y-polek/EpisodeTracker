import UIKit
import SharedCode

class EpisodesViewController: UIViewController {
    
    var showId: Int!
    var seasons = [SeasonViewModel]()
    
    /**
     * Returns `true` if scroll should be blocked (offset set to `0`), `false` otherwise.
     */
    var scrollCallback: ((_ offset: CGFloat) -> Bool)?
    var seasonWatchedStateToggleCallback: ((_ season: SeasonViewModel) -> Void)?
    var episodeWatchedStateToggleCallback: ((_ episode: EpisodeViewModel) -> Void)?
    var retryTapCallback: (() -> Void)?
    
    @IBOutlet weak var tableView: TableView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(SeasonHeaderView.nib, forHeaderFooterViewReuseIdentifier: SeasonHeaderView.reuseIdentifier)
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        tableView.retryTappedCallback = {
            self.retryTapCallback?()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        tableView.retryTappedCallback = nil
    }
    
    func displaySeasons(_ seasons: [SeasonViewModel]) {
        self.seasons = seasons
        tableView.reloadData()
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
    
    func reloadSeason(_ season: Int32) {
        let section = Int(season) - 1
        reloadSection(section)
    }
    
    func setBottomInset(_ inset: CGFloat) {
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: inset, right: 0)
    }
    
    func showActivityIndicator() {
        tableView.isHidden = true
        activityIndicator.startAnimating()
    }
    
    func hideActivityIndicator() {
        tableView.isHidden = false
        activityIndicator.stopAnimating()
    }
    
    func showError() {
        tableView.showErrorView()
    }
    
    func hideError() {
        tableView.hideErrorView()
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
            
            self.seasonWatchedStateToggleCallback?(season)
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
            self.episodeWatchedStateToggleCallback?(episode)
        }
        
        return cell
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollCallback == nil { return }
        
        let offset = scrollView.contentOffset.y
        let blockScroll = scrollCallback!(offset)
        if blockScroll {
            scrollView.contentOffset.y = 0
        }
    }
    
    private func reloadSection(_ section: Int, animated: Bool = false) {
        tableView.reloadSections(IndexSet(arrayLiteral: section), with: animated ? .automatic : .none)
    }
}
