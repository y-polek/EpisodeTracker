import UIKit
import MaterialComponents.MaterialRipple
import SharedCode

class EpisodeCell: RippleTableViewCell {
    
    @IBOutlet weak var episodeImage: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var checkbox: Checkbox!
    @IBOutlet weak var timeLeftLabel: UILabel!
    @IBOutlet weak var middleDivider: Divider!
    @IBOutlet weak var fullDivider: Divider!
    
    var episode: EpisodeViewModel?
    
    func bind(episode: EpisodeViewModel) {
        self.episode = episode
        
        episodeImage.imageUrl = episode.imageUrl
        nameLabel.text = "\(episode.number.episode). \(episode.name)"
        dateLabel.text = episode.airDate
        checkbox.isChecked = episode.isWatched
        timeLeftLabel.text = episode.timeLeftToRelease
        
        checkbox.isHidden = !episode.isCheckboxVisible
        timeLeftLabel.isHidden = !episode.isTimeLeftVisible
    }
}
