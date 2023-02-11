import Foundation
import RxSwift
import RxCocoa

final class AuthNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func login(
    email: String,
    password: String
  ) -> Observable<Result<Token, Error>> {
    let body: [String: Any] = [
      "email": email,
      "password": password
    ]
    
    guard let data = try? JSONSerialization.data(withJSONObject: body) else {
      return Observable.just(.failure(NetworkError.failedToLogin))
    }
    
    guard let request = NetworkHelper.createRequest(
      path: "/auth/v1/login",
      httpMethod: "POST",
      httpBody: data,
      queries: []
    ) else {
      return Observable.just(.failure(NetworkError.failedToLogin))
    }
    
    return URLSession.shared.rx
      .data(request: request)
      .observe(on: scheduler)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            LoginResponse.self, from: data)
          
          if response.message == "SUCCESS" {
            return .success(response.data.toDomain())
          }
          throw NetworkError.failedToLogin
        } catch {
          return .failure(error)
        }
      })
  }
  
  func autoLogin(
    refreshToken: String,
    email: String
  ) -> Observable<Result<Token, Error>> {
    let body: [String: Any] = ["email": email]
    guard let data = try? JSONSerialization.data(withJSONObject: body) else {
      return Observable.just(.failure(NetworkError.failedToAutoLogin))
    }
    
    guard var request = NetworkHelper.createRequest(
      path: "/auth/v1/reissue",
      httpMethod: "POST",
      httpBody: data,
      queries: []
    ) else {
      return Observable.just(.failure(NetworkError.failedToAutoLogin))
    }
    
    request.setValue(
      refreshToken,
      forHTTPHeaderField: "Authorization"
    )
    
    return URLSession.shared.rx
      .data(request: request)
      .observe(on: scheduler)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            LoginResponse.self,
            from: data)
          
          if response.message == "SUCCESS" {
            return .success(response.data.toDomain())
          }
          throw NetworkError.failedToAutoLogin
        } catch {
          return .failure(error)
        }
      })
  }
  
  func logout(accessToken: String) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/auth/v1/logout",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: []
    ) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    request.addValue(
      accessToken,
      forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx
      .data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
  
  func signup(
    with signupRequest: SignupRequest
  ) -> Observable<Result<User, Error>> {
    guard let body = try? JSONEncoder().encode(signupRequest) else {
      return Observable.just(.failure(NetworkError.failedToSignup))
    }
    
    guard let request = NetworkHelper.createRequest(
      path: "/auth/v1/signup",
      httpMethod: "POST",
      httpBody: body,
      queries: []
    ) else {
      return Observable.just(.failure(NetworkError.failedToSignup))
    }
    
    return URLSession.shared.rx
      .data(request: request)
      .observe(on: scheduler)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            SignupResponse.self,
            from: data)
          
          if response.message == "SUCCESS" {
            return .success(response.data.toDomain())
          }
          throw NetworkError.failedToSignup
        } catch {
          return .failure(error)
        }
      })
  }
  
  func requestVerifyCode(
    email: String,
    userID: String,
    accessToken: String
  ) -> Observable<Result<Void, Error>> {
    let body: [String: Any] = [
      "email": email,
      "userId": userID
    ]
    let data = try? JSONSerialization.data(withJSONObject: body)
    
    guard var request = NetworkHelper.createRequest(
      path: "/auth/v1/email/code",
      httpMethod: "POST",
      httpBody: data,
      queries: []
    ) else {
      return Observable.just(.failure(NetworkError.failedToCreateRequest))
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
      .asResult()
  }
  
  func verifyCode(
    email: String,
    userID: String,
    code: String
  ) -> Observable<Result<Void, Error>> {
    let body: [String: Any] = [
      "email": email,
      "userId": userID,
      "verificationCode": code
    ]
    
    let data = try? JSONSerialization.data(withJSONObject: body)
    
    guard let request = NetworkHelper.createRequest(
      path: "/auth/v1/email/verify",
      httpMethod: "POST",
      httpBody: data,
      queries: []
    ) else {
      return Observable.just(.failure(NetworkError.invalidVerifyCode))
    }
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
      .asResult()
  }
  
  func withdrawal(accessToken: String) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/auth/v1/withdrawal",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: []
    ) else {
      return Observable.error(NetworkError.failedWithdrawal)
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .mapToVoid()
  }
  
  //TODO: - 아이디 찾기
  
  //TODO: - 비밀번호 찾기
}
