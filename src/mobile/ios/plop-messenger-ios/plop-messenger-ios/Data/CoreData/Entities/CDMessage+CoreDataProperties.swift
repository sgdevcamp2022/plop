import Foundation
import CoreData

extension CDMessage {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDMessage> {
        return NSFetchRequest<CDMessage>(entityName: "CDMessage")
    }

    @NSManaged public var content: String?
    @NSManaged public var contentType: String?
    @NSManaged public var createdAt: Date?
    @NSManaged public var roomID: String?
    @NSManaged public var senderID: String?
    @NSManaged public var uid: String?
    @NSManaged public var unread: Bool
    @NSManaged public var room: CDRoom?

}

extension CDMessage : Identifiable {}
