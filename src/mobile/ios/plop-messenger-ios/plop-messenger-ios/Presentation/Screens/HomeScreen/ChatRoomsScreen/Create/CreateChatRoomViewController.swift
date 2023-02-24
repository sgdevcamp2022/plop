import UIKit
import RxSwift
import RxCocoa
import RxDataSources
import Differentiator

final class CreateChatRoomViewController: UIViewController {
  private let cancelButton = UIBarButtonItem(
    title: "취소", style: .done, target: nil, action: nil)
  
  private let inviteButton = UIBarButtonItem(
    title: "만들기",
    style: .plain, target: nil, action: nil)
  
  private let tableView = UITableView()
  private var friends = [User]()
  private var selectedFriends = [String]()
  private let disposeBag = DisposeBag()
  
  private var dataSource: RxTableViewSectionedAnimatedDataSource<CreateChatRoomSection>?
  private let updateListTrigger = PublishSubject<IndexPath?>()
  private let viewModel: CreateChatRoomViewModel
  
  init(viewModel: CreateChatRoomViewModel) {
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
    
    updateListTrigger.onNext(nil)
  }
  
  private func bind() {
    let input = CreateChatRoomViewModel.Input(
      updateListTrigger: updateListTrigger.asDriverOnErrorJustComplete(),
      cancelTrigger: cancelButton.rx.tap.asDriver(),
      inviteTrigger: inviteButton.rx.tap.asDriver()
    )
    
    let output = viewModel.transform(input)
    
    guard let dataSource = dataSource else { return }
    
    output.updateList
      .asObservable()
      .bind(to: tableView.rx.items(dataSource: dataSource))
      .disposed(by: disposeBag)

    output.cancel
      .drive()
      .disposed(by: disposeBag)
    
    output.invite
      .drive()
      .disposed(by: disposeBag)
    
    tableView.rx.setDelegate(self)
      .disposed(by: disposeBag)
    
    tableView.rx.itemSelected
      .subscribe(onNext: { indexPath in
        self.updateListTrigger.onNext(indexPath)
      })
      .disposed(by: disposeBag)
  }
  
  private func configureDataSource() {
    dataSource = RxTableViewSectionedAnimatedDataSource<CreateChatRoomSection>(
      configureCell: { dataSource, tableView, indexPath, item in
        guard let cell = tableView.dequeueReusableCell(
          withIdentifier: CreateChatRoomCell.reuseIdentifier,
          for: indexPath) as? CreateChatRoomCell else {
          return UITableViewCell()
        }
        cell.configureData(item)
        return cell
      },
    titleForHeaderInSection: { dataSource, index in
      return dataSource.sectionModels[index].headerTitle
    })
  }
}

extension CreateChatRoomViewController {
  private func configureUI() {
    view.backgroundColor = .systemBackground
    title = "채팅방 생성"
    
    cancelButton.tintColor = .systemRed
    inviteButton.tintColor = UIConstants.plopColor
    
    navigationItem.leftBarButtonItem = cancelButton
    navigationItem.rightBarButtonItem = inviteButton
    
    tableView.register(
      CreateChatRoomCell.self,
      forCellReuseIdentifier: CreateChatRoomCell.reuseIdentifier)
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
}

extension CreateChatRoomViewController: UITableViewDelegate {
  func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 64
  }
}
