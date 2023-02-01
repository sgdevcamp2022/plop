import Foundation

struct RoomsListResponse: Decodable {
  let message: String
  let data: [RoomData]
}

struct RoomData: Decodable {
  let roomID: String
  let title: String
  let lastMessage: LastMessageResponse
}

struct LastMessageResponse: Decodable {
  let messageID: String
  let senderID: String
  let content: String
  let lastModifiedAt: String
}
