import Foundation
import CoreData

extension CDMember {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDMember> {
        return NSFetchRequest<CDMember>(entityName: "CDMember")
    }

    @NSManaged public var imageURL: String?
    @NSManaged public var uid: String?
    @NSManaged public var email: String?
    @NSManaged public var nickname: String?
    @NSManaged public var room: CDRoom?

}

extension CDMember : Identifiable {}
