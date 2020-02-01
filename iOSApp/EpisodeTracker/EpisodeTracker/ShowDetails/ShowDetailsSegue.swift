import UIKit

extension UIStoryboardSegue {
    
    func setShowDetailsParameters(_ showId: Int, openEpisodesTabOnStart: Bool) {
        let vc = destination as! ShowDetailsViewController
        vc.showId = showId
        vc.openEpisodesTabOnStart = openEpisodesTabOnStart
    }
}
