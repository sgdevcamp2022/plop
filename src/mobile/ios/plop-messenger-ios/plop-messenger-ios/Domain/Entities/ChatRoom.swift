import Foundation
import Differentiator

struct ChatRoom {
  let roomID: String
  let title: String?
  let type: String
  let members: [Member]
  let messages: [Message]
  let managers: [String]?
}

extension ChatRoom: IdentifiableType {
  typealias Identity = String
  
  var identity: String {
    return roomID
  }
}

extension ChatRoom: Comparable {
  static func ==(lhs: ChatRoom, rhs: ChatRoom) -> Bool {
    return lhs.roomID == rhs.roomID
  }
  
  static func <(lhs: ChatRoom, rhs: ChatRoom) -> Bool {
    if lhs.messages.count == 0 && rhs.messages.count == 0 {
      return (lhs.title ?? "") < (rhs.title ?? "")
    } else if lhs.messages.count != 0 && rhs.messages.count == 0 {
      return true
    } else if lhs.messages.count == 0 && rhs.messages.count != 0 {
      return false
    } else {
      return lhs.messages.last!.createdAt < rhs.messages.last!.createdAt
    }
  }
}
