import UIKit
import Kingfisher
import SharedCode

class ShowCell: UITableViewCell {
    
    @IBOutlet weak var backgroundImage: ImageView!
    
    func bind(show: MyShowsListItem.ShowViewModel) {
        backgroundImage.imageUrl = show.backdropUrl
    }
}
