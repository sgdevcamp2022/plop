import Foundation

struct SignupRequest: Encodable {
  let userid: String
  let email: String
  let password: String
  let nickname: String
}

struct SignupResponse: Decodable {
  let id: Int64
  let message: String
}
