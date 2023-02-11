import Foundation

struct UserResponse: Decodable {
  let result: String
  let message: String
  let data: UserData
}
