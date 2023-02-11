import Foundation

struct SignupRequest: Encodable {
  let userID: String
  let email: String
  let nickname: String
  let password: String
  
  enum CodingKeys: String, CodingKey {
    case userID = "userId"
    case email, nickname, password
  }
}

struct SignupResponse: Decodable {
  let result: String
  let message: String
  let data: SignupData
}

struct SignupData: Decodable {
  let email: String
  let nickname: String
  let userID: String
  
  enum CodingKeys: String, CodingKey {
    case email, nickname
    case userID = "userId"
  }
}

extension SignupData {
  func toDomain() -> User {
    return User(
      userID: userID,
      email: email,
      profile: Profile(
        nickname: nickname,
        imageURL: "")
    )
  }
}
