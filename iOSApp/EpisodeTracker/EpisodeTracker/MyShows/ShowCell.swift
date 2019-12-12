import UIKit
import SharedCode

class ShowCell: UITableViewCell {
    
    @IBOutlet weak var backgroundImage: ImageView!
    @IBOutlet weak var titleLabel: UILabel!
    
    func bind(show: MyShowsListItem.ShowViewModel) {
        backgroundImage.imageUrl = show.backdropUrl
        titleLabel.text = show.name
    }
}
