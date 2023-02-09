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
    //TODO: - member and messages
    members = []
    messages = []
  }
  
  func update(_ entity: CDRoom) {
    entity.uid = uid
    entity.title = title
    entity.lastMessage = lastMessage
    entity.members = NSSet(array: [])
    entity.messages = NSSet(array: [])
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      print(error)
    }
  }
}
