import Foundation

struct Token: Codable {
  let uid: String
  let accessToken: String
  let refreshToken: String
  let accessTokenExpiresIn: Int
  let refreshTokenExpiresIn: Int
}
