import Foundation
import CoreData
import RxSwift


extension CDMember: DomainConvertibleType {
  func toDomain() -> Member {
    return Member(
      id: id,
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
    entity.id = id
    entity.name = name
    entity.image = image
  }
}
