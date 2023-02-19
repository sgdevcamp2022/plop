import RxSwift
import RxCocoa
import Foundation

final class LaunchViewModel: ViewModelType {
  struct Input {
    let autoLoginTrigger: Driver<Void>
  }
  
  struct Output {
    let autoLoginResult: Driver<Void>
  }
  
  private let coordinator: SceneCoordinator
  private let usecase = AuthUseCase()
  private let userUsecase = UserUseCase()
  
  init(coordinator: SceneCoordinator) {
    self.coordinator = coordinator
  }
  
  func transform(_ input: Input) -> Output {
    let email = UserDefaults.standard.string(forKey: "currentEmail")
    
    let autoLoginResult = input.autoLoginTrigger
      .flatMap({
        return self.usecase.reissue(email: email ?? "")
          .observe(on: MainScheduler.instance)
          .map({ [unowned self] result in
            switch result {
            case .success(_):
              self.coordinator.toHome()
              return
            case .failure(_):
              self.coordinator.toLogin()
            }
          })
          .asDriverOnErrorJustComplete()
      })
      
    return Output(autoLoginResult: autoLoginResult)
  }
}
