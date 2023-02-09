import UIKit

final class OnlineFriendsView: UIView {
  // MARK: - Properties
  private let verticalInset: CGFloat = 8
  private let horizontalInset: CGFloat = 16
  
  private let onlineFriends: [Int]
  
  private lazy var flowLayout: UICollectionViewFlowLayout = {
    let flowLayout = UICollectionViewFlowLayout()
    flowLayout.minimumLineSpacing = 16
    flowLayout.scrollDirection = .horizontal
    flowLayout.sectionInset = UIEdgeInsets(
      top: verticalInset,
      left: horizontalInset,
      bottom: verticalInset,
      right: horizontalInset)
    return flowLayout
  }()
  
  private lazy var collectionView: UICollectionView = {
    let collectionView = UICollectionView(
      frame: .zero,
      collectionViewLayout: self.flowLayout)
    collectionView.delegate = self
    collectionView.dataSource = self
    collectionView.register(
      FriendsCollectionViewCell.self,
      forCellWithReuseIdentifier: FriendsCollectionViewCell.reuseIdentifier)
    collectionView.showsHorizontalScrollIndicator = false
    collectionView.alwaysBounceHorizontal = true
    collectionView.backgroundColor = .systemBackground
    return collectionView
  }()
  
  // MARK: - Initializers
  init(onlineFriends: [Int]) {
    self.onlineFriends = onlineFriends
    super.init(frame: .zero)
    setupCollectionView()
  }
  
  required init?(coder aDecoder: NSCoder) {
    self.onlineFriends = []
    super.init(coder: aDecoder)
  }
  
  // MARK: - Layouts
  override func layoutSubviews() {
    super.layoutSubviews()
    let height = collectionView.frame.height - verticalInset * 2
    let width = height
    let itemSize = CGSize(width: width, height: height)
    flowLayout.itemSize = itemSize
  }
  
  private func setupCollectionView() {
    addSubview(collectionView)
    collectionView.fillSuperview()
  }
}

// MARK: - UICollectionViewDataSource
extension OnlineFriendsView: UICollectionViewDataSource {
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return onlineFriends.count
  }
  
  func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    guard let cell = collectionView .dequeueReusableCell(
      withReuseIdentifier: FriendsCollectionViewCell.reuseIdentifier,
      for: indexPath
    ) as? FriendsCollectionViewCell else {
      fatalError("Dequeued Unregistered Cell")
    }
  
    return cell
  }
}

// MARK: - UICollectionViewDelegate
extension OnlineFriendsView: UICollectionViewDelegate {
  func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
  }
}
