import Foundation
import CoreData

protocol Persistable {
  associatedtype T: NSManagedObject
  
  static var entityName: String { get }
  static var primaryAttributeName: String { get }
  
  var identity: String { get }
  
  init(entity: T)
  
  func update(_ entity: T) throws
  func predicate() -> NSPredicate
}

extension Persistable {
  func predicate() -> NSPredicate {
    NSPredicate(format: "%K == %@", Self.primaryAttributeName, self.identity)
  }
}
