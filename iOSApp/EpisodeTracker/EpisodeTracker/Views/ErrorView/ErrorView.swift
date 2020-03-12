import UIKit
import MaterialComponents.MaterialButtons

@IBDesignable
class ErrorView: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var messageLabel: UILabel!
    @IBOutlet weak var retryButton: MDCButton!
    
    @IBInspectable
    var errorText: String = "Error" {
        didSet { updateMessageLabel() }
    }
    
    var retryTappedCallback: (() -> Void)?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle(for: ErrorView.self).loadNibNamed("ErrorView", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        
        updateMessageLabel()
        
        retryButton.hitAreaInsets = UIEdgeInsets(top: -4, left: 0, bottom: -4, right: 0)
    }
    
    @IBAction func onRetryTapped(_ sender: Any) {
        retryTappedCallback?()
    }
    
    private func updateMessageLabel() {
        messageLabel.text = self.errorText
    }
}
