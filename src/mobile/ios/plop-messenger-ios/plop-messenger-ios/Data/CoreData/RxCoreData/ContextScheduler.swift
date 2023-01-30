import Foundation
import RxSwift
import CoreData

final class ContextScheduler: ImmediateSchedulerType {
  private let context: NSManagedObjectContext
  
  init(context: NSManagedObjectContext) {
    self.context = context
  }
  
  func schedule<StateType>(
    _ state: StateType,
    action: @escaping (StateType) -> RxSwift.Disposable
  ) -> RxSwift.Disposable {
    let disposable = SingleAssignmentDisposable()
    
    context.perform({
      if disposable.isDisposed { return }
      disposable.setDisposable(action(state))
    })
    
    return disposable
  }
}
