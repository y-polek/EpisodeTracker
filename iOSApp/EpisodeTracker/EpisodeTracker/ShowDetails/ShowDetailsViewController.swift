import UIKit
import MaterialComponents.MaterialTabs
import MaterialComponents.MaterialButtons
import SharedCode

class ShowDetailsViewController: UIViewController {
    
    static func instantiate(showId: Int, openEpisodesTabOnStart: Bool = false) -> ShowDetailsViewController {
        let storyboard = UIStoryboard(name: "ShowDetails", bundle: Bundle.main)
        let vc = storyboard.instantiateInitialViewController() as! ShowDetailsViewController
        vc.setParameters(showId: Int(showId), openEpisodesTabOnStart: openEpisodesTabOnStart)
        return vc
    }
    
    private var showId: Int!
    private var openEpisodesTabOnStart: Bool!
    private var presenter: ShowDetailsPresenter!
    private var aboutShowViewController: AboutShowViewController?
    private var episodesViewController: EpisodesViewController?
    
    private let minHeaderHeight: CGFloat = 44 + UIApplication.shared.statusBarFrame.height
    private var maxHeaderHeight: CGFloat!
    
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var toolbar: UIView!
    @IBOutlet weak var imageView: ImageView!
    @IBOutlet weak var imageViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var headerLabelsContainer: UIView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var subheadLabel: UILabel!
    @IBOutlet weak var ratingLabel: UILabel!
    @IBOutlet weak var tabBar: MDCTabBar!
    @IBOutlet weak var aboutView: UIView!
    @IBOutlet weak var episodesView: UIView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var menuButton: UIButton!
    @IBOutlet weak var addButton: FloatingButton!
    @IBOutlet weak var addButtonBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var toolbarLabel: UILabel!
    
    private func setParameters(showId: Int, openEpisodesTabOnStart: Bool) {
        self.showId = showId
        self.openEpisodesTabOnStart = openEpisodesTabOnStart
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        maxHeaderHeight = imageView.bounds.width / 1.8
        imageViewHeightConstraint.constant = maxHeaderHeight
        imageView.overlayOpacity = [0.6, 0.4, 0.4, 0.6]
        imageView.isBlured = true
        imageView.blurAlpha = 0
        
        toolbarLabel.alpha = 0
        headerLabelsContainer.alpha = 1
        
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
        
        backButton.imageView?.tintColor = .textColorPrimaryInverse
        menuButton.imageView?.tintColor = .textColorPrimaryInverse
        
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
        
        aboutShowViewController?.scrollCallback = scrollCallback(offset:)
        episodesViewController?.scrollCallback = scrollCallback(offset:)
        
        presenter.onViewAppeared()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        aboutShowViewController?.scrollCallback = nil
        episodesViewController?.scrollCallback = nil
        
        presenter.onViewDisappeared()
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        get { .lightContent }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "about_view":
            aboutShowViewController = (segue.destination as! AboutShowViewController)
            aboutShowViewController!.showId = showId
            break
        case "episodes_view":
            episodesViewController = (segue.destination as! EpisodesViewController)
            episodesViewController!.showId = showId
            break
        default:
            break
        }
    }
    
    @IBAction func onBackTapped(_ sender: Any) {
        close()
    }
    
    @IBAction func onMenuTapped(_ sender: Any) {
        
    }
    
    @IBAction func addToMyShowsTapped(_ sender: Any) {
        presenter.onAddToMyShowsButtonClicked()
    }
    
    private func scrollCallback(offset: CGFloat) -> Bool {
        var newHeight = imageViewHeightConstraint.constant - offset
        var blockScroll = false
        
        if newHeight > maxHeaderHeight {
            newHeight = maxHeaderHeight
        } else if newHeight < minHeaderHeight {
            newHeight = minHeaderHeight
        } else {
            blockScroll = true
        }
        imageViewHeightConstraint.constant = newHeight
        
        let heightRatio = (newHeight - minHeaderHeight) / (maxHeaderHeight - minHeaderHeight)
        toolbarLabel.alpha = 1 - 2 * heightRatio
        headerLabelsContainer.alpha = 1 - 2 * (1 - heightRatio)
        imageView.blurAlpha = 1 - heightRatio

        return blockScroll
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
        backButton.imageView?.tintColor = .textColorPrimary
        menuButton.imageView?.tintColor = .textColorPrimary
    }
    
    private func hideActivityIndicator() {
        activityIndicator.stopAnimating()
        contentView.isHidden = false
        backButton.imageView?.tintColor = .textColorPrimaryInverse
        menuButton.imageView?.tintColor = .textColorPrimaryInverse
    }
    
    private func setBottomInset() {
        let inset = addButton.bounds.height + addButtonBottomConstraint.constant
        aboutShowViewController?.setBottomInset(inset)
        episodesViewController?.setBottomInset(inset)
    }
    
    private func removeBottomInset() {
        aboutShowViewController?.setBottomInset(0)
        episodesViewController?.setBottomInset(0)
    }
}

extension ShowDetailsViewController: ShowDetailsView {
    
    func displayShowHeader(show: ShowHeaderViewModel) {
        nameLabel.text = show.name
        toolbarLabel.text = show.name
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
        setBottomInset()
    }
    
    func displayAddToMyShowsProgress() {
        addButton.isActivityIndicatorHidden = false
        addButton.isEnabled = false
    }
    
    func hideAddToMyShowsButton() {
        addButton.isActivityIndicatorHidden = true
        addButton.isHidden = true
        removeBottomInset()
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
