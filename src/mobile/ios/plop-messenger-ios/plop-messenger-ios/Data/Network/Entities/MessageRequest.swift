import Foundation

struct MessageRequest: Codable {
  let roomID: String
  let senderID: String
  let messageType: String
  let content: String
  let createdAt: String
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case senderID = "sender_id"
    case messageType = "message_type"
    case content
    case createdAt = "created_at"
  }
}

extension MessageRequest {
  func toDomain(_ id: String) -> Message {
    return Message(
      messageID: id,
      contentType: messageType,
      content: content,
      senderID: senderID,
      createdAt: createdAt,
      unread: false,
      roomID: roomID)
  }
}
