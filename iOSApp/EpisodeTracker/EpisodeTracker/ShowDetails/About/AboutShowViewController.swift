import UIKit
import SharedCode

class AboutShowViewController: UIViewController {
    
    @IBOutlet weak var genresCollectionView: UICollectionView!
    
    var showId: Int!
    var presenter: AboutShowPresenter!
    var genres = [String]()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = AboutShowPresenter(showId: Int32(showId), repository: AppDelegate.instance().myShowsRepository)
        presenter.attachView(view: self)
    }
}

// MARK: - AboutShowView implementation
extension AboutShowViewController: AboutShowView {
    func displayShowDetails(show: ShowDetailsViewModel) {
        genres = show.genres
        genresCollectionView.reloadData()
    }
}

// MARK: - UICollectionView datasource and delegate
extension AboutShowViewController: UICollectionViewDataSource, UICollectionViewDelegate {
    
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
