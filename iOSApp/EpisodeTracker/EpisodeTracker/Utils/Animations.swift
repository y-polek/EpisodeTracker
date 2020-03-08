import UIKit

func withDisabledAnimation(block: () -> Void) {
    CATransaction.begin()
    CATransaction.setDisableActions(true)
    
    block()
    
    CATransaction.commit()
}
