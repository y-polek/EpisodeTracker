import UIKit
import SharedCode

protocol MvpViewController: UIViewController {
    associatedtype P
    func createPresenter() -> P
}

class BaseViewController<V: Any, P: BasePresenter>: UIViewController, MvpViewController {
    
    func createPresenter() -> P {
        <#code#>
    }
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
}
