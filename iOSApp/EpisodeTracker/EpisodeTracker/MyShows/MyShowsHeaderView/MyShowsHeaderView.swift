import UIKit
import MaterialComponents.MaterialRipple

class MyShowsHeaderView: UITableViewHeaderFooterView {
    
    static let reuseIdentifier = String(describing: self)
    static var nib: UINib {
        return UINib(nibName: String(describing: self), bundle: Bundle.main)
    }
    
    private static let expandedImage = UIImage(named: "ic-chevron-up")
    private static let collapsedImage = UIImage(named: "ic-chevron-down")
    
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var icon: UIImageView!
    
    var title: String = "" {
        didSet { updateLabel() }
    }
    
    var isExpanded: Bool = true {
        didSet { updateIcon() }
    }
    
    var tapCallback: (() -> Void)?
    
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
        rippleController.addRipple(to: self)
        
        addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTap)))
    }
    
    private func updateLabel() {
        label.text = title
    }
    
    private func updateIcon() {
        icon.image = isExpanded ? MyShowsHeaderView.expandedImage : MyShowsHeaderView.collapsedImage
    }
    
    @objc func onTap(_ recognizer: UITapGestureRecognizer) {
        tapCallback?()
    }
}
