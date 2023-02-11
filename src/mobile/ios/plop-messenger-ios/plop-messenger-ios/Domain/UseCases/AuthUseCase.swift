import Foundation
import RxSwift

final class AuthUseCase {
  private let network = AuthNetwork()
  private let tokenUseCase = TokenUseCase()
  
  func login(
    email: String,
    password: String
  ) -> Observable<Result<Void, Error>> {
    return network.login(email: email, password: password)
      .map({ [unowned self] result in
        switch result {
        case .success(let token):
          self.tokenUseCase.updateToken(token)
          return .success(())
        case .failure(let error):
          return .failure(error)
        }
      })
  }
  
  func autoLogin(email: String) -> Observable<Result<Void, Error>> {
    guard let refreshToken = tokenUseCase.fetchRefreshToken() else {
      return Observable.just(.failure(TokenError.failedToFetchRefreshToken))
    }
    
    return network.autoLogin(refreshToken: refreshToken, email: email)
      .map({ [unowned self] result in
        switch result {
        case .success(let token):
          self.tokenUseCase.updateToken(token)
          return .success(())
        case .failure(let error):
          return .failure(error)
        }
      })
  }
  
  func logout() -> Observable<Void> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    
    return network.logout(accessToken: accessToken)
  }
  
  func signup(
    user: User,
    password: String
  ) -> Observable<Result<String, Error>> {
    let requestForm = SignupRequest(
      userID: user.userID,
      email: user.email,
      nickname: user.profile.nickname,
      password: password)
    
    return network.signup(with: requestForm)
      .map({ result in
        switch result {
        case .success(let user):
          return .success(user.userID)
        case .failure(let error):
          return .failure(error)
        }
      })
  }
  
  func requestVerifyCode(
    email: String,
    userID: String
  ) -> Observable<Result<Void, Error>> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.just(.failure(TokenError.failedToFetchAccessToken))
    }
    
    return network.requestVerifyCode(
      email: email,
      userID: userID,
      accessToken: accessToken
    )
  }
  
  func verifyCode(
    email: String,
    userID: String,
    code: String
  ) -> Observable<Result<Void, Error>> {
    return network.verifyCode(email: email, userID: userID, code: code)
  }
}
