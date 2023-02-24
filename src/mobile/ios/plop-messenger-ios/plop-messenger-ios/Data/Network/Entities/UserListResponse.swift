import Foundation

struct UserListResponse: Decodable {
  let result: String
  let message: String
  let data: [UserData]
}

extension UserListResponse {
  func toDomain(state: UserState) -> [User] {
    return data.map { $0.toDomain(state: state) }
  }
}
