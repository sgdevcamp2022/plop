import Foundation

struct RoomsListResponse: Decodable {
  let message: String
  let data: [RoomData]
}

struct RoomData: Decodable {
  let roomID: String
  let title: String?
  let lastMessage: LastMessageResponse
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case title
    case lastMessage = "last_message"
  }
}

struct LastMessageResponse: Decodable {
  let messageID: String?
  let senderID: String?
  let content: String?
  let createdAt: String?
  
  enum CodingKeys: String, CodingKey {
    case messageID = "message_id"
    case senderID = "sender_id"
    case content
    case createdAt = "created_at"
  }
}
