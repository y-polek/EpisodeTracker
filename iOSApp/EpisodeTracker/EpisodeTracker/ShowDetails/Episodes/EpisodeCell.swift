import UIKit
import SharedCode

class EpisodeCell: UITableViewCell {
    
    @IBOutlet weak var episodeImage: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var checkbox: Checkbox!
    @IBOutlet weak var middleDivider: Divider!
    @IBOutlet weak var fullDivider: Divider!
    
    var episode: EpisodeViewModel?
    
    func bind(episode: EpisodeViewModel) {
        self.episode = episode
        
        episodeImage.imageUrl = episode.imageUrl
        nameLabel.text = "\(episode.number). \(episode.name)"
        dateLabel.text = episode.airDate
        checkbox.isChecked = episode.isWatched
    }
}
