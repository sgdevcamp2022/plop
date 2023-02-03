import Foundation
import RxSwift

final class AuthUseCase {
  private let network = AuthNetwork()
  private let tokenUseCase = TokenUseCase()
  private let userCoreDataUseCase = CDUsersUseCase()
  
  func login(userid: String?, email: String?, password: String) -> Observable<Void> {
    network.login(userid: userid, email: email, password: password)
      .map({ [unowned self] response -> Void in
        if response.message == "success" {
          guard let accessToken = self.tokenUseCase.fetchAccessToken(),
                let refreshToken = self.tokenUseCase.fetchRefreshToken() else {
            self.updateToken(response)
            return
          }
          
          if accessToken != response.accessToken || refreshToken != (response.refreshToken ?? "") {
            self.updateToken(response)
          }
          return
        } else {
          throw UseCaseError.invalidResponse
        }
      })
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
          
          if accessToken != response.accessToken || refreshToken != (response.refreshToken ?? "") {
            self.updateToken(response)
          }
          return
        } else {
          throw UseCaseError.invalidResponse
        }
      })
  }
  
  func signout() -> Observable<Void> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    return network.signout(token: token)
  }
  
  private func updateToken(_ response: LoginResponse) {
    self.tokenUseCase.updateToken(Token(
      uid: "",
      accessToken: response.accessToken,
      refreshToken: response.refreshToken ?? "",
      accessTokenExpiresIn: response.accessTokenExpiresIn,
      refreshTokenExpiresIn: response.refreshTokenExpiresIn ?? 0)
    )
  }
}
