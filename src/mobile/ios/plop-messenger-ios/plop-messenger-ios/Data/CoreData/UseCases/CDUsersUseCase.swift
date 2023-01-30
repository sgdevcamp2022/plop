import Foundation

final class CDUsersUseCase {
  private let coreDataStack: CoreDataStack
  private let userRepository: Repository<CDUser>
  
  init() {
    self.coreDataStack = CoreDataStack()
    self.userRepository = Repository<CDUser>(context)
  }
}
