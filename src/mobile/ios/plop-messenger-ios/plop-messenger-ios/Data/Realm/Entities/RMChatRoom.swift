import RealmSwift

final class RMChatRoom: Object {
  @Persisted(primaryKey: true) var roomID: String
  @Persisted var title: String?
  @Persisted var type: String
  @Persisted var lastMessage: String
  
  @Persisted var messages: List<RMMessage>
  @Persisted var members: List<RMMember>
  
  convenience init(roomID: String, title: String?, type: String, lastMessage: String) {
    self.init()
    
    self.roomID = roomID
    self.title = title
    self.type = type
    self.lastMessage = lastMessage
  }
}

extension RMChatRoom: DomainConvertibleType {
  typealias DomainType = ChatRoom
  
  func asDomain() -> ChatRoom {
    return ChatRoom(
      roomID: roomID,
      title: title,
      type: type,
      members: members.toArray().map({ $0.asDomain() }),
      messages: messages.toArray().map({ $0.asDomain() }),
      managers: nil)
  }
}

extension ChatRoom: RealmRepresentable {
  typealias RealmType = RMChatRoom
  
  var uid: String {
    return roomID
  }
  
  func asRealm() -> RMChatRoom {
    return RMChatRoom.build({ object in
      let rmMembers = members.map({ $0.asRealm() })
    
      object.roomID = roomID
      object.title = title
      object.type = type
      if messages.count != 0 {
        let rmMessages = messages.map({ $0.asRealm() })
        object.messages.append(objectsIn: rmMessages)
      }
      object.members.append(objectsIn: rmMembers)
    })
  }
}
