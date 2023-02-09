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
    let signupResult: Driver<Void>
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
        return self.usecase.mockSignup(success: true)
          .map({ result in
            switch result {
            case .success(_):
              self.coordinator.presentAlert(
                title: "회원가입 성공!",
                message: "로그인 화면에서 로그인 해주세요!",
                dismiss: true)
            case .failure(_):
              self.coordinator.presentAlert(
                title: "회원가입 실패",
                message: "가입에 실패했습니다.\n다시 시도해주세요.",
                dismiss: false)
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
