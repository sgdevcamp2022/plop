import UIKit
import RxSwift
import RxCocoa

final class LoginViewController: UIViewController {
  //MARK: - Views
  private let logoImageView = UIImageView(
    image: UIImage(named: "plop-logo"))
  private let emailTextField = PlopTextField(
    placeholder: "이메일 또는 아이디", isSecure: false)
  private let passwordTextField = PlopTextField(
    placeholder: "비밀번호", isSecure: true)
  private let loginButton = PlopOrangeButton(title: "로그인")
  private let signupButton = PlopGrayButton(title: "회원가입")
  private let findAccountButton = PlopNoBackgroundButton(
    title: "이메일 / 비밀번호 찾기",
    titleColor: UIConstants.plopColor,
    font: .caption1)
  private let loginStackView = UIStackView()
  private lazy var tapGestureRecognizer: UITapGestureRecognizer = {
    let tapGestureRecognizer = UITapGestureRecognizer(
      target: self.view,
      action: #selector(UIView.endEditing(_:))
    )
    return tapGestureRecognizer
  }()
  
  //MARK: - Properties
  private let viewModel: LoginViewModel
  private let disposeBag = DisposeBag()
  private let fetchUserTrigger = PublishSubject<Void>()
  
  init(viewModel: LoginViewModel) {
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
    setupNotification()
    bind()
  }
  
  private func bind() {
    let input = LoginViewModel.Input(
      email: emailTextField.rx.text.orEmpty.asDriver(),
      password: passwordTextField.rx.text.orEmpty.asDriver(),
      loginTrigger: loginButton.rx.tap.asDriver(),
      signupTrigger: signupButton.rx.tap.asDriver(),
      fetchUserTrigger: fetchUserTrigger.asDriver(onErrorJustReturn: ()))
    
    let output = viewModel.transform(input)
    
    output.buttonEnabled.drive(loginButton.rx.isEnabled)
      .disposed(by: disposeBag)
    
    output.loginResult.drive(onNext: { [unowned self] success in
      if success {
        self.fetchUserTrigger.onNext(())
      } else {
        self.failedAlert()
      }
    })
    .disposed(by: disposeBag)
    
    output.presentSignup.drive()
      .disposed(by: disposeBag)
    
    output.fetchUser
      .drive()
      .disposed(by: disposeBag)
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

//MARK: - Actions
extension LoginViewController {
  @objc private func keyboardWillShown(sender: Notification) {
    guard let userInfo = sender.userInfo,
          let keyboardFrame = userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue,
          let currentTextField = UIResponder.currentFirst() as? UITextField else {
      return
    }
    
    let keyboardTopY = keyboardFrame.cgRectValue.origin.y
    let convertedTextFieldFrame = view.convert(
      currentTextField.frame, from: currentTextField.superview)
    let textFieldBottomY = convertedTextFieldFrame.origin.y + convertedTextFieldFrame.size.height
    
    if textFieldBottomY > keyboardTopY {
      let textBoxY = convertedTextFieldFrame.origin.y
      let newFrameY = (textBoxY - keyboardTopY / 2) * -1
      view.frame.origin.y = newFrameY
    }
  }
  
  @objc private func keyboardWillDisappear(sender: Notification) {
    view.frame.origin.y = 0
  }
}

//MARK: - UI Setup
extension LoginViewController {
  private func configureViews() {
    view.backgroundColor = .systemBackground
    view.addGestureRecognizer(tapGestureRecognizer)
    loginStackView.axis = .vertical
    loginStackView.spacing = 8
    loginStackView.distribution = .fillEqually
    loginButton.isEnabled = true
  }
  
  private func layout() {
    logoImageView.translatesAutoresizingMaskIntoConstraints = false
    emailTextField.translatesAutoresizingMaskIntoConstraints = false
    passwordTextField.translatesAutoresizingMaskIntoConstraints = false
    loginButton.translatesAutoresizingMaskIntoConstraints = false
    signupButton.translatesAutoresizingMaskIntoConstraints = false
    findAccountButton.translatesAutoresizingMaskIntoConstraints = false
    loginStackView.translatesAutoresizingMaskIntoConstraints = false
    
    loginStackView.addArrangedSubview(logoImageView)
    loginStackView.addArrangedSubview(emailTextField)
    loginStackView.addArrangedSubview(passwordTextField)
    loginStackView.addArrangedSubview(loginButton)
    loginStackView.addArrangedSubview(signupButton)
    loginStackView.addArrangedSubview(findAccountButton)
    
    view.addSubview(logoImageView)
    view.addSubview(loginStackView)
    
    NSLayoutConstraint.activate([
      logoImageView.bottomAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerYAnchor,
        constant: -40),
      logoImageView.centerXAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerXAnchor),
      logoImageView.widthAnchor.constraint(equalToConstant: 112),
      logoImageView.heightAnchor.constraint(equalToConstant: 112),
      loginStackView.topAnchor.constraint(
        equalToSystemSpacingBelow: logoImageView.bottomAnchor,
        multiplier: 3),
      loginStackView.centerXAnchor.constraint(
        equalTo: view.safeAreaLayoutGuide.centerXAnchor),
      loginStackView.leadingAnchor.constraint(
        equalToSystemSpacingAfter: view.safeAreaLayoutGuide.leadingAnchor,
        multiplier: 3),
      view.safeAreaLayoutGuide.trailingAnchor.constraint(
        equalToSystemSpacingAfter: loginStackView.trailingAnchor,
        multiplier: 3),
    ])
  }
  
  private func setupNotification() {
    NotificationCenter.default.addObserver(
      self,
      selector: #selector(keyboardWillShown(sender:)),
      name: UIResponder.keyboardWillShowNotification,
      object: nil
    )
    
    NotificationCenter.default.addObserver(
      self,
      selector: #selector(keyboardWillDisappear(sender:)),
      name: UIResponder.keyboardWillHideNotification,
      object: nil
    )
  }
}
