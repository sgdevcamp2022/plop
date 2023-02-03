import Foundation
import CoreData
import RxSwift


extension CDMember: DomainConvertibleType {
  func toDomain() -> Member {
    return Member(
      uid: uid,
      name: name ?? "",
      image: image ?? ""
    )
  }
}

extension CDMember: Persistable {
  static var entityName: String {
    return "CDMember"
  }
}

extension Member: CoreDataRepresentable {
  typealias CoreDataType = CDMember

  func update(entity: CDMember) {
    entity.uid = uid
    entity.name = name
    entity.image = image
  }
}
