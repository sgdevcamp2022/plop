import Foundation

struct FriendsListResponse: Decodable {
  let profiles: [FriendResponse]
  let message: String
}
