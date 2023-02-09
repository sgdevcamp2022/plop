import Foundation

struct User: Codable {
  let uid: String
  let email: String
  let profile: Profile
  let rooms: [Room]
  let friends: [Friend]
}

struct Profile: Codable {
  let uid: String
  let nickname: String
  let image: String
}


extension Profile {
  static func empty() -> Profile {
    return Profile(uid: "", nickname: "", image: "")
  }
}
