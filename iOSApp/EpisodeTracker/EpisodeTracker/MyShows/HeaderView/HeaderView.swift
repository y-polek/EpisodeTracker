import UIKit

class HeaderView: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var icon: UIImageView!
    
    var title: String = "" {
        didSet {
            updateLabel()
        }
    }
    
    var isExpanded: Bool = true {
        didSet {
            updateIcon()
        }
    }
    
    var tapCallback: (() -> Void)?
    
    private let expandedImage = UIImage(named: "ic-chevron-down")
    private let collapsedImage = UIImage(named: "ic-chevron-up")
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle.main.loadNibNamed("HeaderView", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        contentView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        updateLabel()
        updateIcon()
        
        contentView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTap)))
    }
    
    private func updateLabel() {
        label.text = title
    }
    
    private func updateIcon() {
        icon.image = isExpanded ? expandedImage : collapsedImage
    }
    
    @objc func onTap(_ recognizer: UITapGestureRecognizer) {
        tapCallback?()
    }
}
