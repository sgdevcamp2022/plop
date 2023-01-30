//
//  CDMember+CoreDataProperties.swift
//  plop-messenger-ios
//
//  Created by 김호준 on 2023/01/30.
//
//

import Foundation
import CoreData
import UIKit.UIImage


extension CDMember {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CDMember> {
        return NSFetchRequest<CDMember>(entityName: "CDMember")
    }

    @NSManaged public var id: Int64
    @NSManaged public var name: String?
    @NSManaged public var image: UIImage?
    @NSManaged public var room: CDRoom?

}

extension CDMember : Identifiable {

}
