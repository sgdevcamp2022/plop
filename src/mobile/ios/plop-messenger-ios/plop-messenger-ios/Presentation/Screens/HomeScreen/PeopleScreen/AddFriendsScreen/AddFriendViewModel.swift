import RxSwift
import RxCocoa

final class AddFriendViewModel: ViewModelType {
  struct Input {
    let search: Driver<String>
    let searchTrigger: Driver<Void>
    let cancelTrigger: Driver<Void>
    let requestTrigger: Driver<String>
  }
  
  struct Output {
    let searchResult: Driver<[User]>
    let cancel: Driver<Void>
    let requestResult: Driver<Void>
  }
  
  private let usecase = UserUseCase()
  private let coordinator: PeopleCoordinator
  
  init(coordinator: PeopleCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let searchResult = input.searchTrigger
      .withLatestFrom(input.search)
      .flatMap({ [unowned self] text in
        return self.usecase.search(target: text)
          .asDriverOnErrorJustComplete()
      })
    
    let cancel = input.cancelTrigger
      .map({ [unowned self] in
        self.coordinator.dismiss()
      })
      .asDriver()
    
    let request = input.requestTrigger
      .flatMap({ [unowned self] target in
        if target == "" {
          return self.usecase.cancelRequestFriend(to: target)
            .asDriverOnErrorJustComplete()
        } else {
          return self.usecase.requestFriend(to: target)
            .asDriverOnErrorJustComplete()
        }
      })
    
    return Output(
      searchResult: searchResult,
      cancel: cancel,
      requestResult: request
    )
  }
}
