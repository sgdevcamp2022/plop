import Foundation
import RxSwift

final class CDFriendsUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: Repository<Friend>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = Repository<Friend>(context: coreDataStack.context)
  }
  
  func fetch() -> Observable<[Friend]> {
    return repository.query(predicate: nil, sortDescriptors: [])
  }
  
  func save(friend: Friend) -> Observable<Void> {
    return repository.save(entity: friend)
  }
  
  func delete(friend: Friend) -> Observable<Void> {
    return repository.delete(entity: friend)
  }
}
