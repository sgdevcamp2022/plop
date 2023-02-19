import Foundation

struct LoginResponse: Decodable {
  let result: String
  let message: String
  let data: LoginData
}

struct LoginData: Decodable {
  let accessToken: String
  let refreshToken: String?
  let accessExpire: String
  let refreshExpire: String?
}

extension LoginData {
  func toDomain() -> Token {
    return Token(
      accessToken: accessToken,
      refreshToken: refreshToken ?? "",
      accessExpire: accessExpire,
      refreshExpire: refreshExpire ?? ""
    )
  }
}
