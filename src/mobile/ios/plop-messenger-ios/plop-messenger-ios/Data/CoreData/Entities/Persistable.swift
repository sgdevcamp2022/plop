import Foundation
import CoreData

protocol Persistable: NSFetchRequestResult, DomainConvertibleType {
  static var entityName: String {get}
  static func fetchRequest() -> NSFetchRequest<Self>
  static var primaryAttributeName: String { get }
}
