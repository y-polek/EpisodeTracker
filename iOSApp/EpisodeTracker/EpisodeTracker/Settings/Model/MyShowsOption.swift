enum MyShowsOption: Int, CaseIterable, CustomStringConvertible {
    
    case showLastWeekSection
    
    var description: String {
        switch self {
        case .showLastWeekSection: return "Show \"Last Week\" section"
        }
    }
}
