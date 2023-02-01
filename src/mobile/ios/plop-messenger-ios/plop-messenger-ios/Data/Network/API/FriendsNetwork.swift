import Foundation
import RxSwift
import RxCocoa

final class FriendsNetwork {
  func fetchFriendsList(token: String) -> Observable<FriendsListResponse> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend",
      httpMethod: "GET",
      httpBody: nil,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .map({ data in
        return try JSONDecoder().decode(FriendsListResponse.self, from: data)
      })
  }
  
  func sendRequest(token: String, to friendID: String) -> Observable<Void> {
    let json: [String: Any] = [
      "friendid" : friendID
    ]
    let body = try? JSONSerialization.data(withJSONObject: json)
    
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend",
      httpMethod: "POST",
      httpBody: body,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .mapToVoid()
  }
  
  func cancelRequest(token: String, to friendID: String) -> Observable<Void> {
    let json: [String: Any] = [
      "friendid" : friendID
    ]
    
    do {
      let body = try? JSONSerialization.data(withJSONObject: json)
      
      guard var request = NetworkHelper.createRequest(
        path: "/user/v1/friend",
        httpMethod: "DELETE",
        httpBody: body,
        queries: []) else {
        throw NetworkError.failedToCreateRequest
      }
      request.addValue(token, forHTTPHeaderField: "Authorization")
      
      return URLSession.shared.rx.data(request: request)
        .mapToVoid()
    } catch {
      return Observable.error(error)
    }
    
  }
  
  func delete(token: String, friendID: String) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: [
        URLQueryItem(name: "friendid", value: friendID)
      ]) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .mapToVoid()
  }
  
  func acceptFriend(token: String, friendID: String, accept: Bool) -> Observable<Void> {
    let json: [String: Any] = [
      "friendid": friendID,
      "status": accept
    ]
    
    do {
      let data = try JSONSerialization.data(withJSONObject: json)
      guard var request = NetworkHelper.createRequest(
        path: "/user/v1/friend",
        httpMethod: "PUT",
        httpBody: data,
        queries: [
          URLQueryItem(name: "friendid", value: friendID)
        ]) else {
        throw NetworkError.failedToCreateRequest
      }
      request.addValue(token, forHTTPHeaderField: "Authorization")
      
      return URLSession.shared.rx.data(request: request)
        .mapToVoid()
      
    } catch {
      return Observable.error(error)
    }
    
  }
  
  func friendRequestsList(token: String) -> Observable<FriendsListResponse> {
    guard var request = NetworkHelper.createRequest(
      path: "/user/v1/friend",
      httpMethod: "GET",
      httpBody: nil,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .map({ data in
        return try JSONDecoder().decode(FriendsListResponse.self, from: data)
      })
  }
}
