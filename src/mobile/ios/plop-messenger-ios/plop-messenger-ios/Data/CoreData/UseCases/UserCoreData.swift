import CoreData
import RxSwift

final class UserCoreData {
  private let coreDataStack: CoreDataStack
  private var repository: CoreDataRepository<User>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = CoreDataRepository<User>(context: coreDataStack.context)
  }
  
  func fetch(_ email: String) -> Observable<User> {
    return repository.query()
      .map({ users in
        if let user = users.first {
          return user
        }
        throw CoreDataError.failedToFetch
      })
  }
  
  func save(_ user: User) {
    return repository.save(user)
  }
  
  func delete(_ user: User) {
    return repository.delete(user)
  }
}
