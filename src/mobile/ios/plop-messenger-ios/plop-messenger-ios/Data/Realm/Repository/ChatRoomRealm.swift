import Foundation
import RxSwift
import RealmSwift
import RxRealm

final class ChatRoomRealm {
  private let realm: Realm
  static let scheduler = RunLoopThreadScheduler(threadName: "com.plop.RealmRepository.Scheduler")
  
  init() {
    self.realm = try! Realm(
      configuration: .defaultConfiguration
    )
  }
  
  func observeFetchAll() -> Observable<[ChatRoom]> {
    return Observable.deferred {
      let realm = self.realm
      
      let objects = realm.objects(RMChatRoom.self)
      return Observable.array(from: objects)
        .map({ $0.map({ $0.asDomain() }) })
    }
    .subscribe(on: MainScheduler.instance)
  }
  
  func observeSingleChatRoom(with primaryKey: String) -> Observable<ChatRoom> {
    return Observable.deferred({
      let realm = self.realm
      guard let object = realm.object(
        ofType: RMChatRoom.self,
        forPrimaryKey: primaryKey
      ) else {
        throw RealmRepositoryError.failedToFetch
      }
      
      return Observable.from(object: object)
        .map({ $0.asDomain() })
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func fetch(with primaryKey: String) -> Observable<ChatRoom?> {
    return Observable.create({ observer in
      let realm = self.realm
      let object = realm.object(
        ofType: RMChatRoom.self,
        forPrimaryKey: primaryKey
      )
      observer.onNext(object?.asDomain())
      observer.onCompleted()
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func save(_ chatRoom: ChatRoom) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      
      if let object = realm.object(
        ofType: RMChatRoom.self,
        forPrimaryKey: chatRoom.roomID) {
        let updatedRoom = chatRoom.asRealm()
        updatedRoom.messages = object.messages
        
        try? realm.write({
          realm.add(updatedRoom, update: .modified)
        })
      } else {
        try? realm.write({
          realm.add(chatRoom.asRealm(), update: .modified)
        })
      }
      observer.onNext(())
      observer.onCompleted()
      
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func save(rooms: [ChatRoom]) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      
      rooms.forEach({ room in
        if let object = realm.object(
          ofType: RMChatRoom.self,
          forPrimaryKey: room.roomID) {
          let updatedRoom = room.asRealm()
          updatedRoom.messages = object.messages
          
          try? realm.write({
            realm.add(updatedRoom, update: .modified)
          })
        } else {
          try? realm.write({
            realm.add(room.asRealm(), update: .modified)
          })
        }
      })
      
      observer.onNext(())
      observer.onCompleted()
      
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func save(messages: [Message], in roomID: String) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      guard let chatRoom = realm.object(
        ofType: RMChatRoom.self,
        forPrimaryKey: roomID) else {
        observer.onError(RealmRepositoryError.failedToAdd)
        return Disposables.create()
      }
      
      let rmMessages = messages.map({ $0.asRealm() })
      
      do {
        try realm.write({
          chatRoom.messages.append(objectsIn: rmMessages)
          realm.add(chatRoom, update: .modified)
        })
        observer.onNext(())
        observer.onCompleted()
      } catch {
        observer.onError(error)
      }
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func save(message: Message, in roomID: String) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      guard let chatRoom = realm.object(
        ofType: RMChatRoom.self,
        forPrimaryKey: roomID) else {
        observer.onError(RealmRepositoryError.failedToAdd)
        return Disposables.create()
      }
      
      let rmMessage = message.asRealm()
      
      do {
        try realm.write({
          chatRoom.messages.append(rmMessage)
          realm.add(chatRoom)
        })
        observer.onNext(())
        observer.onCompleted()
      } catch {
        observer.onError(error)
      }
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func delete(_ primaryKey: String) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      guard let room = realm.object(
        ofType: RMChatRoom.self,
        forPrimaryKey: primaryKey
      ) else {
        observer.onError(RealmRepositoryError.failedToDelete)
        return Disposables.create()
      }
      do {
        try realm.write({
          realm.delete(room)
        })
        observer.onNext(())
        observer.onCompleted()
      } catch {
        observer.onError(error)
      }
      
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func saveLastReadMessage(_ roomID: String, _ userID: String, _ messageID: String) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      guard let object = realm.object(ofType: RMChatRoom.self, forPrimaryKey: roomID),
            let member = object.members.last(where: {
                $0.userID == userID
              }) else {
        observer.onError(RealmRepositoryError.failedToFetch)
        return Disposables.create()
      }
      
      do {
        try realm.write({
          member.lastReadMessageID = messageID
          realm.add(object)
        })
        observer.onNext(())
        observer.onCompleted()
      } catch {
        observer.onError(error)
      }
      
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
}
