import Foundation

struct RoomResponse: Decodable {
  let roomID: String
  let title: String
  let members: [MemberResponse]
  let manager: String
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case title, members, manager
  }
}

extension RoomResponse {
  func toDomain() -> Room {
    return Room(
      uid: roomID,
      title: title,
      lastMessage: "",
      members: members.map({ $0.toDomain() }),
      messages: [])
  }
}
