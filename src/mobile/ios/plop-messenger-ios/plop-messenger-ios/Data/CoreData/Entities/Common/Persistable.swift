import Foundation
import CoreData

protocol Persistable: NSFetchRequestResult, DomainConvertibleType {
  static var entityName: String { get }
  
  static func fetchRequest() -> NSFetchRequest<Self>
}

extension Persistable {
  static var primaryAttributeName: String {
    return "id"
  }
}
