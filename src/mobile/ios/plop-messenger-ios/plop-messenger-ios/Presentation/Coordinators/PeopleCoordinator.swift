import UIKit

final class PeopleCoordinator: Coordinator {
  var childCoordinators: [Coordinator] = []

  private let navigationController: UINavigationController
  
  init(navigationController: UINavigationController) {
    self.navigationController = navigationController
  }
  
  func start() {
    navigationController.tabBarItem.title = "People"
    navigationController.tabBarItem.image = UIImage(named: "people")
    
    let viewModel = PeopleViewModel(coordinator: self)
    let peopleViewController = PeopleViewController(viewModel: viewModel)
    navigationController.pushViewController(peopleViewController,
                                            animated: false)
  }
  
  func presentAddFriendsScreen() {
    let viewModel = AddFriendViewModel(coordinator: self)
    let addFriendViewController = AddFriendViewController(viewModel: viewModel)
    let viewController = UINavigationController(
      rootViewController: addFriendViewController)
    navigationController.present(viewController, animated: true)
  }
  
  func dismiss() {
    guard let presentedViewController = self.navigationController.presentedViewController else {
      return
    }
    presentedViewController.dismiss(animated: true)
  }
}
