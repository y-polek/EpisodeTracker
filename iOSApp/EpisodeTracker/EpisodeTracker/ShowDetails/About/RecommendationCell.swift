import UIKit

class RecommendationsCell: RippleCollectionViewCell {
    
    @IBOutlet weak var backdropImageView: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var subheadLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        rippleView.layer.cornerRadius = backdropImageView.layer.cornerRadius
    }
}
