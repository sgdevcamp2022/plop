import Foundation
import Differentiator

enum UserState: String {
  case current
  case notFriend
  case friend
  case requestSended
  case requestReceived
  case none
}

struct User {
  let userID: String
  let email: String
  var state: UserState
  let profile: Profile
}

struct Profile {
  let nickname: String
  let imageURL: String?
}

extension User: IdentifiableType, Equatable {
  typealias Identity = String
  
  var identity: String {
    return userID
  }
  
  static func == (lhs: User, rhs: User) -> Bool {
    return lhs.userID == rhs.userID
  }
}

extension User {
  func toMember() -> Member {
    return Member(
      userID: userID,
      lastReadMessageID: nil,
      enteredAt: ""
    )
  }
}
