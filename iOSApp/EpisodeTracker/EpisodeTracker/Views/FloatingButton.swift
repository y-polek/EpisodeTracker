import UIKit
import MaterialComponents.MaterialButtons

class FloatingButton: MDCFloatingButton {
    
    let activityIndicator: UIActivityIndicatorView = {
        let indicator = UIActivityIndicatorView(style: UIActivityIndicatorView.Style.white)
        indicator.hidesWhenStopped = true
        indicator.color = .textColorPrimaryInverse
        return indicator
    }()
    
    var image: UIImage? = nil
    
    var isActivityIndicatorHidden = true {
        didSet {
            if isActivityIndicatorHidden {
                hideActivityIndicator()
            } else {
                showActivityIndicator()
            }
        }
    }
    
    private func showActivityIndicator() {
        addSubview(activityIndicator)
        activityIndicator.center = imageView?.center ?? CGPoint(x: 0, y: 0)
        activityIndicator.startAnimating()
        image = image(for: .normal)
        setImage(nil, for: .normal)
    }
    
    private func hideActivityIndicator() {
        activityIndicator.stopAnimating()
        activityIndicator.removeFromSuperview()
        setImage(image, for: .normal)
    }
}
