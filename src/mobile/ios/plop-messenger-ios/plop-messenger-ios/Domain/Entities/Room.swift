import Foundation

struct Room: Codable {
  let uid: Int64
  let title: String
  let unreadMessagesCount: Int
  let lastMessage: String
  let lastModified: String
  let members: [Member]
  let messages: [Message]
}
