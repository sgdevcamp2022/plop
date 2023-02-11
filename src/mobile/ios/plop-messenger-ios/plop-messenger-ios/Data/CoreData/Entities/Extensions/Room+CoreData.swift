import CoreData
import Foundation

extension Room: Persistable {
  typealias T = CDRoom
  
  static var entityName: String {
    return "CDRoom"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String {
    return uid
  }
  
  init(entity: CDRoom) {
    uid = entity.uid ?? ""
    title = entity.title ?? ""
    lastMessage = entity.lastMessage ?? ""
    members = (entity.members?.allObjects as? [CDMember])
      .map({ $0.map({ Member.init(entity: $0) }) }) ?? []
    messages = (entity.messages?.allObjects as? [CDMessage])
      .map({ $0.map({Message.init(entity: $0)}) }) ?? []
  }
  
  func update(_ entity: CDRoom) throws {
    entity.uid = uid
    entity.title = title
    entity.lastMessage = lastMessage
    //TODO: - domain to core data entity
    entity.members = NSSet(array: [])
    entity.messages = NSSet(array: [])
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      throw error
    }
  }
}
