import Foundation
import CoreData
import RxSwift

extension Reactive where Base: NSManagedObjectContext {
  func entities<T: NSFetchRequestResult>(
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
  
  func save() -> Observable<Void> {
    return Observable.create { observer in
      do {
        try self.base.save()
        observer.onNext(())
      } catch {
        observer.onError(error)
      }
      return Disposables.create()
    }
  }
  
  func delete<T: NSManagedObject>(entity: T) -> Observable<Void> {
    return Observable.create { observer in
      self.base.delete(entity)
      observer.onNext(())
      return Disposables.create()
    }.flatMapLatest {
      self.save()
    }
  }
  
  func first<T: NSFetchRequestResult>(ofType: T.Type = T.self, with predicate: NSPredicate) -> Observable<T?> {
    return Observable.deferred {
      let entityName = String(describing: T.self)
      let request = NSFetchRequest<T>(entityName: entityName)
      request.predicate = predicate
      do {
        let result = try self.base.fetch(request).first
        return Observable.just(result)
      } catch {
        return Observable.error(error)
      }
    }
  }
  
  func sync<C: CoreDataRepresentable, P>(
    entity: C,
    update: @escaping (P) -> Void
  ) -> Observable<P> where C.CoreDataType == P {
    let predicate = NSPredicate(
      format: "%K = %d",
      P.primaryAttributeName,
      entity.uid
    )
    
    print(predicate)
    
    return first(ofType: P.self, with: predicate)
      .flatMap { obj -> Observable<P> in
        let object = obj ?? self.base.create()
        update(object)
        return Observable.just(object)
      }
  }
}

