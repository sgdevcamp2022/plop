import Foundation
import CoreData
import RxSwift

extension Reactive where Base: NSManagedObjectContext {
  func entities<T: NSManagedObject>(
    fetchRequest: NSFetchRequest<T>,
    sectionNameKeyPath: String? = nil,
    cacheName: String? = nil
  ) -> Observable<[T]> {
    return Observable.create { observer in
      let observerAdapter = FetchedResultsControllerEntityObserver(
        observer: observer,
        fetchRequest: fetchRequest,
        managedObjectContext: self.base,
        sectionNameKeyPath: sectionNameKeyPath,
        cacheName: cacheName)
      
      return Disposables.create {
        observerAdapter.dispose()
      }
    }
  }
  
  func performUpdate(updateAction: (NSManagedObjectContext) throws -> Void) throws {
    let privateContext = NSManagedObjectContext(
      concurrencyType: .privateQueueConcurrencyType)
    privateContext.parent = self.base
    
    try updateAction(privateContext)
    guard privateContext.hasChanges else { return }
    try privateContext.save()
    try self.base.save()
  }
}

extension Reactive where Base:  NSManagedObjectContext {
  private func create<E: Persistable>(_ type: E.Type = E.self) -> E.T {
    return NSEntityDescription.insertNewObject(
      forEntityName: E.entityName,
      into: self.base) as! E.T
  }
  
  private func get<P: Persistable>(_ persistable: P) throws -> P.T? {
    let fetchRequest: NSFetchRequest<P.T> = NSFetchRequest(entityName: P.entityName)
    fetchRequest.predicate = persistable.predicate()
    
    let result = (try self.base.execute(fetchRequest)) as! NSAsynchronousFetchResult<P.T>
    
    return result.finalResult?.first
  }
  
  func delete<P: Persistable>(_ persistable: P) throws {
    if let entity = try get(persistable) {
      self.base.delete(entity)
      do {
        try entity.managedObjectContext?.save()
      } catch let error {
        throw error
      }
    }
  }
  
  func entities<P: Persistable>(
    _ type: P.Type = P.self,
    predicate: NSPredicate? = nil,
    sortDescriptors: [NSSortDescriptor]? = nil
  ) -> Observable<[P]> {
    let fetchRequest: NSFetchRequest<P.T> = NSFetchRequest(entityName: P.entityName)
    fetchRequest.predicate = predicate
    fetchRequest.sortDescriptors = sortDescriptors ?? [NSSortDescriptor(key: P.primaryAttributeName, ascending: true)]
    return entities(fetchRequest: fetchRequest)
      .map({
        return $0.map(P.init)
      })
  }
  
  func update<P: Persistable>(_ persistable: P) throws {
    try persistable.update(try get(persistable) ?? self.create(P.self))
  }
}
