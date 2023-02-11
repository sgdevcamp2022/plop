import Foundation

struct UserData: Decodable {
  let userID: String
  let email: String
  let profile: ProfileData
  
  enum CodingKeys: String, CodingKey {
    case userID = "userId"
    case email, profile
  }
}

struct ProfileData: Codable {
  let imageURL: String
  let nickname: String
  
  enum CodingKeys: String, CodingKey {
    case imageURL = "img"
    case nickname
  }
}

extension UserData {
  func toDomain() -> User {
    return User(
      userID: userID,
      email: email,
      profile: profile.toDomain())
  }
}

extension ProfileData {
  func toDomain() -> Profile {
    return Profile(
      nickname: nickname,
      imageURL: imageURL
    )
  }
}

extension Profile {
  func toEncodable() -> ProfileData {
    return ProfileData(
      imageURL: imageURL,
      nickname: nickname)
  }
}
