import UIKit

final class PlopGrayButton: UIButton {
  init(title: String) {
    super.init(frame: .zero)
    configure(title: title)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func configure(title: String) {
    setTitle(title, for: .normal)
    setTitleColor(.black, for: .normal)
    titleLabel?.font = .preferredFont(forTextStyle: .title3)
    backgroundColor = .secondarySystemBackground
    layer.cornerRadius = 8
    layer.masksToBounds = true
  }
}
