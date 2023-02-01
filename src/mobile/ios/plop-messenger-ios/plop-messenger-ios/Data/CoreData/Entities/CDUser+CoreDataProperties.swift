import Foundation
import CoreData

extension CDUser {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDUser> {
        return NSFetchRequest<CDUser>(entityName: "CDUser")
    }

    @NSManaged public var email: String?
    @NSManaged public var userid: String?
    @NSManaged public var uid: Int64
    @NSManaged public var device: String?
    @NSManaged public var friends: NSSet
    @NSManaged public var profile: CDProfile
    @NSManaged public var rooms: NSSet

}

// MARK: Generated accessors for friends
extension CDUser {

    @objc(addFriendsObject:)
    @NSManaged public func addToFriends(_ value: CDFriend)

    @objc(removeFriendsObject:)
    @NSManaged public func removeFromFriends(_ value: CDFriend)

    @objc(addFriends:)
    @NSManaged public func addToFriends(_ values: NSSet)

    @objc(removeFriends:)
    @NSManaged public func removeFromFriends(_ values: NSSet)

}

// MARK: Generated accessors for rooms
extension CDUser {

    @objc(addRoomsObject:)
    @NSManaged public func addToRooms(_ value: CDRoom)

    @objc(removeRoomsObject:)
    @NSManaged public func removeFromRooms(_ value: CDRoom)

    @objc(addRooms:)
    @NSManaged public func addToRooms(_ values: NSSet)

    @objc(removeRooms:)
    @NSManaged public func removeFromRooms(_ values: NSSet)

}

extension CDUser : Identifiable {

}
