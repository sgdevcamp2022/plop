import UIKit
import RxSwift
@main
class AppDelegate: UIResponder, UIApplicationDelegate {
  private let presenceNetwork = PresenceNetwork()
  private let disposeBag = DisposeBag()
  func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    let notificationOption = launchOptions?[.remoteNotification]
    if let notification = notificationOption as? [String: AnyObject],
       let aps = notification["aps"] as? [String: AnyObject] {
      print(aps)
    }
    return true
  }
  
  // MARK: UISceneSession Lifecycle
  func application(
    _ application: UIApplication,
    configurationForConnecting connectingSceneSession: UISceneSession,
    options: UIScene.ConnectionOptions
  ) -> UISceneConfiguration {
    return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
  }
  
  func application(
    _ application: UIApplication,
    didDiscardSceneSessions sceneSessions: Set<UISceneSession>
  ) {}
  
  func applicationDidEnterBackground(_ application: UIApplication) {
  }
  
  func applicationWillTerminate(_ application: UIApplication) {
  }
}
