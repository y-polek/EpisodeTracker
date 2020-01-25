import UIKit
import MaterialComponents.MaterialRipple
import SharedCode

class ShowCell: RippleTableViewCell {
    
    @IBOutlet weak var backgroundImage: ImageView!
    @IBOutlet weak var titleLabel: UILabel!
    
    func bind(show: MyShowsListItem.ShowViewModel) {
        selectionStyle = .none
        rippleView.layer.cornerRadius = backgroundImage.layer.cornerRadius
        
        backgroundImage.imageUrl = show.backdropUrl
        titleLabel.text = show.name
    }
}
