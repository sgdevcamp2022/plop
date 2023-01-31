import Foundation
import RxSwift

final class CDRoomsUseCase {
  private let coreDataStack: CoreDataStack
  private var repository: Repository<Room>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = Repository<Room>(context: coreDataStack.context)
  }
  
  func fetch() -> Observable<[Room]> {
    repository.query(predicate: nil, sortDescriptors: [])
  }
  
  func save(room: Room) -> Observable<Void> {
    repository.save(entity: room)
  }
  
  func delete(room: Room) -> Observable<Void> {
    repository.delete(entity: room)
  }
}
