import Foundation

struct ProfileResponse: Decodable {
  let email: String
  let userid: String
  let profile: Profile
  let message: String
}
