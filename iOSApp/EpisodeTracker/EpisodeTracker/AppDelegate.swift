import UIKit
import Kingfisher
import SharedCode

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    let database: Database = DatabaseKt.database
    let preferences: Preferences = Preferences(settings: SettingsKt.settings)
    let tmdbService: TmdbService = TmdbService()
    let omdbService: OmdbService = OmdbService()
    let connectivity: Connectivity = ConnectivityImpl()
    lazy var showRepository: ShowRepository = ShowRepository(tmdbService: tmdbService, omdbService: omdbService, db: database)
    lazy var addToMyShowsQueue: AddToMyShowsQueue = AddToMyShowsQueue(db: database, tmdbService: tmdbService, connectivity: connectivity, showRepository: showRepository)
    lazy var myShowsRepository: MyShowsRepository = MyShowsRepository(db: database, addToMyShowsQueue: addToMyShowsQueue)
    lazy var discoverRepository: DiscoverRepository = DiscoverRepository(tmdbService: tmdbService)
    lazy var toWatchRepository: ToWatchRepository = ToWatchRepository(db: database)
    lazy var episodesRepository: EpisodesRepository = EpisodesRepository(db: database)

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        ImageCache.default.memoryStorage.config.totalCostLimit = 50 * 1024 * 1024
        ImageCache.default.memoryStorage.config.expiration = .never
        
        
        return true
    }
    
    @available(iOS 13.0, *)
    func setAppearance(_ appearance: Appearance) {
        var style: UIUserInterfaceStyle
        switch appearance {
        case .automatic:
            style = .unspecified
        case .light:
            style = .light
        case .dark:
            style = .dark
        default:
            fatalError("Unknown appearance: \(appearance)")
        }
        window?.overrideUserInterfaceStyle = style
    }

    static func instance() -> AppDelegate {
        return UIApplication.shared.delegate as! AppDelegate
    }
}

