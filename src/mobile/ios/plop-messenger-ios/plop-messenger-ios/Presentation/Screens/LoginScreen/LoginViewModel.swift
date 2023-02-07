import Foundation
import RxSwift
import RxCocoa

final class LoginViewModel: ViewModelType {
  struct Input {
    let email: Driver<String>
    let password: Driver<String>
    let loginTrigger: Driver<Void>
    let signupTrigger: Driver<Void>
  }
  
  struct Output {
    let buttonEnabled: Driver<Bool>
    let loginSuccess: Driver<Void>
    let presentSignup: Driver<Void>
  }
  
  private let usecase = AuthUseCase()
  private let coordinator: LoginCoordinator
  
  init(coordinator: LoginCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let emailAndPassword = Driver.combineLatest(input.email, input.password)
    
    let buttonEnabled = emailAndPassword
      .map({ email, password in
        if email != "" && password != "" {
          return true
        } else {
          return false
        }
      })
    
    let login = input.loginTrigger.withLatestFrom(emailAndPassword)
      .flatMapLatest({ [unowned self] email, password in
        return self.usecase.mockLogin()
          .map({ result in
            switch result {
            case .success(_):
              self.coordinator.toHome()
            case .failure(_):
              //TODO: - Error handling
              break
            }
          })
          .asDriver(onErrorJustReturn: ())
      })
    
    let presentSignup = input.signupTrigger
      .map({ self.coordinator.toSignup() })
    
    return Output(
      buttonEnabled: buttonEnabled,
      loginSuccess: login,
      presentSignup: presentSignup)
  }
}
