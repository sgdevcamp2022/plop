import Foundation

struct WithdrawalResponse: Decodable {
  let result: String
  let message: String
  let data: WithdrawalData
}

struct WithdrawalData: Decodable {
  let email: String
  let nickname: String
  let userID: String
  
  enum CodingKeys: String, CodingKey {
    case email, nickname
    case userID = "userId"
  }
}
