import UIKit
import Kingfisher
import SharedCode

class UpcomingShowCell: UITableViewCell {
    
    @IBOutlet weak var backgroundImage: ImageView!
    
    func bind(show: MyShowsListItem.UpcomingShowViewModel) {
        backgroundImage.imageUrl = show.backdropUrl
    }
}
