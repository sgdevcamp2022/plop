import UIKit

final class FriendCell: UITableViewCell {
  static let reuseIdentifier = String(describing: FriendCell.self)
  
  private let profileImageView = PlopProfileImageView(borderShape: .circle)
  private let nameLabel = UILabel()
  private let stackView = UIStackView()
  
  override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
    super.init(style: style, reuseIdentifier: reuseIdentifier)
    configureUI()
    layout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func configureData(_ user: User) {
    nameLabel.text = user.profile.nickname
  }
}

extension FriendCell {
  private func configureUI() {
    selectionStyle = .none
    
    stackView.axis = .horizontal
    stackView.distribution = .fill
    stackView.spacing = 8
    
    nameLabel.font = .preferredFont(forTextStyle: .body)
    nameLabel.textColor = .label
    nameLabel.text = "Friend"
    nameLabel.numberOfLines = 1
  }
  
  private func layout() {
    profileImageView.translatesAutoresizingMaskIntoConstraints = false
    nameLabel.translatesAutoresizingMaskIntoConstraints = false

    stackView.translatesAutoresizingMaskIntoConstraints = false
    
    nameLabel.setContentCompressionResistancePriority(.defaultHigh,
                                                      for: .horizontal)
    
    profileImageView.heightAnchor.constraint(
      equalToConstant: 48).isActive = true
    profileImageView.widthAnchor.constraint(
      equalToConstant: 48).isActive = true
    
    stackView.addArrangedSubview(profileImageView)
    stackView.addArrangedSubview(nameLabel)
    
    contentView.addSubview(stackView)
    
    NSLayoutConstraint.activate([
      stackView.topAnchor.constraint(
        equalTo: contentView.topAnchor,
        constant: 8),
      stackView.leadingAnchor.constraint(
        equalTo: contentView.leadingAnchor,
        constant: 8),
      stackView.trailingAnchor.constraint(
        equalTo: contentView.trailingAnchor,
        constant: -8),
      stackView.bottomAnchor.constraint(
        equalTo: contentView.bottomAnchor,
        constant: -8),
    ])
  }
}
