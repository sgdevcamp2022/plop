import Foundation

struct PresenceChangeResponse: Decodable {
  let userID: String
  let status: String
  
  enum CodingKeys: String, CodingKey {
    case userID = "user_id"
    case status
  }
}
