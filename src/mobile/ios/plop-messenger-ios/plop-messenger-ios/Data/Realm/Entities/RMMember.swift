import RealmSwift

final class RMMember: Object {
  @Persisted(primaryKey: true) var userID: String
  @Persisted var enteredAt: String
  @Persisted var lastReadMessageID: String?
  
  convenience init(userID: String, enteredAt: String, lastReadMessageID: String?) {
    self.init()
    
    self.userID = userID
    self.enteredAt = enteredAt
    self.lastReadMessageID = lastReadMessageID
  }
}

extension RMMember: DomainConvertibleType {
  typealias DomainType = Member
  
  var uid: String {
    return userID
  }
  
  func asDomain() -> Member {
    return Member(
      userID: userID,
      lastReadMessageID: lastReadMessageID,
      enteredAt: enteredAt)
  }
}

extension Member: RealmRepresentable {
  typealias RealmType = RMMember
  
  var uid: String {
    return userID
  }
  
  func asRealm() -> RMMember {
    return RMMember.build({ object in
      object.userID = userID
      object.lastReadMessageID = lastReadMessageID
      object.enteredAt = enteredAt
    })
  }
}
