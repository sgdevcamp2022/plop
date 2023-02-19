import RxSwift
import Foundation
import RxCocoa

final class AddFriendViewModel: ViewModelType {
  struct Input {
    let search: Driver<String>
    let searchTrigger: Driver<Void>
    let cancelTrigger: Driver<Void>
    let requestTrigger: Driver<User>
    let cancelRequestTrigger: Driver<User>
  }
  
  struct Output {
    let searchResult: Driver<[AddFriendSection]>
    let cancel: Driver<Void>
    let sendRequest: Driver<Void>
    let cancelRequest: Driver<Void>
  }
  
  private let usecase = UserUseCase()
  private let coordinator: PeopleCoordinator
  private let userRealm = UserRealm()
  
  private var friends = [String]()
  private var receivedList = [String]()
  private var sendedList = [String]()
  
  private let disposeBag = DisposeBag()
  
  init(coordinator: PeopleCoordinator) {
    self.coordinator = coordinator
    
    userRealm.observeFetchAll()
      .subscribe(onNext: { [unowned self] users in
        users.forEach({ user in
          if user.state == .friend { self.friends.append(user.userID) }
          else if user.state == .requestSended { self.sendedList.append(user.userID) }
          else if user.state == .requestReceived { self.receivedList.append(user.userID) }
        })
      })
      .disposed(by: disposeBag)
  }
  
  func transform(_ input: Input) -> Output {
    let currentUser = UserDefaults.standard.string(forKey: "currentEmail") ?? ""
    let searchResult = input.searchTrigger
      .withLatestFrom(input.search)
      .flatMap({ [unowned self] text in
        return self.usecase.search(target: text)
          .map({ users in
            let filteredUsers = users.map({ user in
              var statedUser = user
              if self.receivedList.contains(statedUser.userID) {
                statedUser.state = .requestReceived
              } else if self.sendedList.contains(statedUser.userID) {
                statedUser.state = .requestSended
              } else if self.friends.contains(statedUser.userID) {
                statedUser.state = .friend
              }
              return statedUser
            })
            return filteredUsers.filter({ $0.state != .friend && $0.email != currentUser })
          })
          .map({
            return [
              AddFriendSection(
                headerTitle: "검색 결과",
                items: $0)
            ]
          })
          .asDriverOnErrorJustComplete()
      })
    
    let cancel = input.cancelTrigger
      .map({ [unowned self] in
        self.coordinator.dismiss()
      })
      .asDriver()
    
    let sendRequest = input.requestTrigger
      .flatMapLatest({ [unowned self] user in
        self.usecase.requestFriend(to: user)
          .asDriverOnErrorJustComplete()
      })
    
    let cancelRequest = input.cancelRequestTrigger
      .flatMapLatest({ [unowned self] user in
        self.usecase.cancelRequestFriend(to: user)
          .asDriverOnErrorJustComplete()
      })
    
    return Output(
      searchResult: searchResult,
      cancel: cancel,
      sendRequest: sendRequest,
      cancelRequest: cancelRequest
    )
  }
}
