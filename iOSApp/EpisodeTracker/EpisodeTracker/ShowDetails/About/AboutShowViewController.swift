import UIKit
import SharedCode

class AboutShowViewController: UIViewController, UICollectionViewDelegate {
    
    @IBOutlet weak var genresCollectionView: UICollectionView!
    @IBOutlet weak var timeLabel: UILabel!
    @IBOutlet weak var overviewLabel: UILabel!
    @IBOutlet weak var trailersContainer: UIView!
    @IBOutlet weak var trailersCollectionView: UICollectionView!
    
    var showId: Int!
    var presenter: AboutShowPresenter!
    var genresDataSource = GenresDataSource()
    var trailersDataSource = TrailersDataSource()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        genresCollectionView.dataSource = genresDataSource
        trailersCollectionView.dataSource = trailersDataSource
        
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

// MARK: - Trailers UICollectionView datasource
class TrailersDataSource: NSObject, UICollectionViewDataSource {
    
    var trailers = [Trailer]()
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return trailers.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "trailer_cell", for: indexPath) as! TrailerCell
        let trailer = trailers[indexPath.row]
        
        cell.previewImageView.imageUrl = trailer.previewImageUrl
        cell.nameLabel.text = trailer.name
        
        return cell
    }
}
