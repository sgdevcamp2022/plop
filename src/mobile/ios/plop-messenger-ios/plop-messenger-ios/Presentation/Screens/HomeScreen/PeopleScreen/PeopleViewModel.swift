import Foundation
import RxSwift
import RxCocoa

final class PeopleViewModel: ViewModelType {
  struct Input {
    let requestFriendListTrigger: Driver<Void>
    let friendsListTrigger: Driver<Void>
    let addFriendsTrigger: Driver<Void>
    let requestFriendRespondTrigger: Driver<String>
  }
  
  struct Output {
    let requestFriendList: Driver<[User]>
    let friends: Driver<[User]>
    let presentAddFriends: Driver<Void>
    let requestResponse: Driver<Void>
  }
  
  private let usecase = UserUseCase()
  private let disposeBag = DisposeBag()
  private let coordinator: PeopleCoordinator
  
  init(coordinator: PeopleCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let requestFriends = input.requestFriendListTrigger
      .flatMap({
        return self.usecase.fetchFriendRequestList()
          .asDriverOnErrorJustComplete()
      })
    
    let friends = input.friendsListTrigger
      .flatMap({ [unowned self] in
        return self.usecase.fetchFriendList()
          .asDriverOnErrorJustComplete()
      })
  
    let presentAddFriends = input.addFriendsTrigger
      .map({ [unowned self] in
        self.coordinator.presentAddFriendsScreen()
      })
    
    let requestResponse = input.requestFriendRespondTrigger
      .flatMap({ [unowned self] id in
        if id == "" {
          return self.usecase.respondToFriendRequest(
            to: id,
            method: "DELETE")
          .asDriverOnErrorJustComplete()
        }
        else {
          return self.usecase.respondToFriendRequest(
            to: id,
            method: "POST")
          .asDriverOnErrorJustComplete()
        }
      })
    
    return Output(
      requestFriendList: requestFriends,
      friends: friends,
      presentAddFriends: presentAddFriends,
      requestResponse: requestResponse
    )
  }
}
