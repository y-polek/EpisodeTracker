import UIKit

extension UIColor {
    
    static let transparent = UIColor(red: 0, green: 0, blue: 0, alpha: 0)
    
    static let textColorPrimary = color("TextColorPrimary")
    static let textColorSecondary = color("TextColorSecondary")
    static let textColorPrimaryInverse = color("TextColorPrimaryInverse")
    static let accent = color("Accent")
    static let dividerPrimary = color("DividerPrimary")
    static let windowBackground = color("WindowBackground")
    static let ripple = color("RippleColor")
    
    private static let bundle = Bundle(for: AppDelegate.self)
    
    private static func color(_ named: String) -> UIColor {
        UIColor(named: named, in: UIColor.bundle, compatibleWith: nil)!
    }
}
