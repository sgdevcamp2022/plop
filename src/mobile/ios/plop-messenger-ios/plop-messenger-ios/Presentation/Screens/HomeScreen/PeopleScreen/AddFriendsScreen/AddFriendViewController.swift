import UIKit
import RxCocoa
import RxSwift

final class AddFriendViewController: UIViewController {
  
  private let cancelButton = UIBarButtonItem(
    title: "취소", style: .done, target: nil, action: nil
  )
  private let titleLabel = UILabel()
  private let tableView = UITableView()
  private let searchController = UISearchController()
  
  private let viewModel: AddFriendViewModel
  private let requestTrigger = PublishSubject<String>()
  private let disposeBag = DisposeBag()
  
  private var userResults: [User] = [] {
    didSet {
      tableView.reloadSections(IndexSet(0...0), with: .automatic)
    }
  }
  
  init(viewModel: AddFriendViewModel) {
    self.viewModel = viewModel
    super.init(nibName: nil, bundle: nil)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    configureUI()
    layout()
    bind()
  }
  
  private func bind() {
    let input = AddFriendViewModel.Input(
      search: searchController.searchBar.rx.text.orEmpty.asDriver(),
      searchTrigger: searchController.searchBar.rx.textDidEndEditing.asDriver(),
      cancelTrigger: cancelButton.rx.tap.asDriver(),
      requestTrigger: requestTrigger.asDriver(onErrorJustReturn: ""))
    
    let output = viewModel.transform(input)
    
    output.searchResult
      .drive(onNext: { [unowned self] users in
        self.userResults = users
      })
      .disposed(by: disposeBag)
    
    output.cancel
      .drive()
      .disposed(by: disposeBag)
    
    output.requestResult
      .drive()
      .disposed(by: disposeBag)
  }
}

extension AddFriendViewController {
  private func configureUI() {
    view.backgroundColor = .systemBackground
    title = "친구 추가"
    cancelButton.tintColor = .systemRed
    
    searchController.searchBar.placeholder = "검색"
    searchController.searchBar.tintColor = .systemRed
    searchController.hidesNavigationBarDuringPresentation = false
    
    navigationItem.hidesSearchBarWhenScrolling = false
    navigationItem.leftBarButtonItem = cancelButton
    navigationItem.searchController = searchController
    
    tableView.delegate = self
    tableView.dataSource = self
    
    tableView.register(
      AddFriendCell.self,
      forCellReuseIdentifier: AddFriendCell.reuseIdentifier
    )
  }
  
  private func layout() {
    tableView.translatesAutoresizingMaskIntoConstraints = false
    
    view.addSubview(tableView)
    
    NSLayoutConstraint.activate([
      tableView.topAnchor.constraint(
        equalToSystemSpacingBelow: view.safeAreaLayoutGuide.topAnchor,
        multiplier: 2),
      tableView.leadingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      tableView.trailingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      tableView.bottomAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.bottomAnchor)
    ])
  }
}

extension AddFriendViewController: UITableViewDataSource {
  func tableView(
    _ tableView: UITableView,
    numberOfRowsInSection section: Int
  ) -> Int {
    return userResults.count
  }
  
  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    guard let cell = tableView.dequeueReusableCell(
      withIdentifier: AddFriendCell.reuseIdentifier,
      for: indexPath) as? AddFriendCell else {
      return UITableViewCell()
    }
    let user = userResults[indexPath.row]
    cell.configureData(user)
    cell.delegate = self
    return cell
  }
}

extension AddFriendViewController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 64
  }
}

extension AddFriendViewController: AddFriendCellDelegate {
  func requestFriend(_ cell: AddFriendCell, _ email: String) {
    if cell.canRequest {
      requestTrigger.onNext(email)
      cell.canRequest.toggle()
    } else {
      requestTrigger.onNext("")
      cell.canRequest.toggle()
    }
  }
}
