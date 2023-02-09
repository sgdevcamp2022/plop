import UIKit

final class LoginCoordinator: Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private var window: UIWindow
  private lazy var loginViewModel: LoginViewModel = {
    let vm = LoginViewModel(coordinator: self)
    return vm
  }()
  
  private lazy var loginViewController: LoginViewController = {
    let vc = LoginViewController(viewModel: loginViewModel)
    return vc
  }()
  
  private var signupViewController: SignupViewController!
  
  init(window: UIWindow) {
    self.window = window
  }
  
  func start() {
    window.rootViewController = loginViewController
  }
  
  func toSignup() {
    let viewModel = SignupViewModel(coordinator: self)
    signupViewController = SignupViewController(
      viewModel: viewModel)
    loginViewController.present(
      signupViewController, animated: true)
  }
  
  func toHome() {
    let coordinator = HomeCoordinator()
    coordinator.start()
  }
  
  func presentLoginAlert(title: String, message: String) {
    let alertController = UIAlertController(
      title: title,
      message: message,
      preferredStyle: .alert)
    
    alertController.addAction(UIAlertAction(
      title: "확인", style: .default)
    )
    
    loginViewController.present(alertController, animated: true)
  }
  
  func presentAlert(title: String, message: String, dismiss: Bool) {
    let alertController = UIAlertController(
      title: title,
      message: message, preferredStyle: .alert)
    
    
    if dismiss {
      alertController.addAction(UIAlertAction(
        title: "확인", style: .default, handler: { [weak self] _ in
          self?.signupViewController.dismiss(animated: true)
        })
      )
    } else {
      alertController.addAction(UIAlertAction(
        title: "확인", style: .default)
      )
    }
    
    signupViewController.present(alertController, animated: true)
  }
  
  func dismissSignupScreen() {
    signupViewController.dismiss(animated: true)
  }
}
