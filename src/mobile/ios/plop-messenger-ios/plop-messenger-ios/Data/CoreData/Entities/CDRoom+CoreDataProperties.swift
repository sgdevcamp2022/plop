//
//  CDRoom+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/31.
//
//

import Foundation
import CoreData


extension CDRoom {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDRoom> {
        return NSFetchRequest<CDRoom>(entityName: "CDRoom")
    }

    @NSManaged public var id: Int64
    @NSManaged public var lastMessage: String?
    @NSManaged public var title: String?
    @NSManaged public var unread: Int16
    @NSManaged public var lastModified: String?
    @NSManaged public var members: NSSet?
    @NSManaged public var messages: NSSet?
    @NSManaged public var user: CDUser?

}

// MARK: Generated accessors for members
extension CDRoom {

    @objc(addMembersObject:)
    @NSManaged public func addToMembers(_ value: CDMember)

    @objc(removeMembersObject:)
    @NSManaged public func removeFromMembers(_ value: CDMember)

    @objc(addMembers:)
    @NSManaged public func addToMembers(_ values: NSSet)

    @objc(removeMembers:)
    @NSManaged public func removeFromMembers(_ values: NSSet)

}

// MARK: Generated accessors for messages
extension CDRoom {

    @objc(addMessagesObject:)
    @NSManaged public func addToMessages(_ value: CDMessage)

    @objc(removeMessagesObject:)
    @NSManaged public func removeFromMessages(_ value: CDMessage)

    @objc(addMessages:)
    @NSManaged public func addToMessages(_ values: NSSet)

    @objc(removeMessages:)
    @NSManaged public func removeFromMessages(_ values: NSSet)

}

extension CDRoom : Identifiable {

}
