import UIKit
import MaterialComponents.MaterialRipple
import SwipeCellKit

@IBDesignable
class SwipeRippleTableViewCell: SwipeTableViewCell {
    
    @IBInspectable
    var rippleColor: UIColor = .ripple {
        didSet { updateColor() }
    }
    
    @IBOutlet
    weak var rippleView: MDCRippleView! {
        didSet { updateColor() }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        
        if let touch = touches.first {
            if shouldBeginRippleWith(touch) {
                let location = touch.location(in: rippleView)
                rippleView.beginRippleTouchDown(at: location, animated: true, completion: nil)
            }
        }
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesEnded(touches, with: event)
        rippleView.beginRippleTouchUp(animated: true, completion: nil)
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesCancelled(touches, with: event)
        rippleView.beginRippleTouchUp(animated: true, completion: nil)
    }
    
    func shouldBeginRippleWith(_ touch: UITouch) -> Bool {
        return true
    }
    
    private func updateColor() {
        rippleView?.rippleColor = rippleColor
    }
}
