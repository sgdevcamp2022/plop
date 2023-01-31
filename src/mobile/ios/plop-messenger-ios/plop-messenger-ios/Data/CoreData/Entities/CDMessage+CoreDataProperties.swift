//
//  CDMessage+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/31.
//
//

import Foundation
import CoreData


extension CDMessage {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDMessage> {
        return NSFetchRequest<CDMessage>(entityName: "CDMessage")
    }

    @NSManaged public var content: String?
    @NSManaged public var contentType: String?
    @NSManaged public var createdAt: Date?
    @NSManaged public var from: String?
    @NSManaged public var uid: Int64
    @NSManaged public var roomID: Int64
    @NSManaged public var unread: Bool
    @NSManaged public var room: CDRoom?

}

extension CDMessage : Identifiable {

}
