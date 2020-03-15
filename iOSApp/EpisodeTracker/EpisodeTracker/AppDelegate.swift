import UIKit
import Kingfisher
import SharedCode

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    let database: Database = DatabaseKt.database
    let tmdbService: TmdbService = TmdbService()
    let omdbService: OmdbService = OmdbService()
    let connectivity: Connectivity = ConnectivityImpl()
    lazy var showRepository: ShowRepository = ShowRepository(tmdbService: tmdbService, omdbService: omdbService)
    lazy var myShowsRepository: MyShowsRepository = MyShowsRepository(db: database, tmdbService: tmdbService, showRepository: showRepository)
    lazy var discoverRepository: DiscoverRepository = DiscoverRepository(tmdbService: tmdbService)
    lazy var toWatchRepository: ToWatchRepository = ToWatchRepository(db: database)
    lazy var episodesRepository: EpisodesRepository = EpisodesRepository(db: database)

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        //ImageCache.default.clearDiskCache()
        
        
        return true
    }

    static func instance() -> AppDelegate {
        return UIApplication.shared.delegate as! AppDelegate
    }
}

