import UIKit

class MainViewController: UITabBarController, UITabBarControllerDelegate {
    
    private var previousSelectedViewController: UIViewController? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()
        delegate = self
    }
    
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        if (previousSelectedViewController == nil) || (previousSelectedViewController == viewController) {
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
