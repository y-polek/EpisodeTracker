import UIKit

class SwitchPreferenceCell: UITableViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var checkbox: UISwitch!
    
    var isEnabled: Bool = true {
        didSet {
            nameLabel.isEnabled = isEnabled
            checkbox.isEnabled = isEnabled
        }
    }
    var switchCallback: ((_ isOn: Bool) -> Void)?
    
    @IBAction func onSwitched(_ sender: Any) {
        switchCallback?(checkbox.isOn)
    }
}
