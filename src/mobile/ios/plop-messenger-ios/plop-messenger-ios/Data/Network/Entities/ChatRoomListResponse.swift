import Foundation

struct ChatRoomListResponse: Decodable {
  let message: String
  let data: [ChatRoomData]?
}

// For `ChatRoomListResponse`
struct ChatRoomData: Decodable {
  let roomID: String
  let title: String?
  let members: [MemberData]
  let lastMessage: LastMessageData
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case title, members
    case lastMessage = "last_message"
  }
}

extension ChatRoomData {
  func toDomain() -> ChatRoom {
    return ChatRoom(
      roomID: roomID,
      title: title,
      type: "",
      members: members.map({$0.toDomain()}),
      messages: [],
      managers: nil
    )
  }
}

struct LastMessageData: Decodable {
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
