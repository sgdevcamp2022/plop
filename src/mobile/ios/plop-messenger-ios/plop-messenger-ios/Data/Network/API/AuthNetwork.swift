import Foundation
import RxSwift
import RxCocoa

final class AuthNetwork {
  func login(userid: String?, email: String?, password: String) -> Observable<LoginResponse> {
    var jsonBody: [String: Any] = [:]
    
    if let userid = userid {
      jsonBody = [
        "userid": userid,
        "password": password
      ]
    } else if let email = email {
      jsonBody = [
        "email": email,
        "password": password
      ]
    }
    
    let jsonData = try? JSONSerialization.data(withJSONObject: jsonBody)
    guard let request = NetworkHelper.createRequest(
      path: "/auth/v1/login",
      httpMethod: "POST",
      httpBody: jsonData,
      queries: []
    ) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    return URLSession.shared.rx
      .data(request: request)
      .map({ data -> LoginResponse in
        return try JSONDecoder().decode(LoginResponse.self, from: data)
      })
  }
  
  func autoLogin(token: String) -> Observable<LoginResponse> {
    guard var request = NetworkHelper.createRequest(
      path: "/auth/v1/refresh",
      httpMethod: "POST",
      httpBody: nil,
      queries: []
    ) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    request.setValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx
      .data(request: request)
      .map({ data in
        return try JSONDecoder().decode(LoginResponse.self, from: data)
      })
  }
  
  func signout(token: String) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/auth/v1/signout",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: []
    ) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx
      .data(request: request)
      .mapToVoid()
  }
}
