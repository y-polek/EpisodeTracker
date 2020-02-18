import UIKit

class RecommendationsCell: RippleCollectionViewCell {
    
    @IBOutlet weak var backdropImageView: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    
    override func layoutSubviews() {
        super.layoutSubviews()
        setup()
    }
    
    private func setup() {
        rippleView.layer.cornerRadius = backdropImageView.layer.cornerRadius
    }
}
