import Foundation

struct LogoutResponse: Decodable {
  let result: String
  let message: String
  let data: LogoutData
}

struct LogoutData: Decodable {
  let email: String
  let userID: String
  
  enum CodingKeys: String, CodingKey {
    case email
    case userID = "userId"
  }
}
