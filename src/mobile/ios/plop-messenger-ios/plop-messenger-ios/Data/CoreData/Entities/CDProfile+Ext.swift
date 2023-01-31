import CoreData

extension CDProfile: DomainConvertibleType {
  func toDomain() -> Profile {
    return Profile(
      id: id,
      nickname: nickname ?? "",
      image: imageURL ?? ""
    )
  }
}

extension CDProfile: Persistable {
  static var entityName: String {
    return "CDProfile"
  }
}

extension Profile: CoreDataRepresentable {
  typealias CoreDataType = CDProfile
  
  func update(entity: CDProfile) {
    entity.id = id
    entity.nickname = nickname
    entity.imageURL = image
  }
}
