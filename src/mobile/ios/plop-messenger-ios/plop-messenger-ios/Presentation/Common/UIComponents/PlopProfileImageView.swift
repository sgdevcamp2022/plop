import UIKit

enum BorderShape: String {
  case circle
  case squircle
  case none
}

final class PlopProfileImageView: UIImageView {
  private let borderShape: BorderShape
  
  // MARK: - Initializers
  convenience init() {
    self.init(borderShape: .none)
  }
  
  init(borderShape: BorderShape) {
    self.borderShape = borderShape
    super.init(frame: CGRect.zero)
    commonInit()
  }
  
  required init?(coder aDecoder: NSCoder) {
    borderShape = .none
    super.init(coder: aDecoder)
    commonInit()
  }
  
  private func commonInit() {
    backgroundColor = .systemBackground
    contentMode = .scaleAspectFit
    clipsToBounds = true
    layer.masksToBounds = true
    image = UIImage(named: "base-profile")
  }
  
  // MARK: - Layouts
  override func layoutSubviews() {
    super.layoutSubviews()
    setupBorderShape()
  }
  
  private func setupBorderShape() {
    let width = bounds.size.width
    let divisor: CGFloat
    switch borderShape {
    case .circle:
      divisor = 2
    case .squircle:
      divisor = 4
    case .none:
      divisor = width
    }
    let cornerRadius = width / divisor
    layer.cornerRadius = cornerRadius
  }
}
