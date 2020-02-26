import UIKit
import MaterialComponents.MaterialTabs
import MaterialComponents.MaterialButtons
import SharedCode

class ShowDetailsViewController: UIViewController {
    
    static func instantiate(showId: Int, openEpisodesTabOnStart: Bool) -> ShowDetailsViewController {
        let storyboard = UIStoryboard(name: "ShowDetails", bundle: Bundle.main)
        let vc = storyboard.instantiateInitialViewController() as! ShowDetailsViewController
        vc.setParameters(showId: Int(showId), openEpisodesTabOnStart: openEpisodesTabOnStart)
        return vc
    }
    
    private var showId: Int!
    private var openEpisodesTabOnStart: Bool!
    private var presenter: ShowDetailsPresenter!
    
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var imageView: ImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var subheadLabel: UILabel!
    @IBOutlet weak var ratingLabel: UILabel!
    @IBOutlet weak var tabBar: MDCTabBar!
    @IBOutlet weak var aboutView: UIView!
    @IBOutlet weak var episodesView: UIView!
    @IBOutlet weak var dismissButton: UIButton!
    @IBOutlet weak var addButton: FloatingButton!
    @IBOutlet weak var addButtonBottomConstraint: NSLayoutConstraint!
    
    private func setParameters(showId: Int, openEpisodesTabOnStart: Bool) {
        self.showId = showId
        self.openEpisodesTabOnStart = openEpisodesTabOnStart
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
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
        
        dismissButton.imageView?.tintColor = .textColorPrimaryInverse
        
        addButton.mode = .expanded
        
        if let bottomInset = UIApplication.shared.keyWindow?.safeAreaInsets.bottom {
            addButtonBottomConstraint.constant = bottomInset > 0 ? 0 : 16
        }
        
        if openEpisodesTabOnStart {
            showEpisodesTab()
        } else {
            showAboutTab()
        }
        
        showActivityIndicator()
        
        presenter = ShowDetailsPresenter(
            showId: Int32(showId),
            myShowsRepository: AppDelegate.instance().myShowsRepository,
            showRepository: AppDelegate.instance().showRepository
        )
        presenter.attachView(view: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        get { .lightContent }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "about_view":
            (segue.destination as! AboutShowViewController).showId = showId
            break
        case "episodes_view":
            (segue.destination as! EpisodesViewController).showId = showId
            break
        default:
            break
        }
    }
    
    @IBAction func onDismissTapped(_ sender: Any) {
        close()
    }
    
    @IBAction func addToMyShowsTapped(_ sender: Any) {
        presenter.onAddToMyShowsButtonClicked()
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
    
    private func showActivityIndicator() {
        activityIndicator.startAnimating()
        contentView.isHidden = true
        dismissButton.imageView?.tintColor = .textColorPrimary
    }
    
    private func hideActivityIndicator() {
        activityIndicator.stopAnimating()
        contentView.isHidden = false
        dismissButton.imageView?.tintColor = .textColorPrimaryInverse
    }
}

extension ShowDetailsViewController: ShowDetailsView {
    
    func displayShowHeader(show: ShowHeaderViewModel) {
        nameLabel.text = show.name
        imageView.imageUrl = show.imageUrl
        subheadLabel.text = show.subhead
        ratingLabel.text = show.rating
        contentView.isHidden = false
        hideActivityIndicator()
    }
    
    func displayAddToMyShowsButton() {
        addButton.isHidden = false
        addButton.isEnabled = true
        addButton.isActivityIndicatorHidden = true
    }
    
    func displayAddToMyShowsProgress() {
        addButton.isActivityIndicatorHidden = false
        addButton.isEnabled = false
    }
    
    func hideAddToMyShowsButton() {
        addButton.isActivityIndicatorHidden = true
        addButton.isHidden = true
    }
    
    func close() {
        navigationController?.popViewController(animated: true)
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
