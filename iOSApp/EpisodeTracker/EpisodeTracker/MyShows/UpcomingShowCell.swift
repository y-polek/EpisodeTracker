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
        timeLeftLabel.text = show.timeLeft
        
        let subtitleText = "\(show.episodeNumber) | \(show.episodeName)"
        subtitleLabel.attributedText = subtitleText.bold(
            font: subtitleLabel.font,
            location: 0,
            length: show.episodeNumber.count)
    }
}
