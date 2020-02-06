import UIKit

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
    
    private let expandedImage = UIImage(named: "ic-chevron-up")
    private let collapsedImage = UIImage(named: "ic-chevron-down")
    
    override init(reuseIdentifier: String?) {
        super.init(reuseIdentifier: reuseIdentifier)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
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
