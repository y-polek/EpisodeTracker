import UIKit
import SharedCode

class UpcomingShowCell: UITableViewCell {
    
    @IBOutlet weak var backgroundImage: ImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var timeLeftLabel: UILabel!
    
    func bind(show: MyShowsListItem.UpcomingShowViewModel) {
        backgroundImage.imageUrl = show.backdropUrl
        titleLabel.text = show.name
        subtitleLabel.text = "\(show.episodeNumber) | \(show.episodeName)"
        timeLeftLabel.text = show.timeLeft
    }
}
