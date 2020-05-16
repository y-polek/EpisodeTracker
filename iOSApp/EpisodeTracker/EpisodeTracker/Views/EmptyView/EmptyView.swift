import UIKit
import MaterialComponents.MaterialButtons

@IBDesignable
class EmptyView: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var messageLabel: UILabel!
    @IBOutlet weak var actionButton: MDCButton!
    
    @IBInspectable
    var messageText: String = "Empty" {
        didSet { updateMessageLabel() }
    }
    
    @IBInspectable
    var actionName: String = "Action" {
        didSet { updateActionButton() }
    }
    
    @IBInspectable
    var isActionButtonHidden: Bool = false {
        didSet { updateActionButton() }
    }
    
    var actionTappedCallback: (() -> Void)?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle(for: EmptyView.self).loadNibNamed("EmptyView", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        
        updateMessageLabel()
        updateActionButton()
        
        actionButton.hitAreaInsets = UIEdgeInsets(top: -4, left: 0, bottom: -4, right: 0)
    }
    
    @IBAction func onActionTapped(_ sender: Any) {
        actionTappedCallback?()
    }
    
    private func updateMessageLabel() {
        messageLabel.text = messageText
    }
    
    private func updateActionButton() {
        actionButton.setTitle(actionName, for: .normal)
        actionButton.isUppercaseTitle = false
        actionButton.isHidden = isActionButtonHidden
    }
}
