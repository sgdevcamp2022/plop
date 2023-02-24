import Foundation
import RxSwift
import RxCocoa
import Moya
import RxMoya

final class UserNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  private let keychainService = KeychainService.shared
  private lazy var provider: MoyaProvider<UserTarget> = {
    guard let accessToken = keychainService.fetchAccessToken() else {
      return MoyaProvider<UserTarget>()
    }
    let authPlugin = AccessTokenPlugin { _ in accessToken}
    let provider = MoyaProvider<UserTarget>(plugins: [authPlugin])
    return provider
  }()
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func fetchUserInfo(
    _ user: User
  ) -> Observable<Result<User, Error>> {
    return provider.rx.request(.fetchUserInfo(user.email))
      .observe(on: scheduler)
      .map(UserResponse.self)
      .map({ response in
        if response.result == "SUCCESS" {
          return response.data.toDomain(state: user.state)
        } else {
          throw NetworkError.failedFetchUserInfo
        }
      })
      .asObservable()
      .asResult()
  }
  
  func fetchCurrentUser(target: String) -> Observable<Result<User, Error>> {
    return provider.rx.request(.fetchUserInfo(target))
      .observe(on: scheduler)
      .map(UserResponse.self)
      .map({ response in
        if response.result == "SUCCESS" {
          UserDefaults.standard.set(
            response.data.email,
            forKey: "currentEmail"
          )
          UserDefaults.standard.set(
            response.data.userID,
            forKey: "currentUserID"
          )
          return response.data.toDomain(state: .current)
        } else {
          throw NetworkError.failedFetchUserInfo
        }
      })
      .asObservable()
      .asResult()
  }
  
  func updateUserInfo(
    target: String,
    updatedProfile: Profile
  ) -> Observable<Result<User, Error>> {
    return provider.rx.request(.updateUserInfo(target, updatedProfile))
      .observe(on: scheduler)
      .map(UserResponse.self)
      .map({ response in
        if response.message == "SUCCESS" {
          return response.data.toDomain(state: .current)
        }
        throw NetworkError.failedToUpdateUserInfo
      })
      .asObservable()
      .asResult()
  }
  
  func searchUser(target: String) -> Observable<Result<[User], Error>> {
    return provider.rx.request(.search(target))
      .observe(on: scheduler)
      .map(UserListResponse.self)
      .map({ response in
        if response.result == "SUCCESS" {
          return response.toDomain(state: .notFriend)
        }
        throw NetworkError.failedToSearchUser
      })
      .asObservable()
      .asResult()
  }
  
  //MARK: - Friends related
  func fetchFriendList() -> Observable<Result<[User], Error>> {
    return provider.rx.request(.fetchFriendList)
      .observe(on: scheduler)
      .map({ response in
        do {
          let users = try JSONDecoder().decode(UserListResponse.self, from: response.data)
          return users.toDomain(state: .friend)
        } catch {
          throw error
        }
      })
      .asObservable()
      .asResult()
  }
  
  func requestFriend(
    target: String
  ) -> Observable<Void> {
    return provider.rx.request(.requestFriend(target))
      .observe(on: scheduler)
      .asObservable()
      .map(FriendRequestResponse.self)
      .mapToVoid()
  }
  
  func cancelRequestFriend(
    target: String
  ) -> Observable<Void> {
    return provider.rx.request(.cancelRequest(target))
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
  }
  
  func friendRequestList() -> Observable<Result<[User], Error>> {
    return provider.rx.request(.friendRequestList)
      .observe(on: scheduler)
      .map(UserListResponse.self)
      .map({ response in
        if response.result == "SUCCESS" {
          return response.toDomain(state: .requestReceived)
        }
        throw NetworkError.failedToFetchFriendRequestList
      })
      .asObservable()
      .asResult()
  }
  
  func fetchRequestSendedList() -> Observable<Result<[User], Error>> {
    return provider.rx.request(.requestSendedList)
      .observe(on: scheduler)
      .map(UserListResponse.self)
      .map({ response in
        if response.result == "SUCCESS" { return response.toDomain(state: .requestSended)
        }
        throw NetworkError.failedToFetchRequestSendedList
      })
      .asObservable()
      .asResult()
  }
  
  func deleteFriend(friendID: String) -> Observable<Void> {
    return provider.rx.request(.deleteFriend(friendID))
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
  }
  
  func acceptRequest(
    target: String
  ) -> Observable<Result<String, Error>> {
    return provider.rx.request(.respondToFriendRequest(target, .post))
      .observe(on: scheduler)
      .map(FriendRequestResponse.self)
      .map({ response in
        if response.result == "SUCCESS" {
          return response.data.receiver
        }
        throw NetworkError.failedToRespondToRequest
      })
      .asObservable()
      .asResult()
  }
  
  func rejectRequest(
    target: String
  ) -> Observable<Result<String, Error>> {
    return provider.rx.request(.respondToFriendRequest(target, .delete))
      .observe(on: scheduler)
      .map(FriendRequestResponse.self)
      .map({ response in
        if response.result == "SUCCESS" {
          return response.data.receiver
        }
        throw NetworkError.failedToRespondToRequest
      })
      .asObservable()
      .asResult()
  }
}
