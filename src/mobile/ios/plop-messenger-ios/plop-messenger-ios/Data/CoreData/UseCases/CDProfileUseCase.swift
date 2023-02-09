import Foundation
import CoreData
import RxSwift

final class CDProfileUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: CoreDataRepository<Profile>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = CoreDataRepository<Profile>(context: coreDataStack.context)
  }
  
  func fetch(_ email: String) -> Observable<Profile?> {
    
    return repository.query()
      .map({ $0.first })
  }
  
  func save(profile: Profile) {
    return repository.save(profile)
  }
  
  func delete(profile: Profile) {
    return repository.delete(profile)
  }
}
