import UIKit

final class CreateChatRoomCell: UITableViewCell {
  
  static let reuseIdentifier = String(describing: CreateChatRoomCell.self)
  
  private let profileImageView = PlopProfileImageView(borderShape: .circle)
  private let nameLabel = UILabel()
  private let stackView = UIStackView()
   
  weak var delegate: AddFriendCellDelegate?
  
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

extension CreateChatRoomCell {
  private func configureUI() {
    selectionStyle = .none
    stackView.axis = .horizontal
    stackView.distribution = .fill
    stackView.spacing = 8
    
    nameLabel.font = .preferredFont(forTextStyle: .headline)
    nameLabel.textColor = .label
    nameLabel.numberOfLines = 1
  }
  
  private func layout() {
    profileImageView.translatesAutoresizingMaskIntoConstraints = false
    nameLabel.translatesAutoresizingMaskIntoConstraints = false
    stackView.translatesAutoresizingMaskIntoConstraints = false
    
    nameLabel.setContentCompressionResistancePriority(
      .defaultHigh, for: .horizontal)
    
    profileImageView.heightAnchor.constraint(
      equalToConstant: 48).isActive = true
    profileImageView.widthAnchor.constraint(
      equalToConstant: 48).isActive = true
    
    stackView.addArrangedSubview(profileImageView)
    stackView.addArrangedSubview(nameLabel)
    
    contentView.addSubview(stackView)
    
    NSLayoutConstraint.activate([
      stackView.topAnchor.constraint(
        equalToSystemSpacingBelow: contentView.topAnchor,
        multiplier: 1),
      stackView.leadingAnchor.constraint(
        equalToSystemSpacingAfter: contentView.leadingAnchor,
        multiplier: 1),
      contentView.trailingAnchor.constraint(
        equalToSystemSpacingAfter: stackView.trailingAnchor,
        multiplier: 1),
      contentView.bottomAnchor.constraint(
        equalToSystemSpacingBelow: stackView.bottomAnchor,
        multiplier: 1),
    ])
  }
}
