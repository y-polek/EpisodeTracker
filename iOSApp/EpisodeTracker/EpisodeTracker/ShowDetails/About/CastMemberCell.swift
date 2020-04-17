import UIKit

class CastMemberCell: RippleCollectionViewCell {
    
    @IBOutlet weak var portraitImageView: ImageView!
    @IBOutlet weak var actorNameLabel: UILabel!
    @IBOutlet weak var characterNameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        rippleView.layer.cornerRadius = portraitImageView.layer.cornerRadius
    }
}
