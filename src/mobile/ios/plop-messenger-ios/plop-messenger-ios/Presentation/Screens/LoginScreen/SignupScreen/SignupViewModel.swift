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
    let signupResult: Driver<Void>
    let dismiss: Driver<Void>
  }
  
  private let usecase = AuthUseCase()
  private let coordinator: LoginCoordinator
  
  init(coordinator: LoginCoordinator) {
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
      .flatMapLatest({ [unowned self] userID, email, nickname, password in
        let user = User(
          userID: userID,
          email: email,
          profile: Profile(nickname: nickname, imageURL: "")
        )
        return self.usecase.signup(user: user, password: password)
          .map({ result in
            switch result {
            case .success(let message):
              print(message)
              self.coordinator.dismissSignupScreen()
            case .failure(_):
              break
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
