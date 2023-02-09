import Foundation
import RxSwift

final class AuthUseCase {
  private let network = AuthNetwork()
  private let tokenUseCase = TokenUseCase()
  
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
  
  func mockLogin(email: String, password: String) -> Observable<Result<Void, Error>> {
    let loginResponse = LoginResponse(
      result: "success",
      message: "success!!",
      data: LoginData(
        accessToken: "mock-access",
        refreshToken: "mock-refresh",
        accessExpire: 1675352728925,
        refreshExpire: 1675356268925)
    )
    
    if email == "email" && password == "password" {
      guard let accessToken = self.tokenUseCase.fetchAccessToken(),
            let refreshToken = self.tokenUseCase.fetchRefreshToken() else {
        self.updateToken(loginResponse)
        return Observable.just(.success(()))
      }
      if accessToken != loginResponse.data.accessToken ||
          refreshToken != (loginResponse.data.refreshToken ?? "") {
        self.updateToken(loginResponse)
      }
      return Observable.just(.success(()))
    } else {
      return Observable.just(.failure(UseCaseError.invalidResponse))
    }
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
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.just(
        .failure(UseCaseError.invalidResponse))
    }
    
    print(token)
    
    // 만료
    if token != "mock-access" {
      return Observable.just(.failure(UseCaseError.invalidResponse))
    } else {
      return Observable.just(.success(()))
    }
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
