import UIKit

class ImdbBadge: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var label: UILabel!
    
    var rating: Float? = nil {
        didSet { updateLabel() }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle(for: ImdbBadge.self).loadNibNamed("ImdbBadge", owner: self, options: nil)
        addSubview(contentView)
        contentView.frame = self.bounds
        
        layer.borderWidth = 1
        layer.borderColor = UIColor.textColorPrimary.cgColor
        layer.cornerRadius = 8
        clipsToBounds = true
    }
    
    private func updateLabel() {
        if self.rating != nil {
            label.text = String(format: "%.1f", self.rating!)
        } else {
            label.text = "—"
        }
    }
}
