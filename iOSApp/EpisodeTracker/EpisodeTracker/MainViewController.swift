import UIKit
import SharedCode

class MainViewController: UITabBarController, UITabBarControllerDelegate {
    
    private var previousSelectedViewController: UIViewController!
    private var toWatchTabItem: UITabBarItem!
    private let presenter = MainPresenter(
        toWatchRepository: AppDelegate.instance().toWatchRepository,
        preferences: AppDelegate.instance().preferences)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        delegate = self
        
        toWatchTabItem = tabBar.items![1]
        
        presenter.attachView(view: self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presenter.onViewAppeared()
        
        previousSelectedViewController = selectedViewController
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        presenter.onViewDisappeared()
    }
    
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        if previousSelectedViewController == viewController {
            scrollToTop(viewController)
        }
        previousSelectedViewController = viewController
    }
    
    private func scrollToTop(_ viewController: UIViewController) {
        if viewController is Scrollable {
            (viewController as! Scrollable).scrollToTop()
        } else if viewController is UINavigationController {
            ((viewController as! UINavigationController).topViewController as? Scrollable)?.scrollToTop()
        }
    }
}

// MARK: - MainView implementation
extension MainViewController: MainView {
    
    func showToWatchBadge(text: String) {
        toWatchTabItem.badgeValue = text
    }
    
    func hideToWatchBadge() {
        toWatchTabItem.badgeValue = nil
    }
}
