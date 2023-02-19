import UIKit
import RxSwift
import RxCocoa

final class SignupViewController: UIViewController {
  
  //MARK: - Views
  private let cancelButton = PlopNoBackgroundButton(
    title: "취소",
    titleColor: .systemRed,
    font: .headline)
  private let userIDTextField = PlopTextField(
    placeholder: "아이디를 입력해 주세요...", isSecure: false)
  private let emailTextField = PlopTextField(
    placeholder: "이메일을 입력해 주세요...", isSecure: false)
  private let nicknameTextField = PlopTextField(
    placeholder: "닉네임을 입력해 주세요...", isSecure: false)
  private let passwordTextField = PlopTextField(
    placeholder: "비밀번호를 입력해 주세요", isSecure: true)
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
      userID: userIDTextField.rx.text.orEmpty.asDriver(),
      email: emailTextField.rx.text.orEmpty.asDriver(),
      nickname: nicknameTextField.rx.text.orEmpty.asDriver(),
      password: passwordTextField.rx.text.orEmpty.asDriver(),
      cancelTrigger: cancelButton.rx.tap.asDriver(),
      signupTrigger: signupButton.rx.tap.asDriver())
    let output = viewModel.transform(input)
    
    output.signupButtonEnabled.drive(signupButton.rx.isEnabled)
      .disposed(by: disposeBag)
    
    output.signupResult.drive(onNext: { [unowned self] userID in
      if userID != "" {
        self.successAlert(userID: userID)
      } else {
        self.failedAlert()
      }
    })
    .disposed(by: disposeBag)
    
    output.dismiss.drive(onNext: { [weak self] in
      self?.dismiss(animated: true)
    })
    
    .disposed(by: disposeBag)
  }
  
  private func successAlert(userID: String) {
    let alertController = UIAlertController(
      title: "🎉 환영합니다 🎉",
      message: "\(userID)님 로그인 화면에서 로그인 해주세요!",
      preferredStyle: .alert)
    
    let alertAction = UIAlertAction(
      title: "알겠어요",
      style: .default,
      handler: { [weak self] _ in
        self?.dismiss(animated: true)
      })
    
    alertController.addAction(alertAction)
    
    self.present(alertController, animated: true)
  }
  
  private func failedAlert() {
    let alertController = UIAlertController(
      title: "❌ 문제가 발생했어요 ❌",
      message: "죄송합니다. 다시 시도해주세요.",
      preferredStyle: .alert)
    
    let alertAction = UIAlertAction(
      title: "네",
      style: .default)
    
    alertController.addAction(alertAction)
    
    self.present(alertController, animated: true)
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
    userIDTextField.translatesAutoresizingMaskIntoConstraints = false
    emailTextField.translatesAutoresizingMaskIntoConstraints = false
    nicknameTextField.translatesAutoresizingMaskIntoConstraints = false
    passwordTextField.translatesAutoresizingMaskIntoConstraints = false
    signupStackView.translatesAutoresizingMaskIntoConstraints = false
    signupButton.translatesAutoresizingMaskIntoConstraints = false
    
    signupStackView.addArrangedSubview(userIDTextField)
    signupStackView.addArrangedSubview(emailTextField)
    signupStackView.addArrangedSubview(nicknameTextField)
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
      signupStackView.heightAnchor.constraint(equalToConstant: 152),
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
