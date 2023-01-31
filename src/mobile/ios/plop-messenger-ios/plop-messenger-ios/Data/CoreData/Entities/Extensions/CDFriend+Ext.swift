import Foundation

extension CDFriend: DomainConvertibleType {
  func toDomain() -> Friend {
    return Friend(
      uid: uid,
      senderID: nil,
      status: .offline,
      block: block,
      imageURL: image,
      name: name ?? ""
    )
  }
}

extension CDFriend: Persistable {
  static var entityName: String {
    return "CDFriend"
  }
}

extension Friend: CoreDataRepresentable {
  typealias CoreDataType = CDFriend
  
  func update(entity: CDFriend) {
    entity.uid = uid
    entity.block = block
    entity.image = imageURL
    entity.name = name
  }
  
}
