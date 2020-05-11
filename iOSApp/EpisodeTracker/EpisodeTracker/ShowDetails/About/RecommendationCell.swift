import UIKit
import SharedCode

class RecommendationsCell: RippleCollectionViewCell {
    
    private static var addImage: UIImage = UIImage(named: "ic-add")!
    private static var checkImage: UIImage = UIImage(named: "ic-minus")!
    
    @IBOutlet weak var backdropImageView: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var subheadLabel: UILabel!
    @IBOutlet weak var addButton: ImageButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        rippleView.layer.cornerRadius = backdropImageView.layer.cornerRadius
    }
    
    func bind(show: RecommendationViewModel) {
        backdropImageView.imageUrl = show.imageUrl
        nameLabel.text = show.name
        subheadLabel.text = show.subhead
        subheadLabel.isHidden = show.subhead.isEmpty
        addButton.image = show.isInMyShows ? RecommendationsCell.checkImage : RecommendationsCell.addImage
        addButton.isActivityIndicatorHidden = !show.isAddInProgress
    }
}
