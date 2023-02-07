import Foundation
import RxSwift
import RxCocoa

final class SignupViewModel: ViewModelType {
  struct Input {
    let email: Driver<String>
    let password: Driver<String>
    let cancelTrigger: Driver<Void>
    let signupTrigger: Driver<Void>
  }
  
  struct Output {
    let signupButtonEnabled: Driver<Bool>
    let signupResult: Driver<Bool>
    let dismiss: Driver<Void>
  }
  
  private let usecase = UserUseCase()
  private let coordinator: LoginCoordinator
  
  init(coordinator: LoginCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let emailAndPassword = Driver.combineLatest(input.email, input.password)
    let signupButtonEnabled = emailAndPassword
      .map({ email, password in
        if email != "" && password != "" { return true }
        else { return false }
      })
    
    let signupResult = input.signupTrigger
      .withLatestFrom(emailAndPassword)
      .flatMapLatest({ [unowned self] email, password in
        return self.usecase.mockSignup()
          .map({ result in
            switch result {
            case .success(_):
              return true
            case .failure(_):
              return false
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
