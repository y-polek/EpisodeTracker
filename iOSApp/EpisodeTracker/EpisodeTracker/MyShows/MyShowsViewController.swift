import UIKit
import SharedCode

class MyShowsViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    
    private let model = MyShowsViewModel(
            upcomingShows: [
                MyShowsListItem.UpcomingShowViewModel(
                    name: "Star Trek: Discovery",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/rhE2h8WYJOPuBlMl20MQRnJw3aq.jpg",
                    episodeNumber: "S01 E01",
                    episodeName: "The Vulcan Hello",
                    timeLeft: "4 days"),
                MyShowsListItem.UpcomingShowViewModel(
                    name: "South Park",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/mSDKNVvDfitFE6Fb6fSSl5DQmgS.jpg",
                    episodeNumber: "S23 E01",
                    episodeName: "TBA",
                    timeLeft: "1 month"),
                MyShowsListItem.UpcomingShowViewModel(
                    name: "The Mandalorian",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/o7qi2v4uWQ8bZ1tW3KI0Ztn2epk.jpg",
                    episodeNumber: "S01 E06",
                    episodeName: "Chapter 6",
                    timeLeft: "1 day"),
                MyShowsListItem.UpcomingShowViewModel(
                    name: "Rick and Morty",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/mzzHr6g1yvZ05Mc7hNj3tUdy2bM.jpg",
                    episodeNumber: "S04 E05",
                    episodeName: "Rattlestar Ricklactica",
                    timeLeft: "3 days")
            ],
            toBeAnnouncedShows: [
                MyShowsListItem.ShowViewModel(
                    name: "The Big Bang Theory",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/nGsNruW3W27V6r4gkyc3iiEGsKR.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "The Boys",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/bI37vIHSH7o4IVkq37P8cfxQGMx.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "Doom Patrol",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/sAzw6I1G9JUxm86KokIDdQeWtaq.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "Love, Death & Robots",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/e7VzDMrYKXVrVon04Uqsrcgnf1k.jpg")
            ],
            endedShows: [
                MyShowsListItem.ShowViewModel(
                    name: "Game of Thrones",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/c0Qt5uorF3WHv9pMKhV5uprNyVl.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "Breaking Bad",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/eSzpy96DwBujGFj0xMbXBcGcfxX.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "Silicon Valley",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/zGPXkczhXYRmTCO6t0x2OHJf8jZ.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "Chernobyl",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/900tHlUYUkp7Ol04XFSoAaEIXcT.jpg"),
                MyShowsListItem.ShowViewModel(
                    name: "Preacher",
                    backdropUrl: "https://image.tmdb.org/t/p/w1066_and_h600_bestv2/aS8EVmpaTxGbZ5ANAIxs5SwYXnQ.jpg")
            ],
            isUpcomingExpanded: true,
            isToBeAnnouncedExpanded: true,
            isEndedExpanded: true)
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
}

extension MyShowsViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return model.isUpcomingExpanded ? model.upcomingShows.count : 0
        case 1:
            return model.isToBeAnnouncedExpanded ? model.toBeAnnouncedShows.count : 0
        case 2:
            return model.isEndedExpanded ? model.endedShows.count : 0
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let header = HeaderView()
        
        switch section {
        case 0:
            header.title = "Upcoming"
            header.isExpanded = model.isUpcomingExpanded
            header.tapCallback = {
                self.model.toggleUpcomingExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: 0), with: .automatic)
            }
        case 1:
            header.title = "To Be Announced"
            header.isExpanded = model.isToBeAnnouncedExpanded
            header.tapCallback = {
                self.model.toggleToBeAnnouncedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: 1), with: .automatic)
            }
        case 2:
            header.title = "Ended"
            header.isExpanded = model.isEndedExpanded
            header.tapCallback = {
                self.model.toggleEndedExpanded()
                tableView.reloadSections(IndexSet(arrayLiteral: 2), with: .automatic)
            }
        default:
            break
        }
        
        return header
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            return upcomingShowCell(tableView, indexPath)
        case 1:
            return toBeAnnouncedShowCell(tableView, indexPath)
        case 2:
            return endedShowCell(tableView, indexPath)
        default:
            fatalError("Unknown section #\(indexPath.section)")
        }
    }
    
    private func upcomingShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> UpcomingShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "upcoming_show_cell") as! UpcomingShowCell
        cell.bind(show: model.upcomingShows[indexPath.row])
        return cell
    }
    
    private func toBeAnnouncedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.bind(show: model.toBeAnnouncedShows[indexPath.row])
        return cell
    }
    
    private func endedShowCell(_ tableView: UITableView, _ indexPath: IndexPath) -> ShowCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "show_cell") as! ShowCell
        cell.bind(show: model.endedShows[indexPath.row])
        return cell
    }
}
