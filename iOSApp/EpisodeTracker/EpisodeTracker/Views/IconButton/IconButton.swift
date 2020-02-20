import UIKit
import MaterialComponents.MaterialRipple

@IBDesignable
class IconButton: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var iconView: UIImageView! {
        didSet {
            self.iconView.tintColor = .textColorPrimary
        }
    }
    
    @IBInspectable
    var image: UIImage? {
        didSet {
            iconView.image = self.image
        }
    }
    
    var tapCallback: (() -> Void)?
    
    private let rippleController = MDCRippleTouchController()

    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle(for: IconButton.self).loadNibNamed("IconButton", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        clipsToBounds = true
        
        rippleController.addRipple(to: self)
        
        addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTapped(gesture:))))
    }
    
    @objc private func onTapped(gesture: UIGestureRecognizer) {
        tapCallback?()
    }
}
