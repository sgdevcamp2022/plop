import Foundation
import MessageKit
import RxDataSources

struct Message {
  let messageID: String
  let contentType: String
  let content: String
  let senderID: String
  let createdAt: String
  let unread: Bool
  let roomID: String
}

extension Message: IdentifiableType, Equatable {
  typealias Identity = String
  
  var identity: String {
    return messageID
  }
  
  static func ==(lhs: Message, rhs: Message) -> Bool {
    return lhs.messageID == rhs.messageID
  }
}

extension Message: MessageType {
  var sender: MessageKit.SenderType {
    return Member(
      userID: senderID, lastReadMessageID: nil, enteredAt: ""
    )
  }
  
  var messageId: String {
    return messageID
  }
  
  var sentDate: Date {
    return createdAt.toDate() ?? Date()
  }
  
  var kind: MessageKit.MessageKind {
    return .text(content)
  }
}

extension Message: Comparable {
  static func < (lhs: Message, rhs: Message) -> Bool {
    return lhs.createdAt < rhs.createdAt
  }
}
