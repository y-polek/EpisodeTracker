import UIKit
import MaterialComponents.MaterialRipple

@IBDesignable
class Checkbox: UIView {
    
    static let IMAGE = UIImage(named: "ic-check")
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var imageView: UIImageView!
    
    @IBInspectable
    var isChecked: Bool = false {
        didSet { updateCheckedState() }
    }
    
    var checkedChangeCallback: ((Bool) -> Void)?
    
    private let rippleController = MDCRippleTouchController()
    
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
        updateUi()
    }
    
    func toggle() {
        isChecked = !isChecked
    }
    
    private func setup() {
        Bundle(for: ImageButton.self).loadNibNamed("Checkbox", owner: self, options: nil)
        addSubview(contentView)
        
        updateUi()
        
        rippleController.addRipple(to: self)
        
        addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTapped(gesture:))))
    }
    
    private func updateUi() {
        contentView.frame = self.bounds
        
        layer.borderWidth = 2
        layer.borderColor = tintColor.cgColor
        layer.cornerRadius = frame.width / 2
        clipsToBounds = true
        
        imageView.image = Checkbox.IMAGE
        imageView.tintColor = tintColor
        
        updateCheckedState()
    }
    
    private func updateCheckedState() {
        contentView.backgroundColor = isChecked ? tintColor : nil
        imageView.tintColor = isChecked ? .white : tintColor
    }
    
    @objc private func onTapped(gesture: UIGestureRecognizer) {
        toggle()
        checkedChangeCallback?(isChecked)
    }
}
