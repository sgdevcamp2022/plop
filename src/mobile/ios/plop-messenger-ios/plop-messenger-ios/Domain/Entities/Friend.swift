import Foundation

struct Friend {
  enum Status {
    case online
    case offline
  }
  
  let id: Int64
  let senderID: Int64?
  let status: Status
  let block: Bool
  let imageURL: String?
  let name: String
}
