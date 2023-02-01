import Foundation

struct LoginResponse: Decodable {
  let accessToken: String
  let refreshToken: String?
  let accessTokenExpiresIn: Int
  let refreshTokenExpiresIn: Int?
  let message: String
  
  enum CodingKeys: String, CodingKey {
    case accessToken = "access_token"
    case refreshToken = "refresh_token"
    case accessTokenExpiresIn = "access_token_expires_in"
    case refreshTokenExpiresIn = "refresh_token_expires_in"
    case message
  }
}
