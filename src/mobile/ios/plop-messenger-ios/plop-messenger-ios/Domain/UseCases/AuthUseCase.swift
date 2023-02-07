import Foundation
import RxSwift

final class AuthUseCase {
  private let network = AuthNetwork()
  private let tokenUseCase = TokenUseCase()
  private let userCoreDataUseCase = CDUsersUseCase()
  
  func login(
    userid: String?,
    email: String?,
    password: String
  ) -> Observable<Result<Void, Error>> {
    network.login(userid: userid, email: email, password: password)
      .map({ [unowned self] response -> Void in
        if response.message == "success" {
          guard let accessToken = self.tokenUseCase.fetchAccessToken(),
                let refreshToken = self.tokenUseCase.fetchRefreshToken() else {
            self.updateToken(response)
            return
          }
          
          if accessToken != response.data.accessToken ||
              refreshToken != (response.data.refreshToken ?? "") {
            self.updateToken(response)
          }
          return
        } else {
          throw UseCaseError.invalidResponse
        }
      })
      .asResult()
  }
  
  func mockLogin() -> Observable<Result<Void, Error>> {
    return Observable.just(.success(()))
  }
  
  func autoLogin() -> Observable<Void> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    
    return network.autoLogin(token: token)
      .map({ [unowned self] response -> Void in
        if response.message == "success" {
          guard let accessToken = self.tokenUseCase.fetchAccessToken(),
                let refreshToken = self.tokenUseCase.fetchRefreshToken() else {
            self.updateToken(response)
            return
          }
          
          if accessToken != response.data.accessToken ||
              refreshToken != (response.data.refreshToken ?? "") {
            self.updateToken(response)
          }
          return
        } else {
          throw UseCaseError.invalidResponse
        }
      })
  }
  
  func mockAutoLogin() -> Observable<Result<Void, Error>> {
    return Observable.just(.failure(UseCaseError.invalidResponse))
  }
  
  func signout() -> Observable<Void> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    return network.signout(token: token)
  }
  
  private func updateToken(_ response: LoginResponse) {
    self.tokenUseCase.updateToken(Token(
      accessToken: response.data.accessToken,
      refreshToken: response.data.refreshToken ?? "",
      accessExpire: response.data.accessExpire,
      refreshExpire: response.data.refreshExpire ?? 0)
    )
  }
}
