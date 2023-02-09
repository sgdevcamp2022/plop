import Foundation
import CoreData

extension CDMember {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDMember> {
        return NSFetchRequest<CDMember>(entityName: "CDMember")
    }

    @NSManaged public var image: String?
    @NSManaged public var name: String?
    @NSManaged public var uid: String?
    @NSManaged public var room: CDRoom?

}

extension CDMember : Identifiable {

}
