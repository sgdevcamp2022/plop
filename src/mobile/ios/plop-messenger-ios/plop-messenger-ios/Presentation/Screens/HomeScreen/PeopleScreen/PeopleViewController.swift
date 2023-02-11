import UIKit
import RxCocoa
import RxSwift

final class PeopleViewController: UIViewController {
  
  enum Section: String, CaseIterable {
    case requestFriend = "친구 요청"
    case friends = "친구"
  }
  
  private let tableView = UITableView()
  private var addFriendsButton = UIBarButtonItem()
  
  private var requestFriend: [User] = [] {
    didSet {
      tableView.beginUpdates()
      tableView.reloadSections(
        IndexSet(0 ... 0),
        with: .automatic)
      tableView.endUpdates()
    }
  }
  
  private var friends: [User] = [] {
    didSet {
      tableView.beginUpdates()
      tableView.reloadSections(
        IndexSet(1 ... 1),
        with: .automatic)
      tableView.endUpdates()
    }
  }
  
  private let viewModel: PeopleViewModel
  private let disposeBag = DisposeBag()
  private let acceptFriendPublisher = PublishSubject<String>()
  
  init(viewModel: PeopleViewModel) {
    self.viewModel = viewModel
    super.init(nibName: nil, bundle: nil)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    configureViews()
    layout()
    
    configureNavigationBarAppearance()
    configureNavigationItems()
    
    requestFriend.append(User(userID: "holuck", email: "holuck@naver.com", profile: Profile(nickname: "holuck", imageURL: "")))
    
    bind()
  }
  
  private func bind() {
    let viewWillAppear = rx.sentMessage(#selector(UIViewController.viewWillAppear(_:)))
      .mapToVoid()
      .asDriverOnErrorJustComplete()
    
    let input = PeopleViewModel.Input(
      requestFriendListTrigger: viewWillAppear,
      friendsListTrigger: viewWillAppear,
      addFriendsTrigger: addFriendsButton.rx.tap.asDriver(),
      requestFriendRespondTrigger: acceptFriendPublisher.asDriver(onErrorJustReturn: "")
    )
    
    let output = viewModel.transform(input)
    
    output.requestFriendList
      .drive(onNext: { [weak self] friends in
        self?.requestFriend = friends
      })
      .disposed(by: disposeBag)
    
    output.friends
      .drive(onNext: { [weak self] friends in
        self?.friends = friends
      })
      .disposed(by: disposeBag)
    
    output.presentAddFriends
      .drive()
      .disposed(by: disposeBag)
    
    output.requestResponse
      .drive(onNext: {
        print("dismiss")
      }, onCompleted: { print("completed")},
      onDisposed: { print("Disposed")})
      .disposed(by: disposeBag)
  }
}

//MARK: - UI setup
extension PeopleViewController {
  private func configureViews() {
    view.backgroundColor = .systemBackground
    tableView.dataSource = self
    tableView.delegate = self
    tableView.separatorStyle = .none
    
    tableView.register(
      FriendRequestCell.self,
      forCellReuseIdentifier: FriendRequestCell.reuseIdentifier)
    tableView.register(
      FriendCell.self,
      forCellReuseIdentifier: FriendCell.reuseIdentifier)
  }
  
  private func layout() {
    tableView.translatesAutoresizingMaskIntoConstraints = false
    view.addSubview(tableView)
    
    NSLayoutConstraint.activate([
      tableView.topAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.topAnchor),
      tableView.leadingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      tableView.trailingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      tableView.bottomAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.bottomAnchor),
    ])
  }
  
  private func configureNavigationBarAppearance() {
    let navigationBarAppearance = UINavigationBarAppearance()
    
    navigationBarAppearance.configureWithOpaqueBackground()
    
    self.navigationController?.navigationBar.standardAppearance = navigationBarAppearance
    self.navigationController?.navigationBar.scrollEdgeAppearance = navigationBarAppearance
    self.navigationController?.navigationBar.compactAppearance = navigationBarAppearance
    
    if #available(iOS 15.0, *) {
      self.navigationController?.navigationBar.compactScrollEdgeAppearance = navigationBarAppearance
    }
  }
  
  private func configureNavigationItems() {
    let leftBarButton = UIBarButtonItem(
      image: UIImage(systemName: "person.circle"),
      style: .plain,
      target: self,
      action: nil
    )
    
    let leftTitle = UIBarButtonItem(
      title: "People",
      style: .done,
      target: nil,
      action: nil
    )
    
    addFriendsButton.image = UIImage(
      systemName: "person.crop.circle.badge.plus")
    
    navigationItem.leftBarButtonItems = [leftBarButton, leftTitle]
    navigationItem.rightBarButtonItems = [addFriendsButton]
    navigationController?.navigationBar.tintColor = .label
  }
}

extension PeopleViewController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
    switch section {
    case 0:
      return Section.allCases[0].rawValue
    case 1:
      return Section.allCases[1].rawValue
    default:
      return ""
    }
  }
  
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 64
  }
}

extension PeopleViewController: UITableViewDataSource {
  func numberOfSections(in tableView: UITableView) -> Int {
    return Section.allCases.count
  }
  
  func tableView(_ tableView: UITableView,
                 numberOfRowsInSection section: Int) -> Int {
    return (section == 0) ? requestFriend.count : friends.count
  }
  
  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    
    if indexPath.section == 0 {
      guard let cell = tableView.dequeueReusableCell(
        withIdentifier: FriendRequestCell.reuseIdentifier,
        for: indexPath) as? FriendRequestCell else {
        return UITableViewCell()
      }
      let friend = requestFriend[indexPath.row]
      cell.configureData(friend, indexPath: indexPath)
      cell.delegate = self
      return cell
    } else {
      guard let cell = tableView.dequeueReusableCell(
        withIdentifier: FriendCell.reuseIdentifier,
        for: indexPath) as? FriendCell else {
        return UITableViewCell()
      }
      let friend = friends[indexPath.row]
      
      cell.configureData(friend)
      return cell
    }
  }
}

extension PeopleViewController: FriendRequestCellDelegate {
  func didTappedAccept(_ indexPath: IndexPath) {
    let requestedFriend = requestFriend[indexPath.row]
    acceptFriendPublisher.onNext(requestedFriend.userID)
  }
  
  func didTappedCancel(_ indexPath: IndexPath) {
    acceptFriendPublisher.onNext("")
  }
}
