enum CreditsOption: Int, CaseIterable {
    
    case tmdb
    
    var attribution: String {
        switch self {
        case .tmdb: return string(R.str.prefs_tmdb_attribution)
        }
    }
    
    var logo: String {
        switch self {
        case .tmdb:
            return "ic-tmdb-logo"
        }
    }
}
