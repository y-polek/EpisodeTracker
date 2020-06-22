enum ToWatchOption: Int, CaseIterable, CustomStringConvertible {
    
    case showBadge
    
    var description: String {
        switch self {
        case .showBadge: return string(R.str.prefs_show_badge)
        }
    }
}
