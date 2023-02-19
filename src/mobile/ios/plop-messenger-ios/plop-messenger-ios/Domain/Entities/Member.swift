import Foundation
import MessageKit

struct Member {
  let userID: String
  let lastReadMessageID: String?
  let enteredAt: String
}

extension Member: SenderType {
  var senderId: String {
    return userID
  }
  
  var displayName: String {
    return userID
  }
}

