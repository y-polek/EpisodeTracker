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
}
