import UIKit
import SwiftStomp

final class ChatRoomListCoordinator: Coordinator {
  var childCoordinators: [Coordinator] = []
  
  private let navigationController: UINavigationController
  
  init(_ navigationController: UINavigationController) {
    self.navigationController = navigationController
  }
  
  func start() {
    navigationController.tabBarItem.title = "Chats"
    navigationController.tabBarItem.image = UIImage(named: "chatroom")
    
    let viewModel = ChatRoomListViewModel(coordinator: self)
    let viewController = ChatRoomListViewController(chatRoomsViewModel: viewModel)
     
    navigationController.pushViewController(viewController, animated: true)
  }
  
  func toCreateChatRoom() {
    let viewModel = CreateChatRoomViewModel(coordinator: self)
    let viewController = CreateChatRoomViewController(viewModel: viewModel)
    let navController = UINavigationController(rootViewController: viewController)
    
    navigationController.present(navController, animated: true)
  }
  
  func dismiss() {
    let viewController = self.navigationController.presentedViewController
    viewController?.dismiss(animated: true)
  }
  
  func dismissAndpushChatRoom(_ type: String, members: [User]) {
    let presentedViewController = self.navigationController.presentedViewController
    let roomName = members.map({ $0.userID }).joined(separator: ", ")
    let room = ChatRoom(
      roomID: "",
      title: roomName,
      type: type,
      members: members.map({ $0.toMember() }),
      messages: [],
      managers: [])
    presentedViewController?.dismiss(animated: true, completion: {
      let chatRoomViewController = ChatRoomViewController(
        room, "ho")
      self.navigationController.pushViewController(chatRoomViewController, animated: true)
    })
  }
  
  func pushChatRoom(_ chatRoom: ChatRoom, websocket: SwiftStomp) {
    let viewController = ChatRoomViewController(
      chatRoom,
      "ho",
      websocket
    )
    self.navigationController.pushViewController(viewController, animated: true)
  }
}
