import UIKit

protocol AddFriendCellDelegate: AnyObject {
  func requestFriend(_ cell: AddFriendCell, _ email: String)
}

final class AddFriendCell: UITableViewCell {
  static let reuseIdentifier = String(describing: AddFriendCell.self)
  
  private let profileImageView = PlopProfileImageView(borderShape: .circle)
  private let nameLabel = UILabel()
  private let addFriendButton = PlopOrangeButton(title: " 친구 추가 ")
  private let stackView = UIStackView()
  
  var canRequest: Bool = true {
    didSet { setButtonState() }
  }
  
  private var userEmail = ""
  
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
    self.userEmail = user.email
  }
  
  @objc private func didTappedRequestButton(_ sender: UIButton) {
    delegate?.requestFriend(self, userEmail)
  }
  
  private func setButtonState() {
    if canRequest {
      self.addFriendButton.backgroundColor = UIConstants.plopColor
      self.addFriendButton.setTitle(" 친구 요청 ", for: .normal)
    } else {
      self.addFriendButton.backgroundColor = .systemGray
      self.addFriendButton.setTitle(" 요청됨 ", for: .normal)
      self.addFriendButton.tintColor = .label
    }
  }
}

extension AddFriendCell {
  private func configureUI() {
    selectionStyle = .none
    stackView.axis = .horizontal
    stackView.distribution = .fill
    stackView.spacing = 8
    
    nameLabel.font = .preferredFont(forTextStyle: .headline)
    nameLabel.textColor = .label
    nameLabel.numberOfLines = 1
    
    addFriendButton.isEnabled = true
    addFriendButton.titleLabel?.font = .preferredFont(forTextStyle: .body)
    addFriendButton.addTarget(
      self,
      action: #selector(didTappedRequestButton(_:)),
      for: .touchUpInside)
  }
  
  private func layout() {
    profileImageView.translatesAutoresizingMaskIntoConstraints = false
    nameLabel.translatesAutoresizingMaskIntoConstraints = false
    addFriendButton.translatesAutoresizingMaskIntoConstraints = false
    stackView.translatesAutoresizingMaskIntoConstraints = false
    
    nameLabel.setContentCompressionResistancePriority(
      .defaultHigh, for: .horizontal)
    
    profileImageView.heightAnchor.constraint(
      equalToConstant: 48).isActive = true
    profileImageView.widthAnchor.constraint(
      equalToConstant: 48).isActive = true
    
    stackView.addArrangedSubview(profileImageView)
    stackView.addArrangedSubview(nameLabel)
    stackView.addArrangedSubview(addFriendButton)
    
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
