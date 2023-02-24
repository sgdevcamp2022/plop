import Foundation
import RxSwift

final class UserUseCase {
  private let network = UserNetwork()
  private let tokenUseCase = TokenUseCase()
  private let userRealm = UserRealm()
  
  func fetchUserInfo(
    user: User
  ) -> Observable<Result<User, Error>> {
    return network.fetchUserInfo(user)
  }
  
  // Fetch Current User and save at realm
  func fetchCurrentUser() -> Observable<Void> {
    guard let currentEmail = UserDefaults.standard.string(forKey: "currentEmail") else {
      return Observable.error(UseCaseError.failedToFetchEmail)
    }
    
    return network.fetchCurrentUser(target: currentEmail)
      .flatMap({ result in
        switch result {
        case .success(let user):
          return self.userRealm.save(user)
        case .failure(let error):
          throw error
        }
      })
  }
  
  // Update user and save at realm
  func updateUserInfo(
    target: String,
    updatedProfile: Profile
  ) -> Observable<Void> {
    return network.updateUserInfo(
      target: target,
      updatedProfile: updatedProfile)
    .flatMap({ result in
      switch result {
      case .success(let user):
        return self.userRealm.save(user)
      case .failure(let error):
        throw error
      }
    })
  }
  
  func search(
    target: String
  ) -> Observable<[User]> {
    return network.searchUser(target: target)
      .map({ result in
        switch result {
        case .success(let users):
          return users
        case .failure(let error):
          throw error
        }
      })
  }
  
  //Fetch friend list and save at realm
  func fetchFriendList() -> Observable<Void> {
    return network.fetchFriendList()
      .flatMap({ result in
        switch result {
        case .success(let users):
          return self.userRealm.save(users: users)
        case .failure(let error):
          throw error
        }
      })
  }
  
  func requestFriend(to user: User) -> Observable<Void> {
    return network.requestFriend(target: user.email)
      .flatMap({
        return self.userRealm.save(user)
      })
  }
  
  func cancelRequestFriend(to user: User) -> Observable<Void> {
    return network.cancelRequestFriend(target: user.email)
      .flatMap({
        return self.userRealm.delete(user.userID)
      })
  }
  
  func fetchFriendRequestList() -> Observable<Void> {
    return network.friendRequestList()
      .flatMap({ result in
        switch result {
        case .success(let users):
          return self.userRealm.save(users: users)
        case .failure(let error):
          throw error
        }
      })
  }
  
  func fetchRequestSendedList() -> Observable<Void> {
    return network.fetchRequestSendedList()
      .flatMap({ result in
        switch result {
        case .success(let users):
          return self.userRealm.save(users: users)
        case .failure(let error):
          throw error
        }
      })
  }
  
  func acceptRequest(to user: User) -> Observable<Void> {
    return network.acceptRequest(target: user.email)
      .flatMap({ result in
        switch result {
        case .success(let receiver):
          if user.email == receiver {
            return self.userRealm.save(user)
          }
          throw NetworkError.failedToRespondToRequest
        case .failure(let error):
          throw error
        }
      })
  }
  
  func rejectRequest(to user: User) -> Observable<Void> {
    return network.rejectRequest(target: user.email)
      .flatMap({ result in
        switch result {
        case .success(let receiver):
          if user.email == receiver {
            return self.userRealm.delete(user.userID)
          }
          throw NetworkError.failedToRespondToRequest
        case .failure(let error):
          throw error
        }
      })
  }
}
