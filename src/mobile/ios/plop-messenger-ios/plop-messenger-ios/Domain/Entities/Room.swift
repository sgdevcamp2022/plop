import Foundation

struct Room: Codable {
  let uid: String
  let title: String
  let lastMessage: String
  let members: [Member]
  let messages: [Message]
}
