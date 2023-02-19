import RealmSwift

final class RMUser: Object {
  @Persisted(primaryKey: true) var userID: String
  @Persisted var email: String
  @Persisted var nickname: String
  @Persisted var state: String
  @Persisted var imageURL: String?
  
  convenience init(userID: String, email: String, nickname: String, imageURL: String?) {
    self.init()
    
    self.userID = userID
    self.email = email
    self.nickname = nickname
    self.imageURL = imageURL
  }
}

extension RMUser: DomainConvertibleType {
  typealias DomainType = User
  
  func asDomain() -> User {
    return User(
      userID: userID,
      email: email,
      state: UserState(rawValue: state) ?? .none,
      profile: Profile(nickname: nickname, imageURL: imageURL)
    )
  }
}

extension User: RealmRepresentable {
  typealias RealmType = RMUser
  
  var uid: String {
    return userID
  }
  
  func asRealm() -> RMUser {
    return RMUser.build({ object in
      object.userID = userID
      object.email = email
      object.state = state.rawValue
      object.nickname = profile.nickname
      object.imageURL = profile.imageURL
    })
  }
}
