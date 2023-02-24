import Foundation

struct WebsocketMessage: Decodable {
  let messageType: String
  let roomID: String
  let senderID: String
  let content: String
  let messageID: String
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

extension WebsocketMessage {
  func toDomain() -> Message {
    return Message(
      messageID: messageID,
      contentType: messageType,
      content: content,
      senderID: senderID,
      createdAt: createdAt,
      unread: false,
      roomID: roomID
    )
  }
}
