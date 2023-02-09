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
    name = entity.name ?? ""
    image = entity.image ?? ""
  }
  
  func update(_ entity: CDMember) {
    entity.uid = uid
    entity.name = name
    entity.image = image
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      print(error)
    }
  }
}
