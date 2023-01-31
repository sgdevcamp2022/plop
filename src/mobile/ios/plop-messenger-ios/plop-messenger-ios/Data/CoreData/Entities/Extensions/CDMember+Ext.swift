import Foundation
import CoreData
import RxSwift


extension CDMember: DomainConvertibleType {
  func toDomain() -> Member {
    return Member(
      id: uid,
      name: name ?? "",
      image: image ?? "",
      room: room!.toDomain()
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
    entity.uid = id
    entity.name = name
    entity.image = image
  }
}
