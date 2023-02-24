import Foundation

struct MessageResponse: Decodable {
  let message: String
  let data: [MessageData]
}

struct MessageData: Decodable {
  let messageID: String
  let roomID: String
  let messageType: String
  let senderID: String
  let content: String
  let createdAt: String
  
  enum CodingKeys: String, CodingKey {
    case messageType = "message_type"
    case roomID = "room_id"
    case senderID = "sender_id"
    case content
    case messageID = "message_id"
    case createdAt = "created_at"
  }
}

extension MessageData {
  func toDomain() -> Message {
    return Message(
      messageID: messageID,
      contentType: messageType,
      content: content,
      senderID: senderID,
      createdAt: createdAt,
      unread: true,
      roomID: roomID
    )
  }
}
