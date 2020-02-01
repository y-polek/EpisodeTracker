import UIKit
import Kingfisher

@IBDesignable
class ImageView: UIImageView {
    
    var imageUrl: String? {
        didSet {
            updateImage()
        }
    }
    
    @IBInspectable
    var overlayColor: UIColor? {
        didSet {
            updateOverlay()
        }
    }
    
    @IBInspectable
    var cornerRadius: CGFloat = 8 {
        didSet {
            layer.cornerRadius = self.cornerRadius
        }
    }
    
    private let overlayLayer = CAGradientLayer()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        updateOverlay()
    }
    
    private func setup() {
        contentMode = .scaleAspectFill
        
        layer.cornerRadius = cornerRadius
        layer.masksToBounds = true
        
        layer.insertSublayer(overlayLayer, at: 0)
    }
    
    private func updateImage() {
        if imageUrl != nil {
            kf.setImage(with: URL(string: imageUrl!))
        } else {
            image = nil
        }
    }
    
    private func updateOverlay() {
        overlayLayer.frame = bounds
        
        if let color = overlayColor {
            overlayLayer.colors = [color.withAlphaComponent(0), color.withAlphaComponent(0.8).cgColor]
        } else {
            overlayLayer.colors = [UIColor.transparent.cgColor, UIColor.transparent.cgColor]
        }
    }
}
