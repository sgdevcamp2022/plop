import Foundation
import RxSwift

final class MessageCoreData {
  private let coreDataStack: CoreDataStack
  private var repository: CoreDataRepository<Message>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = CoreDataRepository<Message>(context: coreDataStack.context)
  }
  
  func fetch(roomID: String) -> Observable<[Message]> {
    let predicate = NSPredicate(
      format: "%K = %@",
      #keyPath(CDMessage.roomID),
      roomID
    )
    
    return repository.query(predicate: predicate, sortDescriptors: [
      NSSortDescriptor(keyPath: \CDMessage.createdAt, ascending: false)
    ])
  }
  
  func save(message: Message) {
    return repository.save(message)
  }
  
  func delete(message: Message) {
    return repository.delete(message)
  }
}
