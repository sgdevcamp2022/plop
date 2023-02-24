import UIKit

final class PlopTextField: UITextField {
  init(placeholder: String, isSecure: Bool) {
    super.init(frame: .zero)
    configure(placeholder: placeholder, isSecure: isSecure)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func configure(placeholder: String, isSecure: Bool) {
    self.textColor = .label
    self.backgroundColor = .secondarySystemBackground
    self.layer.cornerRadius = 8
    self.layer.masksToBounds = true
    self.leftView = UIView(frame: CGRect(
      x: 0, y: 0, width: 10, height: 0))
    self.leftViewMode = .always
    self.placeholder = placeholder
    self.isSecureTextEntry = isSecure
    self.autocorrectionType = .no
    self.autocapitalizationType = .none
  }
}
