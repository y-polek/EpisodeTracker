import UIKit
import SharedCode

class DiscoverViewController: UIViewController {
    
    private let results: [DiscoverResultViewModel] = [
        DiscoverResultViewModel(
            name: "Star Trek",
            year: 1966,
            network: "NBC",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/3ATqzWYDbWOV2RBLWNwA43InT60.jpg",
            overview: "Space. The Final Frontier. The U.S.S. Enterprise embarks on a five year mission to explore the galaxy. The Enterprise is under the command of Captain James T. Kirk with First Officer Mr. Spock, from the planet Vulcan.",
            isInMyShows: false),
        DiscoverResultViewModel(
            name: "Star Trek: Discovery",
            year: 2017, network: "CBS All Access",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/ihvG9dCEnVU3gmMUftTkRICNdJf.jpg",
            overview: "Follow the voyages of Starfleet on their missions to discover new worlds and new life forms, and one Starfleet officer who must learn that to truly understand all things alien, you must first understand yourself.",
            isInMyShows: true),
        DiscoverResultViewModel(
            name: "Star Trek: Voyager",
            year: 1995,
            network: "UPN",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/tqUH9uyIKuW7gMTCWh56w1Z5hRY.jpg",
            overview: "Pulled to the far side of the galaxy, where the Federation is 75 years away at maximum warp speed, a Starfleet ship must cooperate with Maquis rebels to find a way home.",
            isInMyShows: false),
        DiscoverResultViewModel(
            name: "Star Trek: Enterprise",
            year: 2001,
            network: "UPN",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/3msta8aYp2309onnFkVXeohbN4w.jpg",
            overview: "During the mid-22nd century, a century before Captain Kirk's five-year mission, Jonathan Archer captains the United Earth ship Enterprise during the early years of Starfleet, leading up to the Earth-Romulan War and the formation of the Federation.",
            isInMyShows: false),
        DiscoverResultViewModel(
            name: "Star Trek: The Next Generation",
            year: 1987, network: "Syndication",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/8ZfPKg1xTahEigpDA53bRmmLdvs.jpg",
            overview: "Follow the intergalactic adventures of Capt. Jean-Luc Picard and his loyal crew aboard the all-new USS Enterprise NCC-1701D, as they explore new worlds.",
            isInMyShows: false),
        DiscoverResultViewModel(
            name: "Star Trek: Deep Space Nine",
            year: 1993,
            network: "UPN",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/amrjHOaXxkeVgSrVS0CVmqb3xhR.jpg",
            overview: "At Deep Space Nine, a space station located next to a wormhole in the vicinity of the liberated planet of Bajor, Commander Sisko and crew welcome alien visitors, root out evildoers and solve all types of unexpected problems that come their way",
            isInMyShows: false),
        DiscoverResultViewModel(
            name: "Star Trek: Short Treks",
            year: 2018,
            network: "CBS All Access",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/nxwjl9rahHCdqEykjynxOEWgQMo.jpg",
            overview: "A series of stand-alone short stories expanding Star Trek: Discovery and the Star Trek franchise.",
            isInMyShows: false),
        DiscoverResultViewModel(
            name: "Star Trek: Picard",
            year: 2020,
            network: "CBS All Access",
            posterUrl: "https://image.tmdb.org/t/p/w440_and_h660_face/nIlAKIrLKxOeoEnc0Urb65yNCp.jpg",
            overview: "A new chapter in Jean-Luc Picard's life set twenty years after the events of Star Trek Nemesis.",
            isInMyShows: true)
    ]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
}

extension DiscoverViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "discover_cell", for: indexPath) as! DiscoverResultCell
        cell.bind(result: results[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
}
