import Foundation

struct Token: Codable {
  let accessToken: String
  let refreshToken: String
  let accessExpire: String
  let refreshExpire: String
}
