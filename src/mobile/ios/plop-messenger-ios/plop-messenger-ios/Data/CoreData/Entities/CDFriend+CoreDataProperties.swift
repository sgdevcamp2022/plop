//
//  CDFriend+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/31.
//
//

import Foundation
import CoreData


extension CDFriend {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDFriend> {
        return NSFetchRequest<CDFriend>(entityName: "CDFriend")
    }

    @NSManaged public var block: Bool
    @NSManaged public var uid: Int64
    @NSManaged public var image: String?
    @NSManaged public var name: String?
    @NSManaged public var user: CDUser?

}

extension CDFriend : Identifiable {

}
