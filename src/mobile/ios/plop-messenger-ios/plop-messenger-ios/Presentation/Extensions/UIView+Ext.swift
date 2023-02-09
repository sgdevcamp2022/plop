import UIKit

extension UIView {
  func fillSuperview(withConstant constant: CGFloat = 0) {
    guard let superview = superview else { return }
    translatesAutoresizingMaskIntoConstraints = false
    NSLayoutConstraint.activate(
      [leadingAnchor.constraint(
        equalTo: superview.leadingAnchor, constant: constant),
       topAnchor.constraint(
        equalTo: superview.topAnchor, constant: constant),
       trailingAnchor.constraint(
        equalTo: superview.trailingAnchor, constant: -constant),
       bottomAnchor.constraint(
        equalTo: superview.bottomAnchor, constant: -constant)]
    )
  }
}
