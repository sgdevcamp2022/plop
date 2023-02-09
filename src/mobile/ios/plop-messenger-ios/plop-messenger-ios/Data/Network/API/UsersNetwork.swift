import Foundation
import RxSwift
import RxCocoa

final class UsersNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func signup(with signupRequest: SignupRequest) -> Observable<SignupResponse> {
    do {
      let requestBody = try JSONEncoder().encode(signupRequest)
      guard let request = NetworkHelper.createRequest(
        path: "/user/v1/signup",
        httpMethod: "POST",
        httpBody: requestBody,
        queries: []
      ) else {
        throw NetworkError.failedToCreateRequest
      }
      
      return URLSession.shared.rx
        .data(request: request)
        .observe(on: scheduler)
        .map({ data in
          return try JSONDecoder().decode(SignupResponse.self, from: data)
        })
    } catch {
      return Observable.error(error)
    }
  }
  
  //TODO: - 이메일 인증
  
  func delete(token: String) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/user",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: []
    ) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
  
  //TODO: - 아이디 찾기
  
  //TODO: - 비밀번호 찾기
  
  func fetchProfile(token: String, email: String?, nickname: String?) -> Observable<ProfileResponse> {
    let queryName: String
    let queryValue: String
    
    if let email = email {
      queryName = "email"
      queryValue = email
    } else if let nickname = nickname {
      queryName = "nickname"
      queryValue = nickname
    } else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/profile/",
      httpMethod: "GET",
      httpBody: nil,
      queries: [
        URLQueryItem(name: queryName, value: queryValue)
      ]) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        return try JSONDecoder().decode(ProfileResponse.self, from: data)
      })
  }
  
  func updateProfile(token: String, to profile: Profile) -> Observable<Void> {
    do {
      let requestData = try JSONEncoder().encode(profile)
      guard var request = NetworkHelper.createRequest(
        path: "/user/v1/profile",
        httpMethod: "PUT",
        httpBody: requestData,
        queries: []) else {
        return Observable.error(NetworkError.failedToCreateRequest)
      }
      
      request.addValue(token, forHTTPHeaderField: "Authorization")
      
      return URLSession.shared.rx.data(request: request)
        .observe(on: scheduler)
        .mapToVoid()
    } catch {
      return Observable.error(error)
    }
  }
  
  func searchUser(token: String, email: String?, nickname: String?) -> Observable<ProfileResponse> {
    let queryName: String
    let queryValue: String
    
    if let email = email {
      queryName = "email"
      queryValue = email
    } else if let nickname = nickname {
      queryName = "nickname"
      queryValue = nickname
    } else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/search/",
      httpMethod: "GET",
      httpBody: nil,
      queries: [
        URLQueryItem(name: queryName, value: queryValue)
      ]) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        return try JSONDecoder().decode(ProfileResponse.self, from: data)
      })
  }
}
