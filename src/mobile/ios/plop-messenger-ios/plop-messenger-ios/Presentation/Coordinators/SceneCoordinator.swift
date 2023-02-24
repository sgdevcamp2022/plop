import UIKit

final class SceneCoordinator: Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private var window: UIWindow
  private var launchViewController: LaunchViewController!
  private var loginViewController: LoginViewController!
  
  init(window: UIWindow) {
    self.window = window
    window.makeKeyAndVisible()
  }
  
  func start() {
    let viewModel = LaunchViewModel(coordinator: self)
    launchViewController = LaunchViewController(viewModel: viewModel)
    
    window.rootViewController = launchViewController
  }
  
  func toLogin() {
    let viewModel = LoginViewModel(coordinator: self)
    loginViewController = LoginViewController(viewModel: viewModel)
    
    window.rootViewController = loginViewController
  }
  
  func toHome() {
    let tabBarController = UITabBarController()
    let coordinator = HomeCoordinator(tabBarController: tabBarController)
    
    childCoordinators.append(coordinator)
    coordinator.start()
    
    window.rootViewController = tabBarController
  }
  
  func toSignup() {
    let viewModel = SignupViewModel(coordinator: self)
    let viewController = SignupViewController(viewModel: viewModel)
    loginViewController.present(viewController, animated: true)
  }
  
  func dismiss() {
    window.rootViewController?.presentedViewController?.dismiss(animated: true)
  }
}
