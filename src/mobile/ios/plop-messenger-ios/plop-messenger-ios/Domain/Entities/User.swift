import Foundation

struct User {
  let userID: String
  let email: String
  let profile: Profile
}

struct Profile {
  let nickname: String
  let imageURL: String
}
