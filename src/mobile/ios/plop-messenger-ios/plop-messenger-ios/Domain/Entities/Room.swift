import Foundation

struct Room {
  let id: Int64
  let title: String
  let unreadMessagesCount: Int
  let lastMessage: String
  let lastModified: String
  let members: [Member]
  let messages: [Message]
  let user: User
}
