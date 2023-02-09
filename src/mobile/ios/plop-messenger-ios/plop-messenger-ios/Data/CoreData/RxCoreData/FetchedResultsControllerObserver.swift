import Foundation
import CoreData
import RxSwift

final class FetchedResultsControllerEntityObserver<T: NSManagedObject>: NSObject, NSFetchedResultsControllerDelegate {
  typealias Observer = AnyObserver<[T]>
  
  private let observer: Observer
  private let frc: NSFetchedResultsController<T>
  
  init(
    observer: Observer,
    fetchRequest: NSFetchRequest<T>,
    managedObjectContext context: NSManagedObjectContext,
    sectionNameKeyPath: String?,
    cacheName: String?
  ) {
    self.observer = observer
    self.frc = NSFetchedResultsController(
      fetchRequest: fetchRequest,
      managedObjectContext: context,
      sectionNameKeyPath: sectionNameKeyPath,
      cacheName: cacheName)
    
    super.init()
    
    context.perform {
      self.frc.delegate = self
      
      do {
        try self.frc.performFetch()
        let entities = self.frc.fetchedObjects ?? []
        self.observer.onNext(entities)
      } catch let error {
        observer.on(.error(error))
      }
    }
  }
  
  func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
    let entities = self.frc.fetchedObjects ?? []
    self.observer.onNext(entities)
  }
}

extension FetchedResultsControllerEntityObserver: Disposable {
  func dispose() {
    frc.delegate = nil
  }
}
