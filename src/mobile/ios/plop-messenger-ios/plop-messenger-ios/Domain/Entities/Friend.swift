import Foundation

struct Friend {
  enum Status {
    case online
    case offline
  }
  
  let userID: Int
  let friendID: Int
  let senderID: Int
  let status: Status
  let block: Bool
}
