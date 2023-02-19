import Foundation

struct ChatRoomResponse: Decodable {
  let roomID: String
  let title: String?
  let type: String
  let members: [MemberData]
  let createdAt: String
  let managers: [String]?
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case title, type, members, managers
    case createdAt
  }
}

extension ChatRoomResponse {
  func toDomain() -> ChatRoom {
    return ChatRoom(
      roomID: roomID,
      title: title,
      type: type,
      members: members.map({ $0.toDomain() }),
      messages: [],
      managers: managers)
  }
}
