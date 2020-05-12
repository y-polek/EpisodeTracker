import UIKit

class MainViewController: UITabBarController, UITabBarControllerDelegate {
    
    private var previousSelectedViewController: UIViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        delegate = self
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        previousSelectedViewController = selectedViewController
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
