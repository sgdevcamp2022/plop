import Foundation
import CoreData
import RxSwift

final class ContextScheduler: ImmediateSchedulerType {
  private let context: NSManagedObjectContext
  
  init(_ context: NSManagedObjectContext) {
    self.context = context
  }
  
  func schedule<StateType>(_ state: StateType, action: @escaping (StateType) -> Disposable) -> Disposable {
    let disposables = SingleAssignmentDisposable()
    
    context.perform {
      if disposables.isDisposed{ return }
      disposables.setDisposable(action(state))
    }
    return disposables
  }
}
