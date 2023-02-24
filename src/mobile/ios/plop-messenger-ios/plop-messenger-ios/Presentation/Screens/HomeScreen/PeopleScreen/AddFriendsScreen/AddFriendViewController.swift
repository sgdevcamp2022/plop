import UIKit
import RxCocoa
import RxSwift
import RxDataSources
import Differentiator

final class AddFriendViewController: UIViewController {
  
  private let cancelButton = UIBarButtonItem(
    title: "취소", style: .done, target: nil, action: nil
  )
  private let tableView = UITableView()
  private let searchController = UISearchController()
  
  private var dataSource: RxTableViewSectionedAnimatedDataSource<AddFriendSection>?
  
  private let viewModel: AddFriendViewModel
  private let disposeBag = DisposeBag()
  
  private let sendRequestTrigger = PublishSubject<User>()
  private let cancelRequestTrigger = PublishSubject<User>()
  
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
    
    configureDataSource()
    
    bind()
  }
  
  private func bind() {
    let input = AddFriendViewModel.Input(
      search: searchController.searchBar.rx.text.orEmpty.asDriver(),
      searchTrigger: searchController.searchBar.rx.textDidEndEditing.asDriver(),
      cancelTrigger: cancelButton.rx.tap.asDriver(),
      requestTrigger: sendRequestTrigger.asDriverOnErrorJustComplete(),
      cancelRequestTrigger: cancelRequestTrigger.asDriverOnErrorJustComplete()
    )
    
    let output = viewModel.transform(input)
    
    if let dataSource = dataSource {
      output.searchResult
        .asObservable()
        .bind(to: tableView.rx.items(dataSource: dataSource))
        .disposed(by: disposeBag)
    }

    output.cancel
      .drive()
      .disposed(by: disposeBag)
    
    output.sendRequest
      .drive()
      .disposed(by: disposeBag)
    
    output.cancelRequest
      .drive()
      .disposed(by: disposeBag)
    
    tableView.rx.setDelegate(self)
      .disposed(by: disposeBag)
  }
  
  private func configureDataSource() {
    dataSource = RxTableViewSectionedAnimatedDataSource<AddFriendSection>(configureCell: { [unowned self] dataSource, tableView, indexPath, item in
      guard let cell = tableView.dequeueReusableCell(
        withIdentifier: AddFriendCell.reuseIdentifier,
        for: indexPath) as? AddFriendCell else {
        return UITableViewCell()
      }
      
      cell.configureData(item)
      
      cell.delegate = self
      return cell
    },
    titleForHeaderInSection: { dataSource, indexPath in
      return dataSource.sectionModels[indexPath].headerTitle
    })
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
    searchController.searchBar.autocorrectionType = .no
    searchController.searchBar.autocapitalizationType = .none
    
    navigationItem.hidesSearchBarWhenScrolling = false
    navigationItem.leftBarButtonItem = cancelButton
    navigationItem.searchController = searchController

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
        equalTo: view.safeAreaLayoutGuide.topAnchor),
      tableView.leadingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.leadingAnchor),
      tableView.trailingAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.trailingAnchor),
      tableView.bottomAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.bottomAnchor)
    ])
  }
}

extension AddFriendViewController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 64
  }
}

extension AddFriendViewController: AddFriendCellDelegate {
  func sendRequest(to user: User) {
    sendRequestTrigger.onNext(user)
  }
  
  func cancelRequest(to user: User) {
    cancelRequestTrigger.onNext(user)
  }
}
