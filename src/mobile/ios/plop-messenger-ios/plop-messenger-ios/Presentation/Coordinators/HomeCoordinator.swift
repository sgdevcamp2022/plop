import UIKit
import RxSwift

final class HomeCoordinator: NSObject, Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private let tabBarController: UITabBarController
  private let userUsecase = UserUseCase()
  private let disposeBag = DisposeBag()
  private let presenceNetwork = PresenceNetwork()
  
  init(tabBarController: UITabBarController) {
    self.tabBarController = tabBarController
    super.init()
    
    userUsecase.fetchCurrentUser()
      .subscribe()
      .disposed(by: disposeBag)
    
    presenceNetwork.changeStateToOnline()
      .subscribe()
      .disposed(by: disposeBag)
  }

  func start() {
    configureTabBarAppearance()
    tabBarController.tabBar.tintColor = UIConstants.plopColor
    tabBarController.viewControllers = [
      createChatRoomsScreen(),
      createPeopleScreen(),
      createSettingScreen()
    ]
  }
  
  private func configureTabBarAppearance() {
    let tabBarAppearance = UITabBarAppearance()
    tabBarAppearance.configureWithOpaqueBackground()
    
    tabBarController.tabBar.standardAppearance = tabBarAppearance
    if #available(iOS 15.0, *) {
      tabBarController.tabBar.scrollEdgeAppearance = tabBarAppearance
    }
  }
  
  private func createChatRoomsScreen() -> UIViewController {
    let navigationController = UINavigationController()
    let coordinator = ChatRoomListCoordinator(navigationController)
    
    childCoordinators.append(coordinator)
    
    coordinator.start()
    
    return navigationController
  }
  
  private func createPeopleScreen() -> UIViewController {
    let navigationController = UINavigationController()
    let coordinator = PeopleCoordinator(
      navigationController: navigationController)
    
    childCoordinators.append(coordinator)
    
    coordinator.start()
    
    return navigationController
  }
  
  private func createSettingScreen() -> UIViewController {
    let viewController = SettingViewController()
    viewController.tabBarItem.title = "Setting"
    viewController.tabBarItem.image = UIImage(systemName: "person.fill")
    return viewController
  }
}
