import UIKit
import CoreData
import RxSwift

final class HomeCoordinator: Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private var tabBarController = UITabBarController()
  private lazy var window: UIWindow? = {
    let window = UIApplication
      .shared
      .connectedScenes
      .compactMap { $0 as? UIWindowScene }
      .flatMap { $0.windows }
      .first { $0.isKeyWindow }
    return window
  }()
  
  func start() {
    configureTabBarAppearance()
    tabBarController.tabBar.tintColor = UIConstants.plopColor
    tabBarController.viewControllers = [
      createChatRoomsScreen(),
      createPeopleScreen(),
      createSettingScreen()
    ]
    
    window?.rootViewController = tabBarController
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
    let viewController = ChatRoomsViewController()
    let navigationController = UINavigationController(
      rootViewController: viewController
    )
    navigationController.tabBarItem.title = "Chats"
    navigationController.tabBarItem.image = UIImage(named: "chatroom")
    return navigationController
  }
  
  private func createPeopleScreen() -> UIViewController {
    let navigationController = UINavigationController()
    let coordinator = PeopleCoordinator(
      navigationController: navigationController)

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
