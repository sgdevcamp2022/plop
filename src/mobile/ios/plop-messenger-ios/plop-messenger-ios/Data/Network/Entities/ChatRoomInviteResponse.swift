import Foundation

struct ChatRoomInviteResponse: Decodable {
  let message: String
  let data: ChatRoomInviteData
}

struct ChatRoomInviteData: Decodable {
  let roomID: String
  let members: [String]
  
  enum CodingKeys: String, CodingKey {
    case roomID = "room_id"
    case members
  }
}
