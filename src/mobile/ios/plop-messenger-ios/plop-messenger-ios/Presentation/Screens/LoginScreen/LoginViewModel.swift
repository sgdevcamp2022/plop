import Foundation
import RxSwift
import RxCocoa

//TODO: - 로그인 화면에서 내부DB 데이터들 다 지워주는 로직 작성해야함.

final class LoginViewModel: ViewModelType {
  struct Input {
    let email: Driver<String>
    let password: Driver<String>
    let loginTrigger: Driver<Void>
    let signupTrigger: Driver<Void>
    let fetchUserTrigger: Driver<Void>
  }
  
  struct Output {
    let buttonEnabled: Driver<Bool>
    let loginResult: Driver<Bool>
    let presentSignup: Driver<Void>
    let fetchUser: Driver<Void>
  }
  
  private let coordinator: SceneCoordinator
  
  private let usecase = AuthUseCase()
  private let userUsecase = UserUseCase()
  
  init(coordinator: SceneCoordinator) {
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
    
    let loginResult = input.loginTrigger
      .withLatestFrom(emailAndPassword)
      .flatMap({ email, password in
        return self.usecase.login(email: email, password: password)
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
    
    let user = input.fetchUserTrigger
      .flatMap({
        return self.userUsecase.fetchCurrentUser()
          .asDriverOnErrorJustComplete()
      })
      .map({
        self.coordinator.toHome()
      })
    
    let presentSignup = input.signupTrigger
      .map({ self.coordinator.toSignup() })
    
    return Output(
      buttonEnabled: buttonEnabled,
      loginResult: loginResult,
      presentSignup: presentSignup,
      fetchUser: user)
  }
}
