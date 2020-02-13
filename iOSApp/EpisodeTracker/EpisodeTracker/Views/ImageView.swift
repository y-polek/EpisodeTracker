import UIKit
import Kingfisher

@IBDesignable
class ImageView: UIImageView {
    
    var imageUrl: String? {
        didSet { updateImage() }
    }
    
    @IBInspectable
    var overlayColor: UIColor? {
        didSet { updateOverlay() }
    }
    
    @IBInspectable
    var cornerRadius: CGFloat = 8 {
        didSet { layer.cornerRadius = self.cornerRadius }
    }
    
    @IBInspectable
    var shadowColor: UIColor? = nil {
        didSet { updateShadow() }
    }
    
    var overlayOpacity: [CGFloat] = [0.0, 0.6]
    
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
        layer.cornerRadius = cornerRadius
        layer.masksToBounds = true
        
        layer.insertSublayer(overlayLayer, at: 0)
    }
    
    private func updateImage() {
        if imageUrl != nil {
            kf.setImage(with: URL(string: imageUrl!), options: [.backgroundDecode])
        } else {
            image = nil
        }
    }
    
    private func updateOverlay() {
        overlayLayer.frame = bounds
        
        if let color = overlayColor {
            overlayLayer.colors = overlayOpacity.map {
                color.withAlphaComponent($0).cgColor
            }
        } else {
            overlayLayer.colors = [UIColor.transparent.cgColor, UIColor.transparent.cgColor]
        }
    }
    
    private func updateShadow() {
        layer.shadowColor = shadowColor?.cgColor
        layer.shadowOffset = CGSize(width: 1, height: 1)
        layer.shadowOpacity = 1
        layer.shadowRadius = 1
        if shadowColor != nil {
            clipsToBounds = false
        } else {
            clipsToBounds = true
        }
    }
}
