enum SpecialsOption: Int, CaseIterable, CustomStringConvertible {

    case showSpecials
    case showSpecialsInToWatch
    
    var description: String {
        switch self {
        case .showSpecials: return "Show special episodes"
        case .showSpecialsInToWatch: return "Show specials in \"To Watch\" list"
        }
    }
}
