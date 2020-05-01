import UIKit

class MultichoicePrefereenceCell: UITableViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var checkIcon: UIImageView!
    
    var isChecked: Bool = false {
        didSet { updateIcon() }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        updateIcon()
    }
    
    private func updateIcon() {
        checkIcon.isHidden = !isChecked
    }
}
