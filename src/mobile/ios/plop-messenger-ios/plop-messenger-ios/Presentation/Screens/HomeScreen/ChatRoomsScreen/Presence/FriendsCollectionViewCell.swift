import UIKit

final class FriendsCollectionViewCell: UICollectionViewCell {
  static let reuseIdentifier = String(describing: FriendsCollectionViewCell.self)
  private let profileImageView = PlopProfileImageView(borderShape: .circle)
  private let onlineStatusView = UIView()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    commonInit()
  }
  
  required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    commonInit()
  }
  
  private func commonInit() {
    setupProfileImageView()
  }
  
  private func setupProfileImageView() {
    contentView.addSubview(profileImageView)
    contentView.addSubview(onlineStatusView)
    
    profileImageView.translatesAutoresizingMaskIntoConstraints = false
    onlineStatusView.translatesAutoresizingMaskIntoConstraints = false
    
    onlineStatusView.backgroundColor = .systemGreen
    onlineStatusView.layer.cornerRadius = 8
    
    NSLayoutConstraint.activate([
      profileImageView.leadingAnchor.constraint(equalTo: leadingAnchor),
      profileImageView.trailingAnchor.constraint(equalTo: trailingAnchor),
      profileImageView.topAnchor.constraint(equalTo: topAnchor),
      profileImageView.bottomAnchor.constraint(equalTo: bottomAnchor),
      onlineStatusView.trailingAnchor.constraint(equalTo: profileImageView.trailingAnchor),
      onlineStatusView.bottomAnchor.constraint(equalTo: profileImageView.bottomAnchor),
      onlineStatusView.widthAnchor.constraint(equalTo: profileImageView.widthAnchor, multiplier: 1/4),
      onlineStatusView.heightAnchor.constraint(equalTo: profileImageView.heightAnchor, multiplier: 1/4)
    ])
  }
  
  // MARK: - Configurations
  func configureCell(imageName: String) {
    profileImageView.image = UIImage(named: imageName)
  }
}
