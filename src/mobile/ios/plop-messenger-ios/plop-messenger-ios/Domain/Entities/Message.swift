import Foundation

struct Message: Codable {
  let uid: Int64
  let from: String
  let type: String
  let content: String
  let senderID: String
  let createdAt: String
  let unread: Bool
  let roomID: Int64
}
