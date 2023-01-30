import Foundation
import Combine

struct User {
  enum State: Int {
    case nocertified = 0
    case certified = 1
    case exit = 9
  }
  
  enum Role {
    case admin
    case user
  }
  
  let id: Int
  let name: String
  let email: String
  let profile: Profile
  let state: State
  let role: Role
  let device: Device
  let createdAt: String
  let updatedAt: String
  let accessAt: String
  let loginAt: String
}

struct Profile {
  let nickname: String
  let image: AnyPublisher<Data, Never>
}

struct Device {
  let ios: String?
  let aos: String?
  let pc: String?
}
