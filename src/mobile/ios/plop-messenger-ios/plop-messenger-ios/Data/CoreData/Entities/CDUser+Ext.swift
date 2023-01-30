import CoreData
import RxSwift

extension CDUser: DomainConvertibleType {
  func toDomain() -> User {
    return User(
      id: id,
      name: name!,
      email: email!,
      profile: <#T##Profile#>,
      state: User.State(rawValue: state), role: <#T##User.Role#>, device: <#T##Device#>, createdAt: <#T##String#>, updatedAt: <#T##String#>, accessAt: <#T##String#>, loginAt: <#T##String#>)
  }
}

extension CDUser: Persistable {
  static var entityName: String {
    return "CDUser"
  }
  
  
}
