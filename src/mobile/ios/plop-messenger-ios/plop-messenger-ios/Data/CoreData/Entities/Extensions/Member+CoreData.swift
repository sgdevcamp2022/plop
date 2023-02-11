import Foundation
import CoreData

extension Member: Persistable {
  typealias T = CDMember
  
  static var entityName: String {
    return "CDMember"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String {
    return uid
  }
  
  init(entity: CDMember) {
    uid = entity.uid ?? ""
    email = entity.email ?? ""
    nickname = entity.nickname ?? ""
    imageURL = entity.imageURL
  }
  
  func update(_ entity: CDMember) throws {
    entity.uid = uid
    entity.email = email
    entity.nickname = nickname
    entity.imageURL = imageURL
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      throw error
    }
  }
}
