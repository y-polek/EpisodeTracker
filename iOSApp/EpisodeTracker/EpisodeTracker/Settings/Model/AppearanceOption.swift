import UIKit
import SharedCode

enum AppearanceOption: Int, CaseIterable, CustomStringConvertible {
    
    case automatic
    case light
    case dark
    
    var description: String {
        switch self {
        case .automatic: return string(R.str.prefs_automatic)
        case .light: return string(R.str.prefs_light)
        case .dark: return string(R.str.prefs_dark)
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
