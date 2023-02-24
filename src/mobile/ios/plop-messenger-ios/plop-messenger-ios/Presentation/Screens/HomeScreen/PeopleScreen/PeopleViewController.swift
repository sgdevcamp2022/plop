import UIKit
import RxCocoa
import RxSwift

import RxDataSources

final class PeopleViewController: UIViewController {
  enum Section: String, CaseIterable {
    case requestFriend = "친구 요청"
    case friends = "친구"
  }
  
  private let tableView = UITableView()
  private var addFriendsButton = UIBarButtonItem()
  
  private var dataSource: RxTableViewSectionedAnimatedDataSource<FriendListSection>?
  
  private let viewModel: PeopleViewModel
  
  private let acceptRequestTrigger = PublishSubject<User>()
  private let rejectRequestTrigger = PublishSubject<User>()
  private let updateListTrigger = PublishSubject<Void>()
  private let disposeBag = DisposeBag()
  
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
    
    configureDataSource()
    
    bind()
  }
  
  override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
    updateListTrigger.onNext(())
  }
  
  private func configureDataSource() {
    dataSource = RxTableViewSectionedAnimatedDataSource(
      configureCell: { [unowned self] dataSource, tableView, indexPath, item in
        if indexPath.section == 0 {
          guard let cell = tableView.dequeueReusableCell(
            withIdentifier: FriendRequestCell.reuseIdentifier,
            for: indexPath) as? FriendRequestCell else {
            return UITableViewCell()
          }
          cell.configureData(item)
          cell.delegate = self
          return cell
        } else {
          guard let cell = tableView.dequeueReusableCell(
            withIdentifier: FriendCell.reuseIdentifier,
            for: indexPath) as? FriendCell else {
            return UITableViewCell()
          }
          cell.configureData(item)
          return cell
        }
      }, titleForHeaderInSection: { dataSource, index in
        return dataSource.sectionModels[index].headerTitle
      })
  }
  
  private func bind() {
    let input = PeopleViewModel.Input(
      updateListTrigger: updateListTrigger.asDriverOnErrorJustComplete(),
      acceptRequestTrigger: acceptRequestTrigger.asDriverOnErrorJustComplete(),
      rejectRequestTrigger: rejectRequestTrigger.asDriverOnErrorJustComplete(),
      presentAddFriendTrigger: addFriendsButton.rx.tap.asDriverOnErrorJustComplete()
    )
    
    let output = viewModel.transform(input)
    
    if let dataSource = dataSource {
      output.fetchedList
        .asObservable()
        .bind(to: tableView.rx.items(dataSource: dataSource))
        .disposed(by: disposeBag)
    }
    
    output.updatedList
      .drive()
      .disposed(by: disposeBag)
    
    output.acceptRequest
      .drive()
      .disposed(by: disposeBag)
    
    output.rejectRequest
      .drive()
      .disposed(by: disposeBag)
    
    output.presentAddFriend
      .drive()
      .disposed(by: disposeBag)
    
    tableView.rx.setDelegate(self)
      .disposed(by: disposeBag)
  }
}

//MARK: - UI setup
extension PeopleViewController {
  private func configureViews() {
    view.backgroundColor = .systemBackground
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
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 64
  }
}

extension PeopleViewController: FriendRequestCellDelegate {
  func requestAccepted(_ user: User) {
    acceptRequestTrigger.onNext(user)
  }
  
  func requestRejected(_ user: User) {
    rejectRequestTrigger.onNext(user)
  }
}
