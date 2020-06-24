enum CreditsOption: Int, CaseIterable, CustomStringConvertible {
    
    case tmdb
    
    var description: String {
        switch self {
        case .tmdb: return string(R.str.prefs_tmdb_attribution)
        }
    }
}
