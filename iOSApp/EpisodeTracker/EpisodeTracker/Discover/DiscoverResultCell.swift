import UIKit
import SharedCode

class DiscoverResultCell: UITableViewCell {
    
    @IBOutlet weak var posterView: ImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var overviewLabel: UILabel!
    @IBOutlet weak var divider: Divider!
    
    func bind(result: DiscoverResultViewModel) {
        posterView.imageUrl = result.posterUrl
        titleLabel.text = result.name
        overviewLabel.text = result.overview
        
        let subtitleText: NSAttributedString
        let yearText = result.year?.stringValue ?? ""
        let generesText = result.genres.joined(separator: ", ")
        if result.year != nil && !result.genres.isEmpty {
            subtitleText = "\(yearText) | \(generesText)".bold(font: subtitleLabel.font, location: 0, length: yearText.count)
        } else if result.year != nil {
            subtitleText = yearText.bold(font: subtitleLabel.font)
        } else {
            subtitleText = NSAttributedString(string: generesText)
        }
        
        subtitleLabel.attributedText = subtitleText
        subtitleLabel.isHidden = result.year == nil && result.genres.isEmpty
    }
}
