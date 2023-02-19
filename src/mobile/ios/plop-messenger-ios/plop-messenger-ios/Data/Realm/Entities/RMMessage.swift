import RealmSwift

final class RMMessage: Object {
  @Persisted(primaryKey: true) var messageID: String
  @Persisted var roomID: String
  @Persisted var senderID: String
  
  @Persisted var contentType: String
  @Persisted var content: String
  
  @Persisted var unread: Bool
  @Persisted var createdAt: String
  
  @Persisted(originProperty: "messages") var chatRoom: LinkingObjects<RMChatRoom>
  
  convenience init(messageID: String, roomID: String, senderID: String, contentType: String, content: String, unread: Bool, createdAt: String) {
    self.init()
    
    self.messageID = messageID
    self.roomID = roomID
    self.senderID = senderID
    self.contentType = contentType
    self.content = content
    self.unread = unread
    self.createdAt = createdAt
  }
}

extension RMMessage: DomainConvertibleType {
  typealias DomainType = Message
  
  func asDomain() -> Message {
    return Message(
      messageID: messageID,
      contentType: contentType,
      content: content,
      senderID: senderID,
      createdAt: createdAt,
      unread: unread,
      roomID: roomID)
  }
}

extension Message: RealmRepresentable {
  typealias RealmType = RMMessage
  
  var uid: String {
    return messageID
  }
  
  func asRealm() -> RMMessage {
    return RMMessage.build({ object in
      object.messageID = messageID
      object.roomID = roomID
      object.senderID = senderID
      object.contentType = contentType
      object.content = content
      object.unread = unread
      object.createdAt = createdAt
    })
  }
}
