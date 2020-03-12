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
    
    @IBInspectable
    var errorText: String = "" {
        didSet {
            errorView.errorText = self.errorText
        }
    }
    
    var retryTappedCallback: (() -> Void)? {
        didSet {
            errorView.retryTappedCallback = self.retryTappedCallback
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
    
    private let errorView: ErrorView = {
        let view = ErrorView()
        return view
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
    
    func scrollToTop(animated: Bool = false) {
        if numberOfSections > 0 && numberOfRows(inSection: 0) > 0 {
            scrollToRow(at: IndexPath(row: 0, section: 0), at: .top, animated: animated)
        }
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
    
    func showErrorView() {
        backgroundView = errorView
    }
    
    func hideErrorView() {
        if backgroundView === errorView {
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
