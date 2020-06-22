enum SpecialsOption: Int, CaseIterable, CustomStringConvertible {

    case showSpecials
    case showSpecialsInToWatch
    
    var description: String {
        switch self {
        case .showSpecials: return string(R.str.prefs_show_specials)
        case .showSpecialsInToWatch: return string(R.str.prefs_show_specials_in_to_watch)
        }
    }
}
