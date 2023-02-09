import Foundation
import CoreData

extension CDProfile {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDProfile> {
        return NSFetchRequest<CDProfile>(entityName: "CDProfile")
    }

    @NSManaged public var imageURL: String?
    @NSManaged public var nickname: String?
    @NSManaged public var uid: String?

}

extension CDProfile : Identifiable {

}
