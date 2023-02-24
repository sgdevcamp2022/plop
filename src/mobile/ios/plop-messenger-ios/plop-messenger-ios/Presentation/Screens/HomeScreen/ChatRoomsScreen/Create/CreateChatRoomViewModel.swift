import Foundation
import RxCocoa
import RxSwift

final class CreateChatRoomViewModel: ViewModelType {
  struct Input {
    let updateListTrigger: Driver<IndexPath?>
    let cancelTrigger: Driver<Void>
    let inviteTrigger: Driver<Void>
  }
  
  struct Output {
    let updateList: Driver<[CreateChatRoomSection]>
    let cancel: Driver<Void>
    let invite: Driver<Void>
  }
  
  private let userRealm = UserRealm()
  
  private var friendsToInvite = [User]()
  private var friends = [User]()
  
  private let disposeBag = DisposeBag()
  
  private let coordinator: ChatRoomListCoordinator
  
  init(coordinator: ChatRoomListCoordinator) {
    self.coordinator = coordinator
    
    userRealm.observeFetchAll()
      .map({ users in
        let filteredUsers = users.filter({ $0.state == .friend })
        self.friends = filteredUsers
      })
      .subscribe()
      .disposed(by: disposeBag)
  }
    
  func transform(_ input: Input) -> Output {
    let updateList = input.updateListTrigger
      .map({ [unowned self] indexPath in
        if let indexPath = indexPath {
          if indexPath.section == 0 {
            // remove
            let user = self.friendsToInvite[indexPath.row]
            self.friendsToInvite.remove(at: indexPath.row)
            self.friends.append(user)
          } else {
            //invite
            let user = self.friends[indexPath.row]
            self.friends.remove(at: indexPath.row)
            self.friendsToInvite.append(user)
          }
        }
        return [
          CreateChatRoomSection(
            headerTitle: "초대", items: self.friendsToInvite),
          CreateChatRoomSection(
            headerTitle: "친구 목록", items: self.friends)
        ]
      })
    
    let cancel = input.cancelTrigger
      .map({ self.coordinator.dismiss() })
    
    let invite = input.inviteTrigger
      .map({ [unowned self] in
        if self.friendsToInvite.count == 1 {
          self.coordinator.dismissAndpushChatRoom("DM", members: friendsToInvite)
        } else {
          self.coordinator.dismissAndpushChatRoom("GROUP", members: friendsToInvite)
        }
      })
  
    return Output(
      updateList: updateList,
      cancel: cancel,
      invite: invite
    )
  }
}
