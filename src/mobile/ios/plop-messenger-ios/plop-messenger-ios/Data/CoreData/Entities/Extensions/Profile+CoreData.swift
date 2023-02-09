import Foundation
import CoreData

extension Profile: Equatable {
  static func ==(lhs: Profile, rhs: Profile) -> Bool {
    return lhs.uid == rhs.uid
  }
}

extension Profile: Persistable {
  typealias T = NSManagedObject
  
  static var entityName: String {
    return "CDProfile"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String { return uid }
  
  init(entity: NSManagedObject) {
    uid = entity.value(forKey: "uid") as! String
    nickname = entity.value(forKey: "nickname") as! String
    image = entity.value(forKeyPath: #keyPath(CDProfile.imageURL)) as! String
  }
  
  func update(_ entity: NSManagedObject) {
    entity.setValue(uid, forKeyPath: #keyPath(CDProfile.uid))
    entity.setValue(nickname, forKeyPath: #keyPath(CDProfile.nickname))
    entity.setValue(image, forKeyPath: #keyPath(CDProfile.imageURL))
    
    do {
      try entity.managedObjectContext?.save()
    } catch let error {
      print(error)
    }
  }
}
