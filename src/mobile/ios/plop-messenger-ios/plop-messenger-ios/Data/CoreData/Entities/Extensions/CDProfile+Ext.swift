import CoreData

extension CDProfile: DomainConvertibleType {
  func toDomain() -> Profile {
    return Profile(
      id: uid,
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
    entity.uid = id
    entity.nickname = nickname
    entity.imageURL = image
  }
}
