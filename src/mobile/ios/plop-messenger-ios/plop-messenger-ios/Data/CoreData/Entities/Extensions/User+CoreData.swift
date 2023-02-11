import Foundation

extension User: Persistable {
  typealias T = CDUser
  
  static var entityName: String {
    return "CDUser"
  }
  
  static var primaryAttributeName: String {
    return "uid"
  }
  
  var identity: String {
    return userID
  }
  
  init(entity: CDUser) {
    userID = entity.uid ?? ""
    email = entity.email ?? ""
    profile = Profile(
      nickname: entity.nickname ?? "",
      imageURL: entity.imageURL ?? "")
  }
  
  func update(_ entity: CDUser) throws {
    entity.uid = userID
    entity.email = email
    entity.nickname = profile.nickname
    entity.imageURL = profile.imageURL
  }
}
