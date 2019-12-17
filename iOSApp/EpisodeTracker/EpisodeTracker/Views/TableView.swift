import UIKit

@IBDesignable
class TableView: UITableView {
    
    @IBInspectable
    var promptText: String = "" {
        didSet {
            promptLabel.text = promptText
        }
    }
    
    @IBInspectable
    var emptyText: String = "" {
        didSet {
            emptyLabel.text = emptyText
        }
    }
    
    private let promptLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        return label
    }()
    private let emptyLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        return label
    }()
    private let activityIndicator: UIActivityIndicatorView = {
        if #available(iOS 13.0, *) {
            return UIActivityIndicatorView(style: .large)
        } else {
            return UIActivityIndicatorView(style: .gray)
        }
    }()
    
    override init(frame: CGRect, style: UITableView.Style) {
        super.init(frame: frame, style: style)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        promptLabel.center = CGPoint(x: bounds.midX, y: bounds.midY)
        emptyLabel.center = CGPoint(x: bounds.midX, y: bounds.midY)
        
        addSubview(activityIndicator)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        activityIndicator.center = CGPoint(x: bounds.midX, y: bounds.midY)
    }
    
    func showPromptView() {
        backgroundView = promptLabel
    }
    
    func hidePromptView() {
        if backgroundView === promptLabel {
            backgroundView = nil
        }
    }
    
    func showEmptyView() {
        backgroundView = emptyLabel
    }
    
    func hideEmptyView() {
        if backgroundView === emptyLabel {
            backgroundView = nil
        }
    }
    
    func showProgress() {
        activityIndicator.isHidden = false
        activityIndicator.startAnimating()
    }
    
    func hideProgress() {
        activityIndicator.stopAnimating()
    }
}
