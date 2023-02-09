import Foundation
import CoreData

extension CDFriend {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDFriend> {
        return NSFetchRequest<CDFriend>(entityName: "CDFriend")
    }

    @NSManaged public var block: Bool
    @NSManaged public var image: String?
    @NSManaged public var name: String?
    @NSManaged public var uid: String?

}

extension CDFriend : Identifiable {

}
