import Foundation

struct FriendRequestResponse: Decodable {
  let result: String
  let message: String
  let data: FriendRequestData
}

struct FriendRequestData: Decodable {
  let sender: String
  let receiver: String
}
