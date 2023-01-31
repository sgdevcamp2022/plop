import Foundation

final class CDUsersUseCase {
  private let coreDataStack: CoreDataStack
  private let userRepository: Repository<User>
  
  init() {
    self.coreDataStack = CoreDataStack()
    self.userRepository = Repository<User>(
      context: coreDataStack.context
    )
  }
}
