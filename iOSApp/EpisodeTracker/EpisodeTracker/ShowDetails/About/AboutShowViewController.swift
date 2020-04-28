import UIKit
import MaterialComponents.MaterialRipple
import SharedCode

class AboutShowViewController: UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var genresCollectionView: UICollectionView!
    @IBOutlet weak var genresHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var overviewLabel: UILabel!
    @IBOutlet weak var trailersCollectionView: UICollectionView!
    @IBOutlet weak var trailersContainer: UIView!
    @IBOutlet weak var castCollectionView: UICollectionView!
    @IBOutlet weak var castContainer: UIView!
    @IBOutlet weak var recommendationsCollectionView: UICollectionView!
    @IBOutlet weak var recommendationsContainer: UIView!
    @IBOutlet weak var socialNetworksView: UIStackView!
    @IBOutlet weak var imdbBadge: ImdbBadge!
    @IBOutlet weak var homePageButton: IconButton!
    @IBOutlet weak var instagramButton: IconButton!
    @IBOutlet weak var facebookButton: IconButton!
    @IBOutlet weak var twitterButton: IconButton!
    
    lazy var refreshControl: UIRefreshControl = {
       let control = UIRefreshControl()
        control.addTarget(self, action: #selector(onRefreshRequested), for: .valueChanged)
        return control
    }()
    
    var showId: Int!
    let genresDataSource = GenresDataSource()
    let trailersDataSource = TrailersDataSource()
    let castDataSource = CastDataSource()
    var recommendationsDataSource: RecommendationsDataSource!
    
    var trailers = [TrailerViewModel]()
    var castMembers = [CastMemberViewModel]()
    var recommendations = [RecommendationViewModel]()
    
    /**
     * Returns `true` if scroll should be blocked (offset set to `0`), `false` otherwise.
     */
    var scrollCallback: ((_ offset: CGFloat) -> Bool)?
    var trailerTapCallback: ((_ trailer: TrailerViewModel) -> Void)? {
        didSet {
            trailersDataSource.trailerTapCallback = self.trailerTapCallback
        }
    }
    var castMemberTapCallback: ((_ castMember: CastMemberViewModel) -> Void)?
    var recommendationTapCallback: ((_ recommendation: RecommendationViewModel) -> Void)?
    var refreshRequestedCallback: (() -> Void)?
    
    private let rippleController = MDCRippleTouchController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        recommendationsDataSource = RecommendationsDataSource()
        
        genresCollectionView.dataSource = genresDataSource
        
        trailersCollectionView.dataSource = trailersDataSource
        trailersCollectionView.delegate = self
        trailersContainer.isHidden = true
        
        castCollectionView.dataSource = castDataSource
        castCollectionView.delegate = self
        castContainer.isHidden = true
        
        recommendationsCollectionView.dataSource = recommendationsDataSource
        recommendationsCollectionView.delegate = self
        recommendationsContainer.isHidden = true
        
        scrollView.refreshControl = refreshControl
    }
    
    func displayShowDetails(_ show: ShowDetailsViewModel) {
        genresDataSource.genres = show.genres
        genresCollectionView.reloadData()
        genresHeightConstraint.constant = show.genres.isEmpty ? 0 : 45
        
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
        
        socialNetworksView.isHidden = imdbBadge.isHidden && homePageButton.isHidden && instagramButton.isHidden && facebookButton.isHidden && twitterButton.isHidden
    }
    
    func displayTrailers(_ trailers: [TrailerViewModel]) {
        self.trailers = trailers
        trailersDataSource.trailers = trailers
        trailersCollectionView.reloadData()
        trailersContainer.isHidden = trailers.isEmpty
    }
    
    func displayCast(_ castMembers: [CastMemberViewModel]) {
        self.castMembers = castMembers
        castDataSource.castMembers = castMembers
        castCollectionView.reloadData()
        castContainer.isHidden = castMembers.isEmpty
    }
    
    func displayRecommendations(_ recommendations: [RecommendationViewModel]) {
        self.recommendations = recommendations
        recommendationsDataSource.recommendations = recommendations
        recommendationsCollectionView.reloadData()
        recommendationsContainer.isHidden = recommendations.isEmpty
    }
    
    func displayImdbRating(_ rating: Float) {
        imdbBadge.rating = rating
    }
    
    func openRecommendation(_ show: RecommendationViewModel) {
        let vc = ShowDetailsViewController.instantiate(showId: show.showId.int, showName: show.name)
        navigationController?.pushViewController(vc, animated: true)
    }
    
    func setBottomInset(_ inset: CGFloat) {
        scrollView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: inset, right: 0)
    }
    
    func hideRefreshProgress() {
        refreshControl.endRefreshing()
    }
    
    @objc func onRefreshRequested() {
        refreshRequestedCallback?()
    }
}

// MARK: - UICollectionViews delegate
extension AboutShowViewController: UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        switch collectionView {
        case trailersCollectionView:
            trailerSelectedAt(indexPath)
        case castCollectionView:
            castMemberSelectedAt(indexPath)
        case recommendationsCollectionView:
            recommendationSelectedAt(indexPath)
        default:
            break
        }
    }
    
    private func trailerSelectedAt(_ indexPath: IndexPath) {
        let trailer = trailers[indexPath.row]
        trailerTapCallback?(trailer)
    }
    
    private func castMemberSelectedAt(_ indexPath: IndexPath) {
        let castMember = castMembers[indexPath.row]
        castMemberTapCallback?(castMember)
    }
    
    private func recommendationSelectedAt(_ indexPath: IndexPath) {
        let recommendation = recommendations[indexPath.row]
        recommendationTapCallback?(recommendation)
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
class TrailersDataSource: NSObject, UICollectionViewDataSource {
    
    var trailers = [TrailerViewModel]()
    var trailerTapCallback: ((_ trailer: TrailerViewModel) -> Void)?
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return trailers.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "trailer_cell", for: indexPath) as! TrailerCell
        let trailer = trailers[indexPath.row]
        
        cell.previewImageView.imageUrl = trailer.previewImageUrl
        cell.nameLabel.text = trailer.name
        cell.playButton.tapCallback = {
            self.trailerTapCallback?(trailer)
        }
        
        return cell
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

// MARK: - Recommendations UICollectionView datasource
class RecommendationsDataSource: NSObject, UICollectionViewDataSource {
    
    var recommendations = [RecommendationViewModel]()
    
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
}
