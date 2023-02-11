import Foundation
import RxSwift

final class MemberCoreData {
  private let coreDataStack: CoreDataStack
  private var repository: CoreDataRepository<Member>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = CoreDataRepository<Member>(context: coreDataStack.context)
  }
  
  func fetch() -> Observable<[Member]> {
    return repository.query(predicate: nil, sortDescriptors: [])
  }
  
  func save(member: Member) {
    return repository.save(member)
  }
  
  func delete(member: Member) {
    return repository.delete(member)
  }
}
