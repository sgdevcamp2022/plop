import CoreData
import RxSwift

extension CDUser: DomainConvertibleType {
  func toDomain() -> User {
    return User(
      uid: uid,
      userid: userid ?? "unknown",
      email: email ?? "",
      profile: profile.toDomain(),
      device: device ?? "",
      rooms: (rooms.allObjects as? [CDRoom])?.mapToDomain() ?? [],
      friends: (friends.allObjects as? [CDFriend])?.mapToDomain() ?? []
    )
  }
}

extension CDUser: Persistable {
  static var entityName: String {
    return "CDUser"
  }
  
  static func synced(
    user: CDUser,
    profile: CDProfile,
    rooms: [CDRoom],
    friends: [CDFriend]
  ) -> CDUser {
    user.friends = NSSet(array: friends)
    user.rooms = NSSet(array: rooms)
    user.profile = profile
    return user
  }
}

extension User: CoreDataRepresentable {
  typealias CoreDataType = CDUser
  
  func sync(in context: NSManagedObjectContext) -> Observable<CDUser> {
    let syncSelf = context.rx.sync(
      entity: self,
      update: update)
    
    let syncProfile = profile.sync(in: context)
    
    let roomsObservables = rooms.map({
      $0.sync(in: context)
    })
    let syncRooms = Observable.combineLatest(roomsObservables)
    
    let friendsObservables = friends.map({
      $0.sync(in: context)
    })
    
    let syncFriends = Observable.combineLatest(friendsObservables)
    
    return Observable.zip(
      syncSelf,
      syncProfile,
      syncRooms,
      syncFriends,
      resultSelector: CDUser.synced
    )
  }
  
  func update(entity: CDUser) {
    entity.uid = uid
    entity.userid = userid
    entity.email = email
  }
}
