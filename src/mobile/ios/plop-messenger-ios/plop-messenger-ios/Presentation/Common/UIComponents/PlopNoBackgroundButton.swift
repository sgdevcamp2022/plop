import UIKit

final class PlopNoBackgroundButton: UIButton {
  init(title: String, titleColor: UIColor, font: UIFont.TextStyle) {
    super.init(frame: .zero)
    setTitle(title, for: .normal)
    setTitleColor(titleColor, for: .normal)
    titleLabel?.font = .preferredFont(forTextStyle: font)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}
