import UIKit
import SharedCode

class AboutShowViewController: UIViewController, UICollectionViewDelegate {
    
    @IBOutlet weak var genresCollectionView: UICollectionView!
    @IBOutlet weak var timeLabel: UILabel!
    @IBOutlet weak var overviewLabel: UILabel!
    @IBOutlet weak var trailersContainer: UIView!
    @IBOutlet weak var trailersCollectionView: UICollectionView!
    @IBOutlet weak var castCollectionView: UICollectionView!
    
    var showId: Int!
    var presenter: AboutShowPresenter!
    let genresDataSource = GenresDataSource()
    let trailersDataSource = TrailersDataSource()
    let castDataSource = CastDataSource()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        genresCollectionView.dataSource = genresDataSource
        trailersCollectionView.dataSource = trailersDataSource
        trailersCollectionView.delegate = trailersDataSource
        castCollectionView.dataSource = castDataSource
        
        presenter = AboutShowPresenter(
            showId: Int32(showId),
            myShowsRepository: AppDelegate.instance().myShowsRepository,
            showRepository: AppDelegate.instance().showRepository)
        presenter.attachView(view: self)
    }
}

// MARK: - AboutShowView implementation
extension AboutShowViewController: AboutShowView {
    
    func displayShowDetails(show: ShowDetailsViewModel) {
        genresDataSource.genres = show.genres
        genresCollectionView.reloadData()
        overviewLabel.text = show.overview
    }
    
    func displayTrailers(trailers: [Trailer]) {
        trailersDataSource.trailers = trailers
        trailersCollectionView.reloadData()
    }
    
    func displayCast(cast: [CastMember]) {
        castDataSource.castMembers = cast
        castCollectionView.reloadData()
    }
}

// MARK: - Genres UICollectionView datasource
class GenresDataSource: NSObject, UICollectionViewDataSource {
    
    var genres = [String]()
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return genres.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "genre_cell", for: indexPath) as! GenreCell
        let genre = genres[indexPath.row]
        cell.label.text = genre
        return cell
    }
}

// MARK: - Trailers UICollectionView datasource and delegate
class TrailersDataSource: NSObject, UICollectionViewDataSource, UICollectionViewDelegate {
    
    var trailers = [Trailer]()
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return trailers.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "trailer_cell", for: indexPath) as! TrailerCell
        let trailer = trailers[indexPath.row]
        
        cell.previewImageView.imageUrl = trailer.previewImageUrl
        cell.nameLabel.text = trailer.name
        cell.playButton.tapCallback = {
            self.openTrailer(trailer)
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let trailer = trailers[indexPath.row]
        openTrailer(trailer)
    }
    
    private func openTrailer(_ trailer: Trailer) {
        var url: URL = URL(string: "youtube://\(trailer.youtubeKey)")!
        if !UIApplication.shared.canOpenURL(url) {
            url = URL(string: trailer.url)!
        }
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
    }
}

// MARK: - Cast UICollectionView datasource
class CastDataSource: NSObject, UICollectionViewDataSource {
    
    var castMembers = [CastMember]()
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return castMembers.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cast_member_cell", for: indexPath) as! CastMemberCell
        let member = castMembers[indexPath.row]
        
        cell.portraitImageView.imageUrl = member.portraitImageUrl
        cell.actorNameLabel.text = member.name
        cell.characterNameLabel.text = member.character
        
        return cell
    }
}

