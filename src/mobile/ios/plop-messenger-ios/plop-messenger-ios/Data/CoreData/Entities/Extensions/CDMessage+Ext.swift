import Foundation
import CoreData
import RxSwift

extension CDMessage: DomainConvertibleType {
  func toDomain() -> Message {
    //TODO : - content type 수정
    return Message(
      uid: uid,
      from: from ?? "",
      type: .text,
      content: content ?? "",
      senderID: from ?? "",
      createdAt: "\(createdAt ?? Date())",
      unread: unread,
      roomID: roomID
    )
  }
}

extension CDMessage: Persistable {
  static var entityName: String {
    return "CDMessage"
  }
}

extension Message: CoreDataRepresentable {
  typealias CoreDataType = CDMessage
  
  func update(entity: CDMessage) {
    entity.uid = uid
    entity.from = from
    entity.unread = unread
    entity.content = content
    entity.createdAt = Date()
    entity.roomID = roomID
  }
}
