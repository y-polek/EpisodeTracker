enum PreferenceSection: Int, CaseIterable, CustomStringConvertible {
    
    case appearance
    case myShows
    case specials
    
    var description: String {
        switch self {
        case .appearance: return "Appearance"
        case .myShows: return "My Shows"
        case .specials: return "Specials"
        }
    }
}
