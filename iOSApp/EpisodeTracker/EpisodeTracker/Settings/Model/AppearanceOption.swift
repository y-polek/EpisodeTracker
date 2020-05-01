import UIKit
import SharedCode

enum AppearanceOption: Int, CaseIterable, CustomStringConvertible {
    
    case automatic
    case light
    case dark
    
    var description: String {
        switch self {
        case .automatic: return "Automatic"
        case .light: return "Light"
        case .dark: return "Dark"
        }
    }
    
    var appearance: Appearance {
        switch self {
        case .automatic: return .automatic
        case .light: return .light
        case .dark: return .dark
        }
    }
}
