enum PreferenceSection: CustomStringConvertible {
    
    case appearance
    case myShows
    case toWatch
    case specials
    
    var description: String {
        switch self {
        case .appearance: return "Appearance"
        case .myShows: return "My Shows"
        case .toWatch: return "To Watch"
        case .specials: return "Specials"
        }
    }
    
    var index: Int {
        PreferenceSection.allSections.firstIndex(of: self) ?? -1
    }
    
    static var allSections: [PreferenceSection] {
        if #available(iOS 13, *) {
            return [.appearance, .myShows, .toWatch, .specials]
        } else {
            return [.myShows, .toWatch, .specials]
        }
    }
    
    static func atIndex(_ index: Int) -> PreferenceSection {
        return allSections[index]
    }
}
