import UIKit

extension UIViewController {
    
    func addHideKeyboardByTapGestureRecognizer() {
        let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(hideKeyboard))
        tapRecognizer.cancelsTouchesInView = false
        view.addGestureRecognizer(tapRecognizer)
    }
    
    @objc func hideKeyboard() {
        view.endEditing(true)
    }
}
