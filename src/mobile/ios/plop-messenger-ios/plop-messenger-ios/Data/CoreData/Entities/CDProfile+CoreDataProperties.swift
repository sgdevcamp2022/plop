//
//  CDProfile+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/31.
//
//

import Foundation
import CoreData


extension CDProfile {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDProfile> {
        return NSFetchRequest<CDProfile>(entityName: "CDProfile")
    }

    @NSManaged public var nickname: String?
    @NSManaged public var imageURL: String?
    @NSManaged public var user: CDUser?

}

extension CDProfile : Identifiable {

}
