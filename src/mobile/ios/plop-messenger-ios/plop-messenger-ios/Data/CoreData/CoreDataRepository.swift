import CoreData
import RxSwift

final class CoreDataRepository<P: Persistable> {
  private let context: NSManagedObjectContext
  private let scheduler: ContextScheduler
  
  init(context: NSManagedObjectContext) {
    self.context = context
    self.scheduler = ContextScheduler(context)
  }
  
  func query(
    predicate: NSPredicate? = nil,
    sortDescriptors: [NSSortDescriptor]? = nil
  ) -> Observable<[P]> {
    return context.rx.entities(
      P.self,
      predicate: predicate,
      sortDescriptors: sortDescriptors
    )
    .subscribe(on: scheduler)
  }
  
  func save(_ entity: P) {
    try? context.rx.update(entity)
  }
  
  func delete(_ entity: P) {
    try? context.rx.delete(entity)
  }
}
