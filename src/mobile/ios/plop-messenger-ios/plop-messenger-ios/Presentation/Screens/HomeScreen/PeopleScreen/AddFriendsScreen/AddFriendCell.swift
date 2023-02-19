import UIKit

protocol AddFriendCellDelegate: AnyObject {
  func sendRequest(to user: User)
  func cancelRequest(to user: User)
}

final class AddFriendCell: UITableViewCell {
  static let reuseIdentifier = String(describing: AddFriendCell.self)
  
  //MARK: - Views
  private let profileImageView = PlopProfileImageView(borderShape: .circle)
  private let nameLabel = UILabel()
  private let addFriendButton = PlopGrayButton(title: " 친구 추가 ")
  private let stackView = UIStackView()
  
  //MARK: - Properties
  private var user: User?
  
  weak var delegate: AddFriendCellDelegate?
  
  override func prepareForReuse() {
    super.prepareForReuse()
  }
  
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
    changeButtonUI(with: user.state)
    nameLabel.text = user.profile.nickname
  }
  
  private func changeButtonUI(with state: UserState) {
    addFriendButton.setTitle(state.title, for: .normal)
    addFriendButton.setTitleColor(state.textColor, for: .normal)
    addFriendButton.backgroundColor = state.buttonColor
    addFriendButton.isEnabled = state.buttonEnabled
  }
  
  @objc private func didTappedRequestButton(_ sender: UIButton) {
    guard let user = user else { return }
    if user.state == .notFriend {
      delegate?.sendRequest(to: user)
      changeButtonUI(with: .requestSended)
    } else if user.state == .requestSended {
      delegate?.cancelRequest(to: user)
      changeButtonUI(with: .notFriend)
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

extension UserState {
  var title: String {
    switch self {
    case .notFriend:
      return " 친구 요청 "
    case .requestSended:
      return " 요청 취소 "
    case .requestReceived:
      return " 요청 받음 "
    default:
      return " - "
    }
  }
  
  var buttonColor: UIColor {
    switch self {
    case .notFriend:
      return UIConstants.plopColor
    default:
      return .systemGray
    }
  }
  
  var textColor: UIColor {
    switch self {
    case .notFriend:
      return .white
    default:
      return .label
    }
  }
  
  var buttonEnabled: Bool {
    switch self {
    case .notFriend, .requestSended: return true
    default: return false
    }
  }
}
