import UIKit
import MaterialComponents.MaterialRipple

class RippleTableViewCell: UITableViewCell {
    
    @IBOutlet weak var rippleView: MDCRippleView!
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        if let location = touches.first?.location(in: rippleView) {
            rippleView.beginRippleTouchDown(at: location, animated: true, completion: nil)
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
}
