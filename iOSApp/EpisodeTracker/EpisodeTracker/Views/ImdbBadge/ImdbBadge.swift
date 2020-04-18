import UIKit
import MaterialComponents.MaterialRipple

@IBDesignable
class ImdbBadge: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var label: UILabel!
    
    @IBInspectable
    var iconOnly: Bool = false {
        didSet { updateLabel() }
    }

    var rating: Float? = nil {
        didSet { updateLabel() }
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
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        layer.borderColor = label.textColor.cgColor
    }
    
    private func setup() {
        Bundle(for: ImdbBadge.self).loadNibNamed("ImdbBadge", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        
        layer.borderWidth = 1
        layer.borderColor = label.textColor.cgColor
        layer.cornerRadius = 5
        clipsToBounds = true
        
        rippleController.addRipple(to: self)
        
        updateLabel()
        
        addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTapped(gesture:))))
    }
    
    private func updateLabel() {
        if self.rating != nil {
            label.text = String(format: "%.1f", self.rating!)
        } else {
            label.text = "—"
        }
        
        label.isHidden = iconOnly
    }
    
    @objc private func onTapped(gesture: UIGestureRecognizer) {
        tapCallback?()
    }
}
