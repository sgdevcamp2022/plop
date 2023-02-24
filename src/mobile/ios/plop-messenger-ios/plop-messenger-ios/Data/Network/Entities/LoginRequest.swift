import Foundation

struct LoginRequest: Encodable {
  let idOrEmail: String
  let password: String
}
