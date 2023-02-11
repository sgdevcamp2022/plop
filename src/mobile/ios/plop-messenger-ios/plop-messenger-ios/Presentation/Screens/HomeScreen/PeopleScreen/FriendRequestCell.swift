import UIKit

protocol FriendRequestCellDelegate: AnyObject {
  func didTappedAccept(_ indexPath: IndexPath)
  func didTappedCancel(_ indexPath: IndexPath)
}

final class FriendRequestCell: UITableViewCell {
  static let reuseIdentifier = String(describing: FriendRequestCell.self)
  
  private let profileImageView = PlopProfileImageView(borderShape: .circle)
  private let nameLabel = UILabel()
  private let acceptButton = PlopOrangeButton(title: "  수락  ")
  private let cancelButton = PlopGrayButton(title: "  거절  ")
  private let stackView = UIStackView()
  
  private var indexPath: IndexPath? = nil
  weak var delegate: FriendRequestCellDelegate?
  
  override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
    super.init(style: style, reuseIdentifier: reuseIdentifier)
    configureUI()
    layout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func configureData(_ friend: User, indexPath: IndexPath) {
    self.nameLabel.text = friend.profile.nickname
    self.indexPath = indexPath
  }
  
  @objc private func didTappedAccept() {
    guard let indexPath = indexPath else { return }
    delegate?.didTappedAccept(indexPath)
  }
  
  @objc private func didTappedCancel() {
    guard let indexPath = indexPath else { return }
    delegate?.didTappedCancel(indexPath)
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
    cancelButton.titleLabel?.font = .preferredFont(forTextStyle: .body)
    
    acceptButton.isEnabled = true
    
    acceptButton.addTarget(
      self,
      action: #selector(didTappedAccept),
      for: .touchUpInside)
    cancelButton.addTarget(
      self,
      action: #selector(didTappedCancel),
      for: .touchUpInside)
  }
  
  private func layout() {
    profileImageView.translatesAutoresizingMaskIntoConstraints = false
    nameLabel.translatesAutoresizingMaskIntoConstraints = false
    acceptButton.translatesAutoresizingMaskIntoConstraints = false
    cancelButton.translatesAutoresizingMaskIntoConstraints = false
    
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
    stackView.addArrangedSubview(cancelButton)
    
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
