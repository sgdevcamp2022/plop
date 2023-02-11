import Foundation
import CoreData

extension CDFriend {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDFriend> {
        return NSFetchRequest<CDFriend>(entityName: "CDFriend")
    }

    @NSManaged public var block: Bool
    @NSManaged public var imageURL: String?
    @NSManaged public var uid: String?
    @NSManaged public var email: String?
    @NSManaged public var nickname: String?

}

extension CDFriend : Identifiable {}
