import UIKit

class TrailerCell: RippleCollectionViewCell {
    
    @IBOutlet weak var previewImageView: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var playButton: ImageButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        rippleView.layer.cornerRadius = previewImageView.layer.cornerRadius
    }
}
