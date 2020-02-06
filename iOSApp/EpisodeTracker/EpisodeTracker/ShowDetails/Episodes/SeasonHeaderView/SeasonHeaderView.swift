import UIKit

class SeasonHeaderView: UIView {
    
    @IBOutlet var contentView: UIView!
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
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle.main.loadNibNamed("SeasonHeaderView", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        contentView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        updateTitle()
        updateEpisodeCount()
        updateEpisodeCount()
        
        contentView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTap)))
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
