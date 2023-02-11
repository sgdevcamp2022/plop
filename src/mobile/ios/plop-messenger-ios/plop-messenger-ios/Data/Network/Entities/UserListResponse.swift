import Foundation

struct UserListResponse: Decodable {
  let result: String
  let message: String
  let data: [UserData]
}

extension UserListResponse {
  func toDomain() -> [User] {
    return data.map { $0.toDomain() }
  }
}
