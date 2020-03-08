import UIKit
import MaterialComponents.MaterialRipple
import SharedCode

class AboutShowViewController: UIViewController, UICollectionViewDelegate {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var genresCollectionView: UICollectionView!
    @IBOutlet weak var overviewLabel: UILabel!
    @IBOutlet weak var trailersCollectionView: UICollectionView!
    @IBOutlet weak var trailersContainer: UIView!
    @IBOutlet weak var castCollectionView: UICollectionView!
    @IBOutlet weak var castContainer: UIView!
    @IBOutlet weak var recommendationsCollectionView: UICollectionView!
    @IBOutlet weak var recommendationsContainer: UIView!
    @IBOutlet weak var imdbBadge: ImdbBadge!
    @IBOutlet weak var homePageButton: IconButton!
    @IBOutlet weak var instagramButton: IconButton!
    @IBOutlet weak var facebookButton: IconButton!
    @IBOutlet weak var twitterButton: IconButton!
    
    var showId: Int!
    var presenter: AboutShowPresenter!
    let genresDataSource = GenresDataSource()
    let trailersDataSource = TrailersDataSource()
    let castDataSource = CastDataSource()
    var recommendationsDelegate: RecommendationsDelegate!
    
    /**
     * Returns `true` if scroll should be blocked (offset set to `0`), `false` otherwise.
     */
    var scrollCallback: ((_ offset: CGFloat) -> Bool)?
    
    private let rippleController = MDCRippleTouchController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter = AboutShowPresenter(
            showId: Int32(showId),
            myShowsRepository: AppDelegate.instance().myShowsRepository,
            showRepository: AppDelegate.instance().showRepository)
        
        recommendationsDelegate = RecommendationsDelegate(presenter)
        
        genresCollectionView.dataSource = genresDataSource
        
        trailersCollectionView.dataSource = trailersDataSource
        trailersCollectionView.delegate = trailersDataSource
        trailersContainer.isHidden = true
        
        castCollectionView.dataSource = castDataSource
        castCollectionView.delegate = castDataSource
        castContainer.isHidden = true
        
        recommendationsCollectionView.dataSource = recommendationsDelegate
        recommendationsCollectionView.delegate = recommendationsDelegate
        recommendationsContainer.isHidden = true
        
        showActivityIndicator()
        
        presenter.attachView(view: self)
    }
    
    func setBottomInset(_ inset: CGFloat) {
        scrollView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: inset, right: 0)
    }
    
    private func showActivityIndicator() {
        scrollView.isHidden = true
        activityIndicator.startAnimating()
    }
    
    private func hideActivityIndicator() {
        scrollView.isHidden = false
        activityIndicator.stopAnimating()
    }
}

// MARK: - AboutShowView implementation
extension AboutShowViewController: AboutShowView {
    
    func displayShowDetails(show: ShowDetailsViewModel) {
        hideActivityIndicator()
        
        genresDataSource.genres = show.genres
        genresCollectionView.reloadData()
        overviewLabel.text = show.overview
        
        imdbBadge.isHidden = show.imdbUrl == nil
        imdbBadge.tapCallback = {
            show.imdbUrl?.toUrl()?.open()
        }
        
        homePageButton.isHidden = show.homePageUrl == nil
        homePageButton.tapCallback = {
            show.homePageUrl?.toUrl()?.open()
        }
        
        instagramButton.isHidden = show.instagramUsername == nil
        instagramButton.tapCallback = {
            let appUrl = "instagram://user?username=\(show.instagramUsername!)".toUrl()!
            if appUrl.canBeOpen() {
                appUrl.open()
            } else {
                show.instagramUrl?.toUrl()?.open()
            }
        }
        
        facebookButton.isHidden = show.facebookProfile == nil
        facebookButton.tapCallback = {
            show.facebookUrl?.toUrl()?.open()
        }
        
        twitterButton.isHidden = show.twitterUsername == nil
        twitterButton.tapCallback = {
            let appUrl = "twitter://user?screen_name=\(show.twitterUsername!)".toUrl()!
            if appUrl.canBeOpen() {
                appUrl.open()
            } else {
                show.twitterUrl?.toUrl()?.open()
            }
        }
    }
    
    func displayTrailers(trailers: [TrailerViewModel]) {
        trailersDataSource.trailers = trailers
        trailersCollectionView.reloadData()
        trailersContainer.isHidden = trailers.isEmpty
    }
    
    func displayCast(cast: [CastMemberViewModel]) {
        castDataSource.castMembers = cast
        castCollectionView.reloadData()
        castContainer.isHidden = cast.isEmpty
    }
    
    func displayRecommendations(recommendations: [RecommendationViewModel]) {
        recommendationsDelegate.recommendations = recommendations
        recommendationsCollectionView.reloadData()
        recommendationsContainer.isHidden = recommendations.isEmpty
    }
    
    func displayImdbRating(rating: Float) {
        imdbBadge.rating = rating
    }
    
    func openRecommendation(showId: Int32) {
        let vc = ShowDetailsViewController.instantiate(showId: Int(showId))
        navigationController?.pushViewController(vc, animated: true)
    }
}

// MARK: - UIScrollView delegate
extension AboutShowViewController: UIScrollViewDelegate {

    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollCallback == nil { return }
        
        let offset = scrollView.contentOffset.y
        let blockScroll = scrollCallback!(offset)
        if blockScroll {
            scrollView.contentOffset.y = 0
        }
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
    
    var trailers = [TrailerViewModel]()
    
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
    
    private func openTrailer(_ trailer: TrailerViewModel) {
        var url: URL = URL(string: "youtube://\(trailer.youtubeKey)")!
        if !url.canBeOpen() {
            url = URL(string: trailer.url)!
        }
        url.open()
    }
}

// MARK: - Cast UICollectionView datasource
class CastDataSource: NSObject, UICollectionViewDataSource, UICollectionViewDelegate {
    
    var castMembers = [CastMemberViewModel]()
    
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

// MARK: - Recommendations UICollectionView delegate
class RecommendationsDelegate: NSObject, UICollectionViewDataSource, UICollectionViewDelegate {
    
    let presenter: AboutShowPresenter
    var recommendations = [RecommendationViewModel]()
    
    init(_ presenter: AboutShowPresenter) {
        self.presenter = presenter
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return recommendations.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "recommendation_cell", for: indexPath) as! RecommendationsCell
        let show = recommendations[indexPath.row]
        
        cell.backdropImageView.imageUrl = show.imageUrl
        cell.nameLabel.text = show.name
        cell.subheadLabel.text = show.subhead
        cell.subheadLabel.isHidden = show.subhead.isEmpty
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let recommendation = recommendations[indexPath.row]
        presenter.onRecommendationClicked(recommendation: recommendation)
    }
}
