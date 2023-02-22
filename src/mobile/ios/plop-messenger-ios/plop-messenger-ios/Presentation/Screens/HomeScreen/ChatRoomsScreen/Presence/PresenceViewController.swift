import UIKit
import RxSwift
import RxCocoa

final class PresenceViewController: UIViewController {
  private let verticalInset: CGFloat = 8
  private let horizontalInset: CGFloat = 16
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
  
  private var friends = [String]() {
    didSet {
      self.collectionView.reloadSections(IndexSet(0...0))
    }
  }
  
  private let connectTrigger = PublishSubject<Void>()
  private let disconnectTrigger = PublishSubject<Void>()
  private let fetchOnlineUsersTrigger = PublishSubject<Void>()
  private let disposeBag = DisposeBag()
  
  private let viewModel = PresenceViewModel()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    layout()
    bind()
    
    connectTrigger.onNext(())
  }
  
  private func bind() {
    NotificationCenter.default.addObserver(self, selector: #selector(willEnterForegroundAction), name: UIApplication.willEnterForegroundNotification, object: nil)
    let didEnterBackground = NotificationCenter.default.rx.notification(UIApplication.didEnterBackgroundNotification)
      .mapToVoid()
      .asDriverOnErrorJustComplete()
    let input = PresenceViewModel.Input(
      connectTrigger: connectTrigger.asDriverOnErrorJustComplete(),
      disconnectTrigger: didEnterBackground,
      fetchOnlineUsersTrigger: fetchOnlineUsersTrigger.asDriverOnErrorJustComplete(),
      didEnterBackgroundTrigger: NotificationCenter.default.rx.notification(UIApplication.didEnterBackgroundNotification)
        .mapToVoid()
        .asDriverOnErrorJustComplete()
    )
    
    let output = viewModel.transform(input)
    
    output.users
      .drive(onNext: { [weak self] users in
        self?.friends = users
      })
      .disposed(by: disposeBag)
    
    output.connected.drive(onNext: { [weak self] in
      self?.fetchOnlineUsersTrigger.onNext(())
    })
    .disposed(by: disposeBag)
    
    output.disconnected.drive().disposed(by: disposeBag)
    output.onlineUsers.drive().disposed(by: disposeBag)
    output.didEnterBackground.drive().disposed(by: disposeBag)
  }
  
  @objc private func willEnterForegroundAction() {
    connectTrigger.onNext(())
  }
}

extension PresenceViewController {
  private func layout() {
    collectionView.translatesAutoresizingMaskIntoConstraints = false
    view.addSubview(collectionView)
    NSLayoutConstraint.activate([
      collectionView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
      collectionView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      collectionView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      collectionView.heightAnchor.constraint(equalToConstant: 80)
    ])
  }
}

// MARK: - UICollectionViewDataSource
extension PresenceViewController: UICollectionViewDataSource {
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return friends.count
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

//MARK: - UICollectionViewDelegateFlowLayout
extension PresenceViewController: UICollectionViewDelegateFlowLayout {
  func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
    return 16
  }
  
  func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
    return UIEdgeInsets(top: verticalInset, left: horizontalInset, bottom: verticalInset, right: horizontalInset)
  }
}

// MARK: - UICollectionViewDelegate
extension PresenceViewController: UICollectionViewDelegate {
  func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
  }
}
