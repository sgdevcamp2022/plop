import Foundation
import Moya
import RxSwift

final class AuthUseCase {
  private let network = AuthNetwork()
  private let tokenUseCase = TokenUseCase()
  private let userRealm = UserRealm()
  
  func login(email: String, password: String) -> Observable<Result<Void, Error>> {
    return network.login(email: email, password: password)
      .map({ result in
        switch result {
        case .success(let token):
          self.tokenUseCase.updateToken(token)
          UserDefaults.standard.set(email, forKey: "currentEmail")
          return .success(())
        case .failure(let error):
          return .failure(error)
        }
      })
  }
  
  func reissue(email: String) -> Observable<Result<Void, Error>> {
    return network.reissue(email: email)
      .map({ result in
        switch result {
        case .success(let token):
          self.tokenUseCase.updateToken(token)
          return .success(())
        case .failure(let error):
          return .failure(error)
        }
      })
  }
  
  // Network logout -> realm delete
  func logout() -> Observable<Void> {
    return network.logout()
      .flatMap({ result in
        switch result {
        case .success(let userID):
          return self.userRealm.delete(userID)
        case .failure(let error):
          throw error
        }
      })
  }
  
  // Only network call
  func signup(
    user: User,
    password: String
  ) -> Observable<Result<String, Error>> {
    let requestForm = SignupRequest(
      userID: user.userID,
      email: user.email,
      nickname: user.profile.nickname,
      password: password)
    
    return network.signup(form: requestForm)
      .map({ result in
        switch result {
        case .success(let user):
          return user.userID
        case .failure(let error):
          return error.localizedDescription
        }
      })
      .asResult()
  }
  
  func requestVerifyCode(
    email: String,
    userID: String
  ) -> Observable<Result<Void, Error>> {
    return network.requestVerifyCode(email: email, userID: userID)
      .asObservable()
      .asResult()
  }
  
  func verifyCode(
    email: String,
    userID: String,
    code: String
  ) -> Observable<Result<Void, Error>> {
    return network.verifyCode(email: email, userID: userID, code: code)
      .asObservable()
      .asResult()
  }
  
  // Network call -> delete in realm
  func withdrawal() -> Observable<Void> {
    return network.withdrawal()
      .flatMap({ result in
        switch result {
        case .success(let userID):
          return self.userRealm.delete(userID)
        case .failure(let error):
          throw error
        }
      })
  }
}
