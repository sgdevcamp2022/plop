import Foundation

struct Room {
  let uid: String
  let title: String
  let lastMessage: String
  let members: [Member]
  let messages: [Message]
}
