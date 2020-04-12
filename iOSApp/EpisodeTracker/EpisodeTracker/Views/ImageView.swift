import UIKit
import Kingfisher

@IBDesignable
class ImageView: UIImageView {
    
    var imageUrl: String? {
        didSet {
            if oldValue != imageUrl {
                updateImage()
            }
        }
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
    
    var overlayOpacity: [CGFloat] = [0.0, 0.6] {
        didSet {
            if oldValue != self.overlayOpacity {
                updateOverlay()
            }
        }
    }
    
    var isBlured: Bool = false {
        didSet {
            if oldValue != self.isBlured {
                updateBlur()
            }
        }
    }
    
    var blurAlpha: CGFloat = 1.0 {
        didSet {
            if oldValue != self.blurAlpha {
                updateBlur()
            }
        }
    }
    
    private let overlayLayer = CAGradientLayer()
    private let blurView = UIVisualEffectView(effect: UIBlurEffect(style: .dark))
    
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
        
        withDisabledAnimation {
            overlayLayer.frame = bounds
        }
        blurView.frame = bounds
    }
    
    private func setup() {
        layer.cornerRadius = cornerRadius
        layer.masksToBounds = true
        
        layer.insertSublayer(overlayLayer, at: 0)
        addSubview(blurView)
        
        updateOverlay()
        updateBlur()
    }
    
    private func updateImage() {
        if imageUrl != nil {
            kf.setImage(with: URL(string: imageUrl!), options: [.backgroundDecode])
        } else {
            image = nil
        }
    }
    
    private func updateOverlay() {
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
    
    private func updateBlur() {
        blurView.frame = bounds
        blurView.isHidden = !isBlured
        blurView.alpha = blurAlpha
    }
}
