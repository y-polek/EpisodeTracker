import UIKit
import MaterialComponents.MaterialRipple

class SeasonHeaderView: UITableViewHeaderFooterView {
    
    static let reuseIdentifier = String(describing: self)
    static var nib: UINib {
        return UINib(nibName: String(describing: self), bundle: Bundle.main)
    }
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var countLabel: UILabel!
    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var checkbox: Checkbox!
    
    var title: String = "" {
        didSet { updateTitle() }
    }
    
    var episodeCount: String = "" {
        didSet { updateEpisodeCount() }
    }
    
    var isExpanded: Bool = true {
        didSet { updateIcon() }
    }
    
    var tapCallback: (() -> Void)?
    
    /**
     Fixes "[LayoutConstraints] Unable to simultaneously satisfy constraints" error.
     See: https://stackoverflow.com/a/35053234/2137020
     */
    override var frame: CGRect {
        get { return super.frame }
        set {
            if newValue.width == 0 { return }
            super.frame = newValue
        }
    }
    
    private let expandedImage = UIImage(named: "ic-chevron-up")
    private let collapsedImage = UIImage(named: "ic-chevron-down")
    
    private let rippleController = MDCRippleTouchController()
    
    override init(reuseIdentifier: String?) {
        super.init(reuseIdentifier: reuseIdentifier)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        rippleController.rippleView.rippleColor = .ripple
        rippleController.delegate = self
        rippleController.addRipple(to: self)
        
        addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTap)))
    }
    
    private func updateTitle() {
        titleLabel.text = title
    }
    
    private func updateEpisodeCount() {
        countLabel.text = episodeCount
    }
    
    private func updateIcon() {
        icon.image = isExpanded ? expandedImage : collapsedImage
    }
    
    @objc func onTap(_ recognizer: UITapGestureRecognizer) {
        tapCallback?()
    }
}

extension SeasonHeaderView: MDCRippleTouchControllerDelegate {
    
    func rippleTouchController(_ rippleTouchController: MDCRippleTouchController, shouldProcessRippleTouchesAtTouchLocation location: CGPoint) -> Bool {
        return !checkbox.frame.contains(location)
    }
}
