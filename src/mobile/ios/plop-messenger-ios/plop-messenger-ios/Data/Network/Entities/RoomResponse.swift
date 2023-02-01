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
