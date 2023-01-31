import Foundation
import RxSwift

final class CDMessagesUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: Repository<Message>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = Repository<Message>(context: coreDataStack.context)
  }
  
  func fetch(roomID: Int64) -> Observable<[Message]> {
    let predicate = NSPredicate(
      format: "%K = %d",
      #keyPath(Message.CoreDataType.roomID),
      roomID
    )
    
    return repository.query(predicate: predicate, sortDescriptors: [
      NSSortDescriptor(keyPath: \Message.CoreDataType.createdAt, ascending: false)
    ])
  }
  
  func save(message: Message) -> Observable<Void> {
    return repository.save(entity: message)
  }
  
  func delete(message: Message) -> Observable<Void> {
    return repository.delete(entity: message)
  }
}
