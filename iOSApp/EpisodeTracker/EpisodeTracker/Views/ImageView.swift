import UIKit
import Kingfisher

@IBDesignable
class ImageView: UIImageView {
    
    @IBInspectable
    var imageUrl: String? {
        didSet {
            updateImage()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        contentMode = .scaleAspectFill
        
        layer.cornerRadius = 8
        layer.masksToBounds = true
    }
    
    private func updateImage() {
        if imageUrl != nil {
            kf.setImage(with: URL(string: imageUrl!))
        } else {
            image = nil
        }
    }
}
