import Foundation

struct MessageRequest: Codable {
  let roomID: String
  let senderID: String
  let messageType: String
  let content: String
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case senderID = "sender_id"
    case messageType = "message_type"
    case content
  }
}
