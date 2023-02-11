import Foundation
import CoreData

extension CDUser {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDUser> {
        return NSFetchRequest<CDUser>(entityName: "CDUser")
    }

    @NSManaged public var uid: String?
    @NSManaged public var email: String?
    @NSManaged public var nickname: String?
    @NSManaged public var imageURL: String?

}

extension CDUser : Identifiable {}
