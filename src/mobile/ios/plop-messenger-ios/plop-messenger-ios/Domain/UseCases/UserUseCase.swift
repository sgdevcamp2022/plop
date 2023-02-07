import Foundation
import RxSwift

final class UserUseCase {
  private let userCoreDataUseCase = CDUsersUseCase()
  private let profileCoreDataUseCase = CDProfileUseCase()
  private let usersNetwork = UsersNetwork()
  
  func signup(
    userid: String,
    email: String,
    password: String,
    nickname: String
  ) -> Observable<Result<User, Error>> {
    let request = SignupRequest(
      userid: userid,
      email: email,
      password: password,
      nickname: nickname
    )
    
    return usersNetwork.signup(with: request)
      .map({ response in
        if response.message == "success" {
          let user = User(
            uid: response.id,
            userid: userid,
            email: email,
            profile: Profile(uid: response.id,
                             nickname: nickname,
                             image: ""),
            device: "",
            rooms: [],
            friends: []
          )
          return .success(user)
        } else {
          return .failure(UseCaseError.invalidResponse)
        }
      })
  }
  
  func mockSignup() -> Observable<Result<User, Error>> {
    let user = User(
      uid: 0,
      userid: "userid",
      email: "email",
      profile: Profile(uid: 0, nickname: "nickname",
                       image: "image"),
      device: "device",
      rooms: [], friends: [])
    return Observable.just(.success(user))
  }
  
  func search(with email: String) -> Observable<Profile> {
    return usersNetwork.searchUser(token: "", email: email, nickname: nil)
      .map({ response in
        if response.message == "success" {
          return response.profile
        } else {
          throw UseCaseError.invalidResponse
        }
      })
  }
  
  func fetchProfile(email: String?, nickname: String?) -> Observable<Profile> {
    return usersNetwork.fetchProfile(
      token: "", email: email, nickname: nickname
    )
    .map({ response in
      if response.message == "success" {
        let profile = response.profile
        return profile
      } else {
        throw UseCaseError.invalidResponse
      }
    })
  }
  
  func updateProfile(_ profile: Profile) -> Observable<Void> {
    return Observable.combineLatest(
      usersNetwork.updateProfile(token: "", to: profile),
      profileCoreDataUseCase.save(profile: profile)
    ).mapToVoid()
  }
  
  func delete(_ user: User) -> Observable<Void> {
    return Observable.combineLatest(
      usersNetwork.delete(token: ""),
      userCoreDataUseCase.delete(user: user)
    )
    .mapToVoid()
  }
  
  func save(user: User) -> Observable<Void> {
    return userCoreDataUseCase.save(user: user)
  }
}
