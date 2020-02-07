import UIKit
import MaterialComponents.MDCTabBar
import SharedCode

class ShowDetailsViewController: UIViewController {
    
    var showId: Int!
    var openEpisodesTabOnStart: Bool!
    var presenter: ShowDetailsPresenter!
    
    @IBOutlet weak var imageView: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var yearsLabel: UILabel!
    @IBOutlet weak var ratingLabel: UILabel!
    @IBOutlet weak var tabBar: MDCTabBar!
    @IBOutlet weak var aboutView: UIView!
    @IBOutlet weak var episodesView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let navigationBar = navigationController?.navigationBar {
            navigationBar.setBackgroundImage(UIImage(), for: .default)
            navigationBar.shadowImage = UIImage()
            navigationBar.isTranslucent = true
        }
        
        imageView.overlayOpacity = [0.6, 0.2, 0.4, 0.6]
        
        tabBar.items = [
            UITabBarItem(title: "About", image: nil, tag: 0),
            UITabBarItem(title: "Episodes", image: nil, tag: 1)
        ]
        tabBar.itemAppearance = .titles
        tabBar.autoresizingMask = [.flexibleWidth, .flexibleBottomMargin]
        tabBar.sizeToFit()
        tabBar.bottomDividerColor = .dividerPrimary
        tabBar.setTitleColor(.textColorSecondary, for: .normal)
        tabBar.setTitleColor(.textColorPrimary, for: .selected)
        tabBar.selectedItemTitleFont = .systemFont(ofSize: 17)
        tabBar.unselectedItemTitleFont = .systemFont(ofSize: 17)
        tabBar.displaysUppercaseTitles = false
        tabBar.tintColor = .accent
        tabBar.inkColor = .transparent
        tabBar.alignment = .justified
        tabBar.delegate = self
        
        if openEpisodesTabOnStart {
            showEpisodesTab()
        } else {
            showAboutTab()
        }
        
        presenter = ShowDetailsPresenter(showId: Int32(showId), repository: AppDelegate.instance().myShowsRepository)
        presenter.attachView(view: self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "about_view":
            break
        case "episodes_view":
            (segue.destination as! EpisodesViewController).showId = showId
            break
        default:
            break
        }
    }
    
    @IBAction func onBackTapped(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    private func showAboutTab() {
        tabBar.setSelectedItem(tabBar.items[0], animated: false)
        aboutView.isHidden = false
        episodesView.isHidden = true
    }
    
    private func showEpisodesTab() {
        tabBar.setSelectedItem(tabBar.items[1], animated: false)
        episodesView.isHidden = false
        aboutView.isHidden = true
    }
}

extension ShowDetailsViewController: ShowDetailsView {
    
    func displayShowDetails(show: ShowDetailsViewModel) {
        nameLabel.text = show.name
        imageView.imageUrl = show.imageUrl
        yearsLabel.text = show.years
        ratingLabel.text = show.contentRating
    }
    
    func close() {
        dismiss(animated: true, completion: nil)
    }
}

extension ShowDetailsViewController: MDCTabBarDelegate {
    
    func tabBar(_ tabBar: MDCTabBar, didSelect item: UITabBarItem) {
        switch item.tag {
        case 0:
            showAboutTab()
        case 1:
            showEpisodesTab()
        default:
            break
        }
    }
    
}
