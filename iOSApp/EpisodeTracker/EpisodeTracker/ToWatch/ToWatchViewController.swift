import UIKit
import IGListKit
import SharedCode

class ToWatchViewController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    private let presenter = ToWatchPresenter(repository: AppDelegate.instance().toWatchRepository)
    private var shows = [ToWatchShowViewModel]()
    private var adapter: ListAdapter!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.setNavigationBarHidden(true, animated: false)
        
        adapter = ListAdapter(updater: ListAdapterUpdater(), viewController: self)
        adapter.collectionView = collectionView
        adapter.dataSource = self
        
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
}

// MARK: - ToWatchView implementation
extension ToWatchViewController: ToWatchView {
    
    func displayShows(shows: [ToWatchShowViewModel]) {
        self.shows = shows
        adapter.performUpdates(animated: true, completion: nil)
    }
    
    func openToWatchShowDetails(show: ToWatchShowViewModel) {
        let vc = ShowDetailsViewController.instantiate(showId: show.id.int, showName: show.name, openEpisodesTabOnStart: true)
        navigationController?.pushViewController(vc, animated: true)
    }
}

extension ToWatchViewController: ListAdapterDataSource {
    
    func objects(for listAdapter: ListAdapter) -> [ListDiffable] {
        return shows
    }
    
    func listAdapter(_ listAdapter: ListAdapter, sectionControllerFor object: Any) -> ListSectionController {
        return ToWatchSectionController(object as! ToWatchShowViewModel, presenter)
    }
    
    func emptyView(for listAdapter: ListAdapter) -> UIView? {
        return nil
    }
}

class ToWatchSectionController: ListSectionController {
    
    private var show: ToWatchShowViewModel!
    private var presenter: ToWatchPresenter!
    
    init(_ show: ToWatchShowViewModel, _ presenter: ToWatchPresenter) {
        super.init()
        self.show = show
        self.presenter = presenter
    }
    
    override func sizeForItem(at index: Int) -> CGSize {
        return CGSize(width: collectionContext!.containerSize.width, height: 158)
    }
    
    override func cellForItem(at index: Int) -> UICollectionViewCell {
        let cell = collectionContext!.dequeueReusableCellFromStoryboard(
            withIdentifier: "to_watch_show_cell", for: self, at: index) as! ToWatchCell
        cell.bind(show)
        cell.checkButton.tapCallback = { [weak self] in
            if let presenter = self?.presenter, let show = self?.show {
                presenter.onWatchedButtonClicked(show: show)
            }
        }
        return cell
    }
    
    override func didUpdate(to object: Any) {
        show = (object as! ToWatchShowViewModel)
    }
    
    override func didSelectItem(at index: Int) {
        presenter.onShowClicked(show: show)
    }
}
