import Foundation
import CoreData

extension Friend: Persistable {
  typealias T = CDFriend
  
  static var entityName: String {
    return "CDFriend"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String {
    return uid
  }
  
  init(entity: CDFriend) {
    uid = entity.uid ?? ""
    block = entity.block
    imageURL = entity.imageURL ?? ""
    nickname = entity.nickname ?? "No Nickname"
    email = entity.email ?? ""
  }
  
  func update(_ entity: CDFriend) throws {
    entity.uid = uid
    entity.email = email
    entity.block = block
    
    entity.imageURL = imageURL
    entity.nickname = nickname
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      throw error
    }
  }
}
