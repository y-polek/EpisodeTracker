enum ToWatchOption: Int, CaseIterable, CustomStringConvertible {
    
    case showBadge
    
    var description: String {
        switch self {
        case .showBadge: return "Show number of episodes in a badge"
        }
    }
}
