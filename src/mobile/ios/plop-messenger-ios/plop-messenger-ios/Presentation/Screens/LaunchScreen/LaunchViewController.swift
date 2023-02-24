import UIKit
import RxSwift
import RxCocoa

final class LaunchViewController: UIViewController {
  private let logoImageView = UIImageView(
    image: UIImage(named: "plop-logo"))
  
  private let viewModel: LaunchViewModel
  private let disposeBag = DisposeBag()
  
  init(viewModel: LaunchViewModel) {
    self.viewModel = viewModel
    super.init(nibName: nil, bundle: nil)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    layout()
    bind()
  }
  
  private func bind() {
    let viewDidAppear = rx.sentMessage(
      #selector(UIViewController.viewWillAppear(_:)))
      .mapToVoid()
      .asDriverOnErrorJustComplete()
    
    let input = LaunchViewModel.Input(
      autoLoginTrigger: viewDidAppear)
    
    let output = viewModel.transform(input)
    
    output.autoLoginResult
      .drive()
      .disposed(by: disposeBag)
  }
}

//MARK: - UI Setup
extension LaunchViewController {
  private func layout() {
    logoImageView.translatesAutoresizingMaskIntoConstraints = false
    
    view.addSubview(logoImageView)
    
    NSLayoutConstraint.activate([
      logoImageView.centerXAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerXAnchor),
      logoImageView.centerYAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerYAnchor),
    ])
  }
}
