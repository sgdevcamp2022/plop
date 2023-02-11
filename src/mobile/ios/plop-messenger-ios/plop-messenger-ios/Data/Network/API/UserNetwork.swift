import Foundation
import RxSwift
import RxCocoa

final class UserNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  //MARK: - Profile related
  func fetchUserInfo(
    accessToken: String,
    target: String
  ) -> Observable<Result<User, Error>> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/profile",
      httpMethod: "GET",
      httpBody: nil,
      queries: [
        URLQueryItem(name: "target", value: target)
      ]) else {
      return Observable.just(.failure(NetworkError.failedFetchUserInfo))
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            UserResponse.self, from: data)
          if response.message == "SUCCESS" {
            return .success(response.data.toDomain())
          } else {
            return .failure(NetworkError.failedFetchUserInfo)
          }
        } catch {
          return .failure(error)
        }
      })
  }
  
  func updateUserInfo(
    accessToken: String,
    target: String,
    updatedProfile: Profile
  ) -> Observable<Result<User, Error>> {
    do {
      let encodedData = try JSONEncoder().encode(updatedProfile.toEncodable())
      guard var request = NetworkHelper.createRequest(
        path: "/user/v1/profile",
        httpMethod: "PUT",
        httpBody: encodedData,
        queries: [
          URLQueryItem(name: "target", value: target)
        ]) else {
        throw NetworkError.failedToUpdateUserInfo
      }
      
      request.setValue(accessToken, forHTTPHeaderField: "Authorization")
      
      return URLSession.shared.rx.data(request: request)
        .observe(on: scheduler)
        .map({ data in
          let response = try JSONDecoder().decode(
            UserResponse.self, from: data)
          if response.message == "SUCCESS" {
            return .success(response.data.toDomain())
          }
          throw NetworkError.failedToUpdateUserInfo
        })
    } catch {
      return Observable.just(.failure(error))
    }
  }
  
  func searchUser(
    accessToken: String,
    target: String
  ) -> Observable<Result<[User], Error>> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/search",
      httpMethod: "GET",
      httpBody: nil,
      queries: [
        URLQueryItem(name: "target", value: target)
      ]) else {
      return Observable.just(.failure(NetworkError.failedToSearchUser))
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            UserListResponse.self, from: data)
          
          if response.message == "SUCCESS" {
            return .success(response.toDomain())
          }
          throw NetworkError.failedToSearchUser
        } catch {
          return .failure(error)
        }
      })
  }
  
  //MARK: - Friends related
  func fetchFriendList(
    accessToken: String
  ) -> Observable<Result<[User], Error>> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend",
      httpMethod: "GET",
      httpBody: nil,
      queries: []
    ) else {
      return Observable.just(.failure(NetworkError.failedToFetchFriendList))
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            UserListResponse.self, from: data)
          if response.message == "SUCCESS" {
            return .success(response.toDomain())
          }
          throw NetworkError.failedToFetchFriendList
        } catch {
          return .failure(error)
        }
      })
  }
  
  func requestFriend(
    accessToken: String,
    target: String
  ) -> Observable<Void> {
    let body: [String: Any] = ["target": target]
    guard let data = try? JSONSerialization.data(withJSONObject: body) else {
      return Observable.error(NetworkError.failedToRequestFriend)
    }
    
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend/request",
      httpMethod: "POST",
      httpBody: data,
      queries: []) else {
      return Observable.error(NetworkError.failedToRequestFriend)
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
  
  func cancelRequestFriend(
    accessToken: String,
    target: String
  ) -> Observable<Void> {
    let body: [String: Any] = ["target": target]
    guard let data = try? JSONSerialization.data(withJSONObject: body) else {
      return Observable.error(NetworkError.failedToRequestFriend)
    }
    
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend/request",
      httpMethod: "DELETE",
      httpBody: data,
      queries: []) else {
      return Observable.error(NetworkError.failedToRequestFriend)
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
  
  func friendRequestList(
    accessToken: String
  ) -> Observable<Result<[User], Error>> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend/request",
      httpMethod: "GET",
      httpBody: nil,
      queries: []) else {
      return Observable.just(.failure(NetworkError.failedToFetchFriendRequestList))
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .map({ data in
        do {
          let response = try JSONDecoder().decode(
            UserListResponse.self,
            from: data)
          
          if response.message == "SUCCESS" {
            return .success(response.toDomain())
          }
          throw NetworkError.failedToFetchFriendRequestList
        } catch {
          return .failure(error)
        }
      })
  }
  
  func friendDelete(
    accessToken: String,
    friendID: String
  ) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: [
        URLQueryItem(name: "friendid", value: friendID)
      ]) else {
      return Observable.error(NetworkError.failedToDeleteFriend)
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
  
  func respondToFriendRequest(
    accessToken: String,
    target: String,
    method: String
  ) -> Observable<Void> {
    let body: [String: Any] = ["target": target]
    guard let data = try? JSONSerialization.data(withJSONObject: body) else {
      return Observable.error(NetworkError.failedToRespondToRequest)
    }
    
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend/response",
      httpMethod: method,
      httpBody: data,
      queries: []) else {
      return Observable.error(NetworkError.failedToRespondToRequest)
    }
    
    request.setValue(accessToken, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
}
