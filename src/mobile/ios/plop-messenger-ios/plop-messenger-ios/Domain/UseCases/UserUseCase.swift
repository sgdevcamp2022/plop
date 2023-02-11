import Foundation
import RxSwift

final class UserUseCase {
  //TODO: - Core Data 연동
  private let network = UserNetwork()
  private let tokenUseCase = TokenUseCase()
  private let coreData = UserCoreData()
  
  func fetchUserInfo(
    target: String
  ) -> Observable<Result<User, Error>> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.just(.failure(TokenError.failedToFetchAccessToken))
    }
    
    return network.fetchUserInfo(accessToken: accessToken, target: target)
  }
  
  func fetchCurrentUser() -> Observable<User> {
    guard let currentEmail = UserDefaults.standard.string(forKey: "currentEmail") else {
      return Observable.error(UseCaseError.failedToFetchEmail)
    }
    
    return fetchUserInfo(target: currentEmail)
      .map({ [unowned self] result in
        switch result {
        case .success(let user):
          self.coreData.save(user)
        case .failure(let error):
          throw error
        }
      })
      .flatMap({ [unowned self] _ in
        return self.coreData.fetch(currentEmail)
      })
  }
  
  func updateUserInfo(
    target: String,
    updatedProfile: Profile
  ) -> Observable<Result<User, Error>> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.just(.failure(TokenError.failedToFetchAccessToken))
    }
    return network.updateUserInfo(
      accessToken: accessToken,
      target: target,
      updatedProfile: updatedProfile
    )
  }
  
  func search(
    target: String
  ) -> Observable<[User]> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    return network.searchUser(accessToken: accessToken, target: target)
      .map({ result in
        switch result {
        case .success(let users):
          return users
        case .failure(let error):
          throw error
        }
      })
  }
  
  //TODO: - Core data logic 추가
  func fetchFriendList() -> Observable<[User]> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    return network.fetchFriendList(accessToken: accessToken)
      .map({ result in
        switch result {
        case .success(let users):
          return users
        case .failure(let error):
          throw error
        }
      })
  }
  
  func requestFriend(to target: String) -> Observable<Void> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    return network.requestFriend(accessToken: accessToken,
                                 target: target)
  }
  
  func cancelRequestFriend(to target: String) -> Observable<Void> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    return network.cancelRequestFriend(accessToken: accessToken,
                                       target: target)
  }
  
  func fetchFriendRequestList() -> Observable<[User]> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    return network.friendRequestList(accessToken: accessToken)
      .map({ result in
        switch result {
        case .success(let users):
          return users
        case .failure(let error):
          throw error
        }
      })
  }
  
  func respondToFriendRequest(
    to target: String,
    method: String
  ) -> Observable<Void> {
    guard let accessToken = tokenUseCase.fetchAccessToken() else {
      return Observable.error(TokenError.failedToFetchAccessToken)
    }
    return network.respondToFriendRequest(
      accessToken: accessToken,
      target: target,
      method: method)
  }
}
