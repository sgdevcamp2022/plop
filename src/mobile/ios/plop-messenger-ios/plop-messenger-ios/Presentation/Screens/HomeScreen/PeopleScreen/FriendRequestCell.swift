import UIKit

protocol FriendRequestCellDelegate: AnyObject {
  func requestAccepted(_ user: User)
  func requestRejected(_ user: User)
}

final class FriendRequestCell: UITableViewCell {
  static let reuseIdentifier = String(describing: FriendRequestCell.self)
  
  private let profileImageView = PlopProfileImageView(
    borderShape: .circle
  )
  private let nameLabel = UILabel()
  private let acceptButton = PlopOrangeButton(title: "  수락  ")
  private let rejectButton = PlopGrayButton(title: "  거절  ")
  private let stackView = UIStackView()
  
  private var user: User? = nil
  weak var delegate: FriendRequestCellDelegate?
  
  override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
    super.init(style: style, reuseIdentifier: reuseIdentifier)
    configureUI()
    layout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func configureData(_ user: User) {
    self.user = user
    self.nameLabel.text = user.profile.nickname
  }
  
  @objc private func didTappedAccept() {
    guard let user = user else { return }
    delegate?.requestAccepted(user)
  }
  
  @objc private func didTappedReject() {
    guard let user = user else { return }
    delegate?.requestRejected(user)
  }
}

extension FriendRequestCell {
  private func configureUI() {
    selectionStyle = .none
    
    stackView.axis = .horizontal
    stackView.distribution = .fill
    stackView.spacing = 8
    
    nameLabel.font = .preferredFont(forTextStyle: .body)
    nameLabel.textColor = .label
    nameLabel.text = "Friend"
    nameLabel.numberOfLines = 1
    
    acceptButton.titleLabel?.font = .preferredFont(forTextStyle: .body)
    rejectButton.titleLabel?.font = .preferredFont(forTextStyle: .body)
    
    acceptButton.isEnabled = true
    
    acceptButton.addTarget(
      self,
      action: #selector(didTappedAccept),
      for: .touchUpInside)
    rejectButton.addTarget(
      self,
      action: #selector(didTappedReject),
      for: .touchUpInside)
  }
  
  private func layout() {
    profileImageView.translatesAutoresizingMaskIntoConstraints = false
    nameLabel.translatesAutoresizingMaskIntoConstraints = false
    acceptButton.translatesAutoresizingMaskIntoConstraints = false
    rejectButton.translatesAutoresizingMaskIntoConstraints = false
    
    stackView.translatesAutoresizingMaskIntoConstraints = false
    
    nameLabel.setContentCompressionResistancePriority(
      .defaultHigh,
      for: .horizontal)
    
    profileImageView.heightAnchor.constraint(
      equalToConstant: 48).isActive = true
    profileImageView.widthAnchor.constraint(
      equalToConstant: 48).isActive = true
    
    stackView.addArrangedSubview(profileImageView)
    stackView.addArrangedSubview(nameLabel)
    stackView.addArrangedSubview(acceptButton)
    stackView.addArrangedSubview(rejectButton)
    
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
