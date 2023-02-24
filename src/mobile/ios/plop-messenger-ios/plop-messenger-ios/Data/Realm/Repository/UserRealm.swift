import Foundation
import RxSwift
import RealmSwift
import RxRealm

final class UserRealm {
  private let realm: Realm
  static let scheduler = RunLoopThreadScheduler(threadName: "com.plop.RealmRepository.Scheduler")

  init() {
    self.realm = try! Realm(
      configuration: .defaultConfiguration
    )
  }
  
  func observeFetchAll() -> Observable<[User]> {
    return Observable.deferred {
      let realm = self.realm
      
      let objects = realm.objects(RMUser.self)
      return Observable.array(from: objects)
        .map({ $0.map({ $0.asDomain() }) })
    }
    .subscribe(on: MainScheduler.instance)
  }
  
  func observeSingleChatRoom(with primaryKey: String) -> Observable<User> {
    return Observable.deferred({
      let realm = self.realm
      guard let object = realm.object(
        ofType: RMUser.self,
        forPrimaryKey: primaryKey
      ) else {
        throw RealmRepositoryError.failedToFetch
      }
      
      return Observable.from(object: object)
        .map({ $0.asDomain() })
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func fetch(with primaryKey: String) -> Observable<User?> {
    return Observable.create({ observer in
      let realm = self.realm
      let object = realm.object(
        ofType: RMUser.self,
        forPrimaryKey: primaryKey
      )
      observer.onNext(object?.asDomain())
      observer.onCompleted()
      return Disposables.create()
    })
    .subscribe(on: MainScheduler.instance)
  }
  
  func save(_ user: User) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      do {
        try realm.write({
          realm.add(user.asRealm(), update: .all)
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
  
  func save(users: [User]) -> Observable<Void> {
    return Observable.create({ observer in
      let realm = self.realm
      do {
        try realm.write({
          users.forEach({
            realm.add($0.asRealm(), update: .all)
          })
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
      guard let user = realm.object(
        ofType: RMUser.self,
        forPrimaryKey: primaryKey
      ) else {
        observer.onError(RealmRepositoryError.failedToDelete)
        return Disposables.create()
      }
      do {
        try realm.write({
          realm.delete(user)
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
