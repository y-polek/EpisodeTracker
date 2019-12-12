import UIKit

@IBDesignable
class Divider: UIView {
    
    @IBInspectable
    let color: UIColor = UIColor.white.withAlphaComponent(0.25)
    
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
