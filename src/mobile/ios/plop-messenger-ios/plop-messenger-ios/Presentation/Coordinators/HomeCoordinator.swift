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
  
  private let usecase = UserUseCase()
  private let profileCoreData = CDProfileUseCase()
  private let disposeBag = DisposeBag()
  
  func start() {
    configureTabBarAppearance()
    tabBarController.tabBar.tintColor = UIConstants.plopColor
    tabBarController.viewControllers = [
      createChatRoomsScreen(),
      createPeopleScreen(),
      createSettingScreen()
    ]
    
    window?.rootViewController = tabBarController
    
    guard let email = UserDefaults.standard.string(
      forKey: "currentEmail"
    ) else { return }
    
    fetchCurrentUser(with: email)
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
    let viewController = PeopleViewController()
    viewController.tabBarItem.title = "People"
    viewController.tabBarItem.image = UIImage(named: "people")
    return viewController
  }
  
  private func createSettingScreen() -> UIViewController {
    let viewController = SettingViewController()
    viewController.tabBarItem.title = "Setting"
    viewController.tabBarItem.image = UIImage(systemName: "person.fill")
    return viewController
  }
  
  private func fetchCurrentUser(with email: String) {
    usecase.mockFetchProfile(email: email)
      .map({ result in
        switch result {
        case .success(let profile):
          self.profileCoreData.save(profile: profile)
        case .failure(let error):
          print(error)
        }
      })
      .flatMap({ _ in
        return self.profileCoreData.fetch(email)
      })
      .subscribe(onNext: {
        print($0)
      })
      .disposed(by: disposeBag)
  }
}
