enum MyShowsOption: Int, CaseIterable, CustomStringConvertible {
    
    case showLastWeekSection
    
    var description: String {
        switch self {
        case .showLastWeekSection: return string(R.str.prefs_show_last_week)
        }
    }
}
