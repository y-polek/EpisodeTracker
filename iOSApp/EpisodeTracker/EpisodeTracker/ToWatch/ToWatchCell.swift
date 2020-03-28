import UIKit
import SharedCode

class ToWatchCell: RippleCollectionViewCell {
    
    @IBOutlet weak var backgroundImage: ImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var episodeCountLabel: UILabel!
    @IBOutlet weak var checkButton: ImageButton!
    
    func bind(_ show: ToWatchShowViewModel) {
        rippleView.layer.cornerRadius = backgroundImage.layer.cornerRadius
        
        backgroundImage.imageUrl = show.imageUrl
        titleLabel.text = show.name
        episodeCountLabel.text = String(show.episodeCount)
        
        let subtitleText = "\(show.episodeNumber) | \(show.episodeName)"
        subtitleLabel.attributedText = subtitleText.bold(
            font: subtitleLabel.font,
            location: 0,
            length: show.episodeNumber.count)
    }
}
