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
  
  init(window: UIWindow) {
    self.window = window
  }
  
  func start() {
    window.rootViewController = loginViewController
  }
  
  func toSignup() {
    let viewModel = SignupViewModel(coordinator: self)
    let viewController = SignupViewController(viewModel: viewModel)
    loginViewController.present(viewController, animated: true)
  }

  func toHome() {
    let coordinator = HomeCoordinator()
    coordinator.start()
  }
}
