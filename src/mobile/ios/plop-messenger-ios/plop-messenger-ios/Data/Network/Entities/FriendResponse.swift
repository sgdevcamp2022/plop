import Foundation

struct FriendResponse: Decodable {
  let userid: String
  let email: String
  let profile: Profile
}
