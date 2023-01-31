import Foundation
import RxSwift

final class CDUsersUseCase {
  private let coreDataStack: CoreDataStack
  private let userRepository: Repository<User>
  
  init() {
    self.coreDataStack = CoreDataStack()
    self.userRepository = Repository<User>(
      context: coreDataStack.context
    )
  }
  
  func save(user: User) -> Observable<Void> {
    return userRepository.save(entity: user)
  }
  
  func fetch(user: User) -> Observable<[User]> {
    let predicate = NSPredicate(
      format: "%K = %d",
      #keyPath(CDUser.uid),
      user.uid
    )
    
    return userRepository
      .query(predicate: predicate, sortDescriptors: [])
  }
  
  func delete(user: User) -> Observable<Void> {
    return userRepository.delete(entity: user)
  }
}
