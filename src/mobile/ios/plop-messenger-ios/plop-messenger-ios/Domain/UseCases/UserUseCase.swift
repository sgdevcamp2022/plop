import Foundation
import RxSwift

final class UserUseCase {
  private let profileCoreDataUseCase = CDProfileUseCase()
  private let usersNetwork = UsersNetwork()
  private let tokenUseCase = TokenUseCase()
  
  func signup(
    userid: String,
    email: String,
    password: String,
    nickname: String
  ) -> Observable<Result<Void, Error>> {
    let request = SignupRequest(
      userid: userid,
      email: email,
      password: password,
      nickname: nickname
    )
    
    return usersNetwork.signup(with: request)
      .map({ response in
        if response.message == "success" {
          return .success(())
        } else {
          return .failure(UseCaseError.invalidResponse)
        }
      })
  }
  
  func mockSignup(success: Bool) -> Observable<Result<Void, Error>> {
    if success {
      return Observable.just(.success(()))
    } else {
      return Observable.just(
        .failure(UseCaseError.invalidResponse))
    }
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
  
  func fetchProfile(
    email: String?,
    nickname: String?
  ) -> Observable<Result<Profile, Error>> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.just(.failure(UseCaseError.invalidResponse))
    }
    
    return usersNetwork.fetchProfile(
      token: token, email: email, nickname: nickname
    )
    .map({ response in
      if response.message == "success" {
        let profile = response.profile
        return .success(profile)
      } else {
        return .failure(UseCaseError.invalidResponse)
      }
    })
  }
  
  func mockFetchProfile(email: String) -> Observable<Result<Profile, Error>> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.just(.failure(UseCaseError.invalidResponse))
    }
    
    let mockProfile = Profile(
      uid: email, nickname: "Joons", image: ""
    )
    
    return Observable.just(.success(mockProfile))
  }
  
  func updateProfile(_ profile: Profile) -> Observable<Void> {
    return usersNetwork.updateProfile(token: "", to: profile)
      .map({ [unowned self] _ in
        self.profileCoreDataUseCase.save(profile: profile)
      })
  }
}
