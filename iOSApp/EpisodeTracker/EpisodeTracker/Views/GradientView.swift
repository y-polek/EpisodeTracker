import UIKit

@IBDesignable
class GradientView: UIView {
    
    @IBInspectable
    var startColor: UIColor = UIColor.clear {
        didSet { updateView() }
    }
    
    @IBInspectable
    var endColor: UIColor = UIColor.clear {
        didSet { updateView() }
    }

    @IBInspectable
    var isHorizontal: Bool = true {
        didSet { updateView() }
    }
    
    override class var layerClass: AnyClass {
        get {
            return CAGradientLayer.self
        }
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        updateView()
    }
    
    func updateView() {
        let layer = self.layer as! CAGradientLayer
        layer.colors = [startColor, endColor].map { $0.cgColor }
        if (isHorizontal) {
            layer.startPoint = CGPoint(x: 0, y: 0.5)
            layer.endPoint = CGPoint (x: 1, y: 0.5)
        } else {
            layer.startPoint = CGPoint(x: 0.5, y: 0)
            layer.endPoint = CGPoint (x: 0.5, y: 1)
        }
    }
}
