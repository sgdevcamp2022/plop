import UIKit

final class PlopOrangeButton: UIButton {
  override var isEnabled: Bool {
    didSet {
      isEnabled ? (backgroundColor = UIConstants.plopColor) : (backgroundColor = .secondarySystemBackground)
    }
  }
  
  init(title: String) {
    super.init(frame: .zero)
    
    configure(title: title)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func configure(title: String) {
    setTitle(title, for: .normal)
    setTitleColor(.white, for: .normal)
    titleLabel?.font = .preferredFont(forTextStyle: .title3)
    layer.cornerRadius = 8
    layer.masksToBounds = true
  }
}
