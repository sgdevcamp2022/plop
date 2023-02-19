import Foundation
import RxSwift
import RxCocoa

final class SignupViewModel: ViewModelType {
  struct Input {
    let userID: Driver<String>
    let email: Driver<String>
    let nickname: Driver<String>
    let password: Driver<String>
    let cancelTrigger: Driver<Void>
    let signupTrigger: Driver<Void>
  }
  
  struct Output {
    let signupButtonEnabled: Driver<Bool>
    let signupResult: Driver<String>
    let dismiss: Driver<Void>
  }
  
  private let usecase = AuthUseCase()
  private let coordinator: SceneCoordinator
  
  init(coordinator: SceneCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let signupForm = Driver.combineLatest(
      input.userID,
      input.email,
      input.nickname,
      input.password
    )
    
    let signupButtonEnabled = signupForm
      .map({ userID, email, nickname, password in
        if userID != "" && email != "" && nickname != "" && password != "" {
          return true
        } else {
          return false
        }
      })
    
    let signupResult = input.signupTrigger
      .withLatestFrom(signupForm)
      .flatMap({ [unowned self] userID, email, nickname, password in
        let user = User(
          userID: userID,
          email: email,
          state: .none,
          profile: Profile(nickname: nickname, imageURL: ""))
        
        return self.usecase.signup(
          user: user,
          password: password)
        .subscribe(on: MainScheduler.instance)
        .map({ result in
          switch result {
          case .success(let userID):
            return userID
          case .failure(_):
            return ""
          }
        })
        .asDriverOnErrorJustComplete()
      })
    
    let dismiss = input.cancelTrigger
      .asDriver()
    
    return Output(
      signupButtonEnabled: signupButtonEnabled,
      signupResult: signupResult,
      dismiss: dismiss
    )
  }
}
