import UIKit

@IBDesignable
class ImageButton: UIView {
    
    @IBOutlet var contentView: UIView!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    @IBInspectable
    var image: UIImage? {
        didSet {
            updateImage()
        }
    }
    
    var isActivityIndicatorHidden: Bool = true {
        didSet {
            updateActivityIndicator()
        }
    }
    
    var tapCallback: (() -> Void)?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    private func setup() {
        Bundle(for: ImageButton.self).loadNibNamed("ImageButton", owner: self, options: nil)
        addSubview(contentView)
        
        contentView.frame = self.bounds
        contentView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        
        layer.borderWidth = 2
        layer.borderColor = UIColor.white.cgColor
        layer.cornerRadius = 2
        clipsToBounds = true
        
        imageView.tintColor = .white
        
        updateActivityIndicator()
        
        addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(onTapped(gesture:))))
    }
    
    private func updateImage() {
        imageView.image = image
    }
    
    private func updateActivityIndicator() {
        if isActivityIndicatorHidden {
            activityIndicator.stopAnimating()
            activityIndicator.isHidden = true
            imageView.isHidden = false
        } else {
            activityIndicator.startAnimating()
            activityIndicator.isHidden = false
            imageView.isHidden = true
        }
    }
    
    @objc private func onTapped(gesture: UIGestureRecognizer) {
        tapCallback?()
    }
}
