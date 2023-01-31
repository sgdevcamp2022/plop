import Foundation
import RxSwift

final class CDProfileUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: Repository<Profile>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = Repository<Profile>(context: coreDataStack.context)
  }
  
  func fetch(id: Int64) -> Observable<[Profile]> {
    let predicate = NSPredicate(format: "%K = %d", #keyPath(CDProfile.uid), id)
    return repository.query(predicate: predicate, sortDescriptors: [])
  }
  
  func save(profile: Profile) -> Observable<Void> {
    return repository.save(entity: profile)
  }
  
  func delete(profile: Profile) -> Observable<Void> {
    return repository.delete(entity: profile)
  }
}
