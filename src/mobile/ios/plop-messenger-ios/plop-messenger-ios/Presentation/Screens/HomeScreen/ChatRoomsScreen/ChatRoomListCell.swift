import UIKit

final class ChatRoomListCell: UITableViewCell {
  static let reuseIdentifier = String(describing: ChatRoomListCell.self)
  
  private let roomImageView = PlopProfileImageView(borderShape: .circle)
  private let roomNameLabel = UILabel()
  private let lastMessageLabel = UILabel()
  private let labelStackView = UIStackView()
  
  override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
    super.init(style: style, reuseIdentifier: reuseIdentifier)
    configure()
    layout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func setupWithData(_ room: ChatRoom) {
    roomNameLabel.text = room.title
    lastMessageLabel.text = room.messages.last?.content ?? ""
    roomImageView.image = UIImage(systemName: "ellipsis.message")
    roomImageView.tintColor = .systemOrange
  }
}

extension ChatRoomListCell {
  private func configure() {
    selectionStyle = .none
    
    labelStackView.axis = .vertical
    labelStackView.distribution = .fillEqually
    
    roomNameLabel.textColor = .label
    roomNameLabel.font = .preferredFont(forTextStyle: .title3)
    roomNameLabel.text = "Room name"
    roomNameLabel.numberOfLines = 1
    
    lastMessageLabel.textColor = .label
    lastMessageLabel.font = .preferredFont(forTextStyle: .body)
    lastMessageLabel.text = "Last message"
    lastMessageLabel.numberOfLines = 1
  }
  
  private func layout() {
    roomImageView.translatesAutoresizingMaskIntoConstraints = false
    roomNameLabel.translatesAutoresizingMaskIntoConstraints = false
    lastMessageLabel.translatesAutoresizingMaskIntoConstraints = false
    labelStackView.translatesAutoresizingMaskIntoConstraints = false
    
    labelStackView.addArrangedSubview(roomNameLabel)
    labelStackView.addArrangedSubview(lastMessageLabel)
    
    contentView.addSubview(roomImageView)
    contentView.addSubview(labelStackView)
    
    NSLayoutConstraint.activate([
      roomImageView.topAnchor.constraint(
        equalToSystemSpacingBelow: contentView.topAnchor,
        multiplier: 2),
      roomImageView.leadingAnchor.constraint(
        equalToSystemSpacingAfter: contentView.leadingAnchor,
        multiplier: 2),
      contentView.bottomAnchor.constraint(
        equalToSystemSpacingBelow: roomImageView.bottomAnchor,
        multiplier: 2),
      roomImageView.widthAnchor.constraint(equalToConstant: 48),
      labelStackView.topAnchor.constraint(
        equalToSystemSpacingBelow: contentView.topAnchor,
        multiplier: 1),
      labelStackView.leadingAnchor.constraint(
        equalToSystemSpacingAfter: roomImageView.trailingAnchor,
        multiplier: 1),
      contentView.trailingAnchor.constraint(
        equalToSystemSpacingAfter: labelStackView.trailingAnchor,
        multiplier: 1),
      contentView.bottomAnchor.constraint(
        equalToSystemSpacingBelow: labelStackView.bottomAnchor,
        multiplier: 1),
    ])
  }
}
