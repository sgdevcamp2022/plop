import UIKit
import RxSwift
import RxCocoa

final class ChatRoomListViewController: UIViewController {
  private let searchController = UISearchController(
    searchResultsController: nil
  )
  
  private let onlineFriendsView = UIView()
  private let createChatRoomButton = UIBarButtonItem(
    image: UIImage(systemName: "square.and.pencil"),
    style: .plain,
    target: nil,
    action: nil)
  private let presenceViewController = PresenceViewController()
  
  //TODO: - Table View
  private let tableView = UITableView()
  
  private let disposeBag = DisposeBag()
  private let fetchChatRoomListTrigger = PublishSubject<Void>()
  private let roomSelectedTrigger = PublishSubject<ChatRoom>()
  private var rooms = [ChatRoom]() {
    didSet {
      tableView.reloadSections(IndexSet(0...0), with: .automatic)
    }
  }
  
  private let chatRoomsViewModel: ChatRoomListViewModel

  init(chatRoomsViewModel: ChatRoomListViewModel) {
    self.chatRoomsViewModel = chatRoomsViewModel
    super.init(nibName: nil, bundle: nil)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    configureViews()
    configureNavigationBarAppearance()
    configureNavigationItems()
    configureChatRoomListView()
    
    layout()
    
    addChild(presenceViewController)
    onlineFriendsView.addSubview(presenceViewController.view)
    onlineFriendsView.addSubview(presenceViewController.view)
    presenceViewController.view.frame = onlineFriendsView.bounds
    presenceViewController.didMove(toParent: self)
  
    bind()
    
    fetchChatRoomListTrigger.onNext(())
  }
  
  private func bind() {
    let input = ChatRoomListViewModel.Input(
      fetchChatRoomListTrigger: fetchChatRoomListTrigger.asDriverOnErrorJustComplete(),
      createChatRoomTrigger: createChatRoomButton.rx.tap.asDriver(),
      chatRoomSelectedTrigger: roomSelectedTrigger.asDriverOnErrorJustComplete()
    )
    
    let output = chatRoomsViewModel.transform(input)
    
    
    output.chatRoomList
      .drive(onNext: { chatRooms in
        self.rooms = chatRooms
      })
      .disposed(by: disposeBag)
    
    output.finishedFetchChatRoomList
      .drive()
      .disposed(by: disposeBag)
    
    output.createChatRoom
      .drive()
      .disposed(by: disposeBag)
    
    output.presentChatRoom
      .drive()
      .disposed(by: disposeBag)
  }
}

//MARK: - UI Setup
extension ChatRoomListViewController {
  private func configureViews() {
    view.backgroundColor = .systemBackground
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
      title: "Chat",
      style: .done,
      target: nil,
      action: nil
    )
    
    navigationItem.leftBarButtonItems = [leftBarButton, leftTitle]
    navigationItem.rightBarButtonItem = createChatRoomButton
    navigationController?.navigationBar.tintColor = .label
    
    searchController.searchBar.placeholder = "Search room..."
    searchController.hidesNavigationBarDuringPresentation = false
    navigationItem.searchController = searchController
  }
  
  private func configureChatRoomListView() {
    tableView.separatorStyle = .none
    
    tableView.register(
      ChatRoomListCell.self,
      forCellReuseIdentifier: ChatRoomListCell.reuseIdentifier)
    
    tableView.dataSource = self
    tableView.delegate = self
  }
  
  private func layout() {
    onlineFriendsView.translatesAutoresizingMaskIntoConstraints = false
    tableView.translatesAutoresizingMaskIntoConstraints = false

    view.addSubview(onlineFriendsView)
    view.addSubview(tableView)
    
    NSLayoutConstraint.activate([
      onlineFriendsView.topAnchor.constraint(
        equalToSystemSpacingBelow: view.safeAreaLayoutGuide.topAnchor,
        multiplier: 1),
      onlineFriendsView.leadingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      onlineFriendsView.trailingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      onlineFriendsView.heightAnchor.constraint(
        equalToConstant: 80),
      tableView.topAnchor.constraint(
        equalToSystemSpacingBelow: onlineFriendsView.bottomAnchor,
        multiplier: 1),
      tableView.leadingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      tableView.trailingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      tableView.bottomAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.bottomAnchor),
    ])
  }
}

extension ChatRoomListViewController: UITableViewDataSource {
  func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    return rooms.count
  }
  
  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    guard let cell = tableView.dequeueReusableCell(
      withIdentifier: ChatRoomListCell.reuseIdentifier,
      for: indexPath) as? ChatRoomListCell else {
      return UITableViewCell()
    }
    let room = rooms[indexPath.row]
    
    cell.setupWithData(room)
    
    return cell
  }
}

//MARK: - UITableViewDelegate
extension ChatRoomListViewController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 80
  }
  
  func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    let chatRoom = rooms[indexPath.row]
    roomSelectedTrigger.onNext(chatRoom)
  }
}
