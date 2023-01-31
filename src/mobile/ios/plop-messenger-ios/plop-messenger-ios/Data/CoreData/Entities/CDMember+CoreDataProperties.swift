//
//  CDMember+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/31.
//
//

import Foundation
import CoreData


extension CDMember {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDMember> {
        return NSFetchRequest<CDMember>(entityName: "CDMember")
    }

    @NSManaged public var uid: Int64
    @NSManaged public var image: String?
    @NSManaged public var name: String?
    @NSManaged public var room: CDRoom?

}

extension CDMember : Identifiable {

}
