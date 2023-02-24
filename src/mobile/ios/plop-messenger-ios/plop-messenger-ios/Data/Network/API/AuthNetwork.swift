import Foundation
import RxSwift
import RxCocoa
import Moya
import RxMoya

final class AuthNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  private let keychainService = KeychainService.shared
  
  private lazy var provider: MoyaProvider<AuthTarget> = {
    guard let accessToken = keychainService.fetchAccessToken() else {
      return MoyaProvider<AuthTarget>()
    }
    let authPlugin = AccessTokenPlugin { _ in accessToken}
    let provider = MoyaProvider<AuthTarget>(plugins: [authPlugin])
    return provider
  }()
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func login(
    email: String,
    password: String
  ) -> Observable<Result<Token, Error>> {
    let request = LoginRequest(idOrEmail: email, password: password)
    return provider.rx.request(.login(request))
      .observe(on: scheduler)
      .map(LoginResponse.self)
      .asObservable()
      .map({ response in
        if response.result == "SUCCESS" {
          return response.data.toDomain()
        } else {
          throw NetworkError.failedToLogin
        }
      })
      .asResult()
  }
  
  func reissue(email: String) -> Observable<Result<Token, Error>> {
    return provider.rx.request(.reissue(email))
      .observe(on: scheduler)
      .map(LoginResponse.self)
      .asObservable()
      .map({ response in
        if response.result == "SUCCESS" {
          return response.data.toDomain()
        } else {
          throw NetworkError.failedToAutoLogin
        }
      })
      .asResult()
  }
  
  func logout() -> Observable<Result<String, Error>> {
    return provider.rx.request(.logout)
      .observe(on: scheduler)
      .map(LogoutResponse.self)
      .asObservable()
      .map({ response in
        if response.result == "SUCCESS" { return response.data.userID }
        else { throw NetworkError.failedToLogout}
      })
      .asResult()
  }
  
  func signup(form request: SignupRequest) -> Observable<Result<User, Error>> {
    return provider.rx.request(.signup(request))
      .observe(on: scheduler)
      .map(SignupResponse.self)
      .asObservable()
      .map({ response in
        if response.result == "SUCCESS" { return response.data.toDomain() }
        else { throw NetworkError.failedToSignup }
      })
      .asResult()
  }
  
  func requestVerifyCode(
    email: String,
    userID: String
  ) -> Single<Void> {
    return provider.rx.request(.requestVerifyCode(email, userID))
      .map({ _ in () })
  }
  
  func verifyCode(
    email: String,
    userID: String,
    code: String
  ) -> Single<Void> {
    return provider.rx.request(.verifyCode(email, userID, code))
      .observe(on: scheduler)
      .map({ _ in () })
  }
  
  func withdrawal() -> Observable<Result<String, Error>> {
    return provider.rx.request(.withdrawal)
      .observe(on: scheduler)
      .map(WithdrawalResponse.self)
      .asObservable()
      .map({ response in
        if response.result == "SUCCESS" { return response.data.userID }
        else { throw NetworkError.failedWithdrawal }
      })
      .asResult()
  }
  
  //TODO: - 아이디 찾기
  
  //TODO: - 비밀번호 찾기
}
