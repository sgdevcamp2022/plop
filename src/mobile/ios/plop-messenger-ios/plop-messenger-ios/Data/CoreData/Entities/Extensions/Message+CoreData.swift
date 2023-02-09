import CoreData
import Foundation

extension Message: Persistable {
  typealias T = CDMessage
  
  static var entityName: String {
    return "CDMessage"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String {
    return uid
  }
  
  init(entity: CDMessage) {
    uid = entity.uid ?? ""
    type = entity.contentType ?? ""
    content = entity.content ?? ""
    senderID = entity.senderID ?? ""
    createdAt = "\(entity.createdAt ?? Date())"
    unread = entity.unread
    roomID = entity.roomID ?? ""
  }
  
  func update(_ entity: CDMessage) {
    entity.uid = uid
    entity.contentType = type
    entity.content = content
    entity.senderID = senderID
    entity.createdAt = Date()
    entity.unread = unread
    entity.roomID = roomID
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      print(error)
    }
  }
  
}
