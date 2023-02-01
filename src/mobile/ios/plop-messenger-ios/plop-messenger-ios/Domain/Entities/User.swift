import Foundation

struct User: Codable {
  let uid: Int64
  let userid: String
  let email: String
  let profile: Profile
  let device: String
  let rooms: [Room]
  let friends: [Friend]
}

struct Profile: Codable {
  let uid: Int64
  let nickname: String
  let image: String
}
