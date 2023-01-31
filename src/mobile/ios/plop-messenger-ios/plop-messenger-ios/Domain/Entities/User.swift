import Foundation

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
  
  let id: Int64
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
  let rooms: [Room]
  let friends: [Friend]
}

struct Profile {
  let id: Int64
  let nickname: String
  let image: String
}

struct Device {
  let ios: String?
  let aos: String?
  let pc: String?
}
