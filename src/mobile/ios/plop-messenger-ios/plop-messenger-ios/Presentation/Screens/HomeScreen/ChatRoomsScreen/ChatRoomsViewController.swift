import UIKit
import RxSwift
import RxCocoa

final class ChatRoomsViewController: UIViewController {
  private let searchController = UISearchController(
    searchResultsController: nil
  )
  
  private let onlineFriendsView = OnlineFriendsView(onlineFriends: [1, 2, 3])
  
  //TODO: - Table View
  private let chatRoomList = UITableView()
  private let viewModel = ChatRoomsViewModel()
  private var chatRooms = [RoomListModel]()
  private let disposeBag = DisposeBag()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    configureViews()
    configureNavigationBarAppearance()
    configureNavigationItems()
    configureChatRoomListView()
    layout()
    
    bind()
  }
  
  private func bind() {
    let viewWillAppear = rx.sentMessage(#selector(UIViewController.viewWillAppear(_:)))
      .mapToVoid()
      .asDriverOnErrorJustComplete()
    
    let input = ChatRoomsViewModel.Input(
      fetchRoomsTrigger: viewWillAppear)
    
    let output = viewModel.transform(input)
    
    output.roomList
      .drive(onNext: { [unowned self] rooms in
        self.chatRooms = rooms
        self.chatRoomList.reloadData()
      })
      .disposed(by: disposeBag)
  }
  
  //MARK: - Actions
  @objc private func didTappedCreateChatRoom() {
    
  }
}

//MARK: - UI Setup
extension ChatRoomsViewController {
  private func configureViews() {
    view.backgroundColor = .systemBackground
    title = "Chats"
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
    
    let rightBarButton = UIBarButtonItem(
      image: UIImage(systemName: "square.and.pencil"),
      style: .plain,
      target: self,
      action: #selector(didTappedCreateChatRoom)
    )
    
    navigationItem.leftBarButtonItem = leftBarButton
    navigationItem.rightBarButtonItem = rightBarButton
    navigationController?.navigationBar.tintColor = UIConstants.plopColor
    
    searchController.searchBar.placeholder = "Search room..."
    searchController.hidesNavigationBarDuringPresentation = false
    navigationItem.searchController = searchController
  }
  
  private func configureChatRoomListView() {
//    chatRoomList.backgroundColor = .systemRed
    
    chatRoomList.register(
      ChatRoomListCell.self,
      forCellReuseIdentifier: ChatRoomListCell.reuseIdentifier)
    
    chatRoomList.delegate = self
    chatRoomList.dataSource = self
  }
  
  private func layout() {
    onlineFriendsView.translatesAutoresizingMaskIntoConstraints = false
    chatRoomList.translatesAutoresizingMaskIntoConstraints = false
    
    view.addSubview(onlineFriendsView)
    view.addSubview(chatRoomList)
    
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
      chatRoomList.topAnchor.constraint(
        equalToSystemSpacingBelow: onlineFriendsView.bottomAnchor,
        multiplier: 1),
      chatRoomList.leadingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      chatRoomList.trailingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      chatRoomList.bottomAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.bottomAnchor),
    ])
  }
}

extension ChatRoomsViewController: UITableViewDataSource {
  func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    return chatRooms.count
  }
  
  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    guard let cell = tableView.dequeueReusableCell(
      withIdentifier: ChatRoomListCell.reuseIdentifier,
      for: indexPath) as? ChatRoomListCell else {
      return UITableViewCell()
    }
    
    let room = chatRooms[indexPath.row]
    
    cell.setupWithData(room)
    
    return cell
  }
}

extension ChatRoomsViewController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 80
  }
}
