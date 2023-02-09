import Foundation
import CoreData

extension Friend: Persistable {
  typealias T = NSManagedObject
  
  static var entityName: String {
    return "CDFriend"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String {
    return uid
  }
  
  init(entity: NSManagedObject) {
    uid = entity.value(forKeyPath: #keyPath(CDFriend.uid)) as! String
    block = entity.value(forKeyPath: #keyPath(CDFriend.block)) as! Bool
    imageURL = entity.value(forKeyPath: #keyPath(CDFriend.image)) as? String
    name = entity.value(forKeyPath: #keyPath(CDFriend.name)) as! String
  }
  
  func update(_ entity: NSManagedObject) {
    entity.setValue(uid, forKeyPath: #keyPath(CDFriend.uid))
    entity.setValue(block, forKeyPath: #keyPath(CDFriend.block))
    entity.setValue(imageURL, forKeyPath: #keyPath(CDFriend.image))
    entity.setValue(name, forKeyPath: #keyPath(CDFriend.name))
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      print(error)
    }
  }
}
