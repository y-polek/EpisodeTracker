import UIKit

@IBDesignable
class Divider: UIView {
    
    @IBInspectable
    var color: UIColor = UIColor.gray {
        didSet {
            setNeedsDisplay()
        }
    }
    
    var thickness: CGFloat = 1 {
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
        let top = (rect.height - thickness) / 2
        let path = UIBezierPath(rect: CGRect(x: 0, y: top, width: rect.width, height: thickness))
        color.setFill()
        path.fill()
    }
}
