import Foundation
import CoreData
import RxSwift

extension CDRoom: DomainConvertibleType {
  func toDomain() -> Room {
    return Room(
      uid: uid,
      title: title ?? "",
      unreadMessagesCount: Int(unread),
      lastMessage: lastMessage ?? "",
      lastModified: lastModified ?? "\(Date())",
      members: (members.allObjects as! [CDMember]).mapToDomain(),
      messages: (messages.allObjects as? [CDMessage] ?? []).mapToDomain(),
      user: user.toDomain()
    )
  }
}

extension CDRoom: Persistable {
  static var entityName: String {
    return "CDRoom"
  }
  
  static func synced(
    room: CDRoom,
    user: CDUser,
    members: [CDMember],
    messages: [CDMessage]
  ) -> CDRoom {
    room.user = user
    room.members = NSSet(array: members)
    room.messages = NSSet(array: messages)
    return room
  }
}

extension Room: CoreDataRepresentable {
  typealias CoreDataType = CDRoom
  
  func sync(
    in context: NSManagedObjectContext
  ) -> Observable<CDRoom> {
    let syncSelf = context.rx.sync(
      entity: self,
      update: update)
    
    let syncUser = user.sync(in: context)

    let membersObservables = members
      .map({
        context.rx.sync(entity: $0, update: $0.update)
      })
    
    let messagesObservables = messages
      .map({
        context.rx.sync(entity: $0, update: $0.update)
      })
    
    let syncMembers = Observable.combineLatest(membersObservables)
    let syncMessages = Observable.combineLatest(messagesObservables)
    
    return Observable.zip(
      syncSelf,
      syncUser,
      syncMembers,
      syncMessages,
      resultSelector: CDRoom.synced)
  }
  
  func update(entity: CDRoom) {
    entity.uid = uid
    entity.title = title
    entity.unread = Int16(unreadMessagesCount)
    entity.lastMessage = lastMessage
    entity.lastModified = lastModified
  }
}
