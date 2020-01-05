import UIKit
import Kingfisher
import SharedCode

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    let database: Database = DatabaseKt.database
    let tmdbService: TmdbService = TmdbService()
    lazy var myShowsRepository: MyShowsRepository = MyShowsRepository(db: database, tmdbService: tmdbService)
    lazy var discoverRepository = DiscoverRepository(tmdbService: tmdbService)

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        //ImageCache.default.clearDiskCache()
        
        
        return true
    }

    static func instance() -> AppDelegate {
        return UIApplication.shared.delegate as! AppDelegate
    }
}

