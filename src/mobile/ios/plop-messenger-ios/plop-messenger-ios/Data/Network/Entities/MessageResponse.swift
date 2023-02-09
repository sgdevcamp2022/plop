import Foundation

struct MessageResponse: Decodable {
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
    case messageID = "message_id"
    case roomID = "room_id"
    case messageType = "message_type"
    case senderID = "sender_id"
    case content
    case createdAt = "created_at"
  }
}

extension MessageData {
  func toDomain() -> Message {
    return Message(
      uid: messageID,
      type: messageType,
      content: content,
      senderID: senderID,
      createdAt: createdAt,
      unread: true,
      roomID: roomID
    )
  }
}
