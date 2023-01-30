//
//  CDUser+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/31.
//
//

import Foundation
import CoreData


extension CDUser {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDUser> {
        return NSFetchRequest<CDUser>(entityName: "CDUser")
    }

    @NSManaged public var email: String?
    @NSManaged public var id: Int64
    @NSManaged public var name: String?
    @NSManaged public var state: Int16
    @NSManaged public var createdAt: String?
    @NSManaged public var loginAt: String?
    @NSManaged public var accessAt: String?
    @NSManaged public var friends: NSSet?
    @NSManaged public var rooms: NSSet?
    @NSManaged public var profile: CDProfile?

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
