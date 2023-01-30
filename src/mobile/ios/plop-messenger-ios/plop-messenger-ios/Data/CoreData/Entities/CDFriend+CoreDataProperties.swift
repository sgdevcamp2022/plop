//
//  CDFriend+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/30.
//
//

import Foundation
import CoreData
import UIKit.UIImage


extension CDFriend {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDFriend> {
        return NSFetchRequest<CDFriend>(entityName: "CDFriend")
    }

    @NSManaged public var block: Bool
    @NSManaged public var id: Int64
    @NSManaged public var image: UIImage?
    @NSManaged public var name: String?
    @NSManaged public var user: CDUser?

}

extension CDFriend : Identifiable {

}
