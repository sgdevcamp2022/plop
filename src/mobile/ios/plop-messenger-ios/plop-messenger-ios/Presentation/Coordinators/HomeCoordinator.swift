import UIKit
import CoreData
import RxSwift
import FirebaseCore
import FirebaseMessaging
import UserNotifications

final class HomeCoordinator: NSObject, Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private let tabBarController: UITabBarController
  private let userUsecase = UserUseCase()
  private let disposeBag = DisposeBag()
  private let pushNetwork = PushNetwork()
  
  init(tabBarController: UITabBarController) {
    self.tabBarController = tabBarController
    super.init()
    
    userUsecase.fetchCurrentUser()
      .subscribe()
      .disposed(by: disposeBag)
    
    FirebaseApp.configure()
    UNUserNotificationCenter.current().delegate = self
    Messaging.messaging().delegate = self
    let application = UIApplication.shared
    UNUserNotificationCenter.current().requestAuthorization(options: [
      .badge, .sound, .alert
    ], completionHandler: { granted, _ in
      guard granted else { return }
      DispatchQueue.main.async {
        application.registerForRemoteNotifications()
      }
    })
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
  
  private func registerPushNotification(_ tokenID: String) {
    pushNetwork.register(tokenID)
      .subscribe(onNext: {
        print("🤝 Sended fcm token")
      })
      .disposed(by: disposeBag)
  }
}

//MARK: - MessagingDelegate
extension HomeCoordinator: MessagingDelegate {
  func messaging(
    _ messaging: Messaging,
    didReceiveRegistrationToken fcmToken: String?
  ) {
    guard let fcmToken = fcmToken else { return }
    registerPushNotification(fcmToken)
  }
}

//MARK: - UNUserNotificationCenterDelegate
extension HomeCoordinator: UNUserNotificationCenterDelegate {
  func application(
    _ application: UIApplication,
    didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
  ) {
    Messaging.messaging().apnsToken = deviceToken
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
    print(notification)
    completionHandler([.badge, .sound])
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
    print(response)
    let userInfo = response.notification.request.content.userInfo
    guard let deepLinkUrl = userInfo["url"] as? String,
          let url = URL(string: deepLinkUrl),
          url.host == "navigation" else { return }
    
    let urlString = url.absoluteString
    guard urlString.contains("name") else { return }
    
    let components = URLComponents(string: urlString)
    
    let urlQueryItems = components?.queryItems ?? []
    var dictionaryData = [String: String]()
    urlQueryItems.forEach { dictionaryData[$0.name] = $0.value }
    guard let name = dictionaryData["name"] else { return }
  
    completionHandler()
  }
}
