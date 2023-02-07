import Foundation

struct Token: Codable {
  let accessToken: String
  let refreshToken: String
  let accessExpire: Int
  let refreshExpire: Int
}
