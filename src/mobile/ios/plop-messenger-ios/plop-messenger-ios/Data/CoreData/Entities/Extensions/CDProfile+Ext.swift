import CoreData

extension CDProfile: DomainConvertibleType {
  func toDomain() -> Profile {
    return Profile(
      uid: uid,
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
    entity.uid = uid
    entity.nickname = nickname
    entity.imageURL = image
  }
}
