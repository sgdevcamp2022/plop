import UIKit

final class SceneCoordinator: Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private var window: UIWindow
  
  init(window: UIWindow) {
    self.window = window
    window.makeKeyAndVisible()
  }
  
  func start() {
    let viewModel = LaunchViewModel(coordinator: self)
    let launchViewController = LaunchViewController(viewModel: viewModel)
    
    window.rootViewController = launchViewController
  }
  
  func toLogin() {
    let coordinator = LoginCoordinator(window: self.window)
    childCoordinators.append(coordinator)
    coordinator.start()
  }
  
  func toHome() {
    let coordinator = HomeCoordinator()
    childCoordinators.append(coordinator)
    coordinator.start()
  }
}
