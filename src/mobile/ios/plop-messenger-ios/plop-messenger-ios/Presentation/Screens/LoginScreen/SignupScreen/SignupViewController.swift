import UIKit
import RxSwift
import RxCocoa

final class SignupViewController: UIViewController {
  
  //MARK: - Views
  private let cancelButton = PlopNoBackgroundButton(
    title: "취소",
    titleColor: .systemRed,
    font: .headline)
  private let emailTextField = PlopTextField(
    placeholder: "Email을 입력해 주세요...", isSecure: false)
  private let passwordTextField = PlopTextField(
    placeholder: "Password를 입력해 주세요", isSecure: true)
  private let signupStackView = UIStackView()
  private let signupButton = PlopOrangeButton(title: "회원가입")
  
  private lazy var tapGestureRecognizer: UITapGestureRecognizer = {
    let tapGestureRecognizer = UITapGestureRecognizer(
      target: self.view,
      action: #selector(UIView.endEditing(_:))
    )
    return tapGestureRecognizer
  }()
  
  //MARK: - Properties
  private let viewModel: SignupViewModel
  private let disposeBag = DisposeBag()
  
  init(viewModel: SignupViewModel) {
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
    bind()
  }
  
  private func bind() {
    let input = SignupViewModel.Input(
      email: emailTextField.rx.text.orEmpty.asDriver(),
      password: passwordTextField.rx.text.orEmpty.asDriver(),
      cancelTrigger: cancelButton.rx.tap.asDriver(),
      signupTrigger: signupButton.rx.tap.asDriver())
    let output = viewModel.transform(input)
    
    output.signupButtonEnabled.drive(signupButton.rx.isEnabled)
      .disposed(by: disposeBag)
    
    output.signupResult.drive()
      .disposed(by: disposeBag)
    
    output.dismiss.drive(onNext: { [weak self] in
      self?.dismiss(animated: true)
    })
    .disposed(by: disposeBag)
  }
}

extension SignupViewController {
  private func configureViews() {
    view.backgroundColor = .systemBackground
    view.addGestureRecognizer(tapGestureRecognizer)
    
    signupStackView.axis = .vertical
    signupStackView.distribution = .fillEqually
    signupStackView.spacing = 8
    
    signupButton.isEnabled = false
  }
  
  private func layout() {
    cancelButton.translatesAutoresizingMaskIntoConstraints = false
    emailTextField.translatesAutoresizingMaskIntoConstraints = false
    passwordTextField.translatesAutoresizingMaskIntoConstraints = false
    signupStackView.translatesAutoresizingMaskIntoConstraints = false
    signupButton.translatesAutoresizingMaskIntoConstraints = false
    
    signupStackView.addArrangedSubview(emailTextField)
    signupStackView.addArrangedSubview(passwordTextField)
    
    view.addSubview(cancelButton)
    view.addSubview(signupStackView)
    view.addSubview(signupButton)
    
    NSLayoutConstraint.activate([
      cancelButton.topAnchor.constraint(
        equalToSystemSpacingBelow: view.safeAreaLayoutGuide.topAnchor,
        multiplier: 2),
      cancelButton.leadingAnchor.constraint(
        equalToSystemSpacingAfter: view.safeAreaLayoutGuide.leadingAnchor,
        multiplier: 2),
      signupStackView.centerXAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerXAnchor),
      signupStackView.centerYAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerYAnchor),
      signupStackView.leadingAnchor.constraint(
        equalToSystemSpacingAfter: view.safeAreaLayoutGuide.leadingAnchor,
        multiplier: 3),
      view.safeAreaLayoutGuide.trailingAnchor.constraint(
        equalToSystemSpacingAfter: signupStackView.trailingAnchor,
        multiplier: 3),
      signupStackView.heightAnchor.constraint(equalToConstant: 80),
      view.safeAreaLayoutGuide.bottomAnchor.constraint(
        equalToSystemSpacingBelow: signupButton.bottomAnchor,
        multiplier: 3),
      signupButton.leadingAnchor.constraint(
        equalToSystemSpacingAfter: view.safeAreaLayoutGuide.leadingAnchor,
        multiplier: 3),
      view.safeAreaLayoutGuide.trailingAnchor.constraint(
        equalToSystemSpacingAfter: signupButton.trailingAnchor,
        multiplier: 3),
    ])
  }
}
