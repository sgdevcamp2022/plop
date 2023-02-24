import Foundation
import RxSwift
import RxCocoa

final class PeopleViewModel: ViewModelType {
  struct Input {
    let updateListTrigger: Driver<Void>
    
    let acceptRequestTrigger: Driver<User>
    let rejectRequestTrigger: Driver<User>
    
    let presentAddFriendTrigger: Driver<Void>
  }
  
  struct Output {
    let fetchedList: Driver<[FriendListSection]>
    let updatedList: Driver<Void>
    let acceptRequest: Driver<Void>
    let rejectRequest: Driver<Void>
    let presentAddFriend: Driver<Void>
  }
  
  private let usecase = UserUseCase()
  private let disposeBag = DisposeBag()
  private let coordinator: PeopleCoordinator
  private let userRealm = UserRealm()
  
  init(coordinator: PeopleCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let friendDBListener = userRealm.observeFetchAll()
      .map({ friends in
        return [
          FriendListSection(headerTitle: "친구 요청", items: friends.filter({ $0.state == .requestReceived })),
          FriendListSection(headerTitle: "친구", items: friends.filter({ $0.state == .friend }))
        ]
      })
      .asDriverOnErrorJustComplete()
    
    let zippedNetworkRequest = Observable.zip(
      usecase.fetchFriendList(),
      usecase.fetchFriendRequestList()
      )
    
    let updatedList = input.updateListTrigger
      .flatMap({
        return zippedNetworkRequest
          .mapToVoid()
          .asDriverOnErrorJustComplete()
      })
    
    let acceptRequest = input.acceptRequestTrigger
      .flatMap({ [unowned self] user in
        return self.usecase.acceptRequest(to: user)
          .asDriverOnErrorJustComplete()
      })
    
    let rejectRequest = input.rejectRequestTrigger
      .flatMap({ [unowned self] user in
        return self.usecase.rejectRequest(to: user)
          .asDriverOnErrorJustComplete()
      })
    
    let presentAddFriend = input.presentAddFriendTrigger
      .map({ [unowned self] in
        self.coordinator.presentAddFriendsScreen()
      })
    
    return Output(
      fetchedList: friendDBListener,
      updatedList: updatedList,
      acceptRequest: acceptRequest,
      rejectRequest: rejectRequest,
      presentAddFriend: presentAddFriend)
  }
}
