import UIKit

extension String {
    
    func bold(font: UIFont, location: Int = 0, length: Int = -1) -> NSAttributedString {
        let string = NSMutableAttributedString(string: self)
        string.addAttribute(
            .font,
            value: font.bold(),
            range: NSRange(location: location, length: (length >= 0 ? length : self.count - location)))
        return string
    }
}
