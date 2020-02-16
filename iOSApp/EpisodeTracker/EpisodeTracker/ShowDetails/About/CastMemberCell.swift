import UIKit

class CastMemberCell: RippleCollectionViewCell {
    
    @IBOutlet weak var portraitImageView: ImageView!
    @IBOutlet weak var actorNameLabel: UILabel!
    @IBOutlet weak var characterNameLabel: UILabel!
    
    override func layoutSubviews() {
        super.layoutSubviews()
        setup()
    }
    
    private func setup() {
        rippleView.layer.cornerRadius = portraitImageView.layer.cornerRadius
    }
}
