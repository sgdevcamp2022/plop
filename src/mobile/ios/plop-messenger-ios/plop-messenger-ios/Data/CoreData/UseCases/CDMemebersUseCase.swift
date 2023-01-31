import Foundation
import RxSwift

final class CDMemebersUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: Repository<Member>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = Repository<Member>(context: coreDataStack.context)
  }
  
  func fetch() -> Observable<[Member]> {
    return repository.query(predicate: nil, sortDescriptors: [])
  }
  
  func save(member: Member) -> Observable<Void> {
    return repository.save(entity: member)
  }
  
  func delete(member: Member) -> Observable<Void> {
    return repository.delete(entity: member)
  }
}
