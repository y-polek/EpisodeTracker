import UIKit

@IBDesignable
class Divider: UIView {
    
    @IBInspectable
    var color: UIColor = UIColor.gray {
        didSet {
            setNeedsDisplay()
        }
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
        backgroundColor = .transparent
    }
    
    override func draw(_ rect: CGRect) {
        let path = UIBezierPath(rect: rect)
        color.setFill()
        path.fill()
    }
}
