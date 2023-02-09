import Foundation
import RxSwift

final class CDFriendsUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: CoreDataRepository<Friend>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = CoreDataRepository<Friend>(context: coreDataStack.context)
  }
  
  func fetch() -> Observable<[Friend]> {
    return repository.query(predicate: nil, sortDescriptors: [])
  }
  
  func save(friend: Friend) {
    return repository.save(friend)
  }
  
  func delete(friend: Friend) {
    return repository.delete(friend)
  }
}
