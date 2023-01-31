import Foundation

struct Message {
  enum ContentType {
    case text
    case image
    case video
  }
  
  let id: Int64
  let from: String
  let type: ContentType
  let content: String
  let senderID: String
  let createdAt: String
  let unread: Bool
  let roomID: Int64
}
