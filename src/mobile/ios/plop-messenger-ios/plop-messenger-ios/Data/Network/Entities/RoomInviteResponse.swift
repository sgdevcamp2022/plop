import Foundation

struct RoomInviteResponse: Decodable {
  let message: String
  let data: RoomInviteData
}

struct RoomInviteData: Decodable {
  let roomID: String
  let members: [String]
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case members
  }
}
