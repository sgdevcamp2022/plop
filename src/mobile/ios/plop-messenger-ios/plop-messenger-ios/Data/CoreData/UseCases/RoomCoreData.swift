import Foundation
import RxSwift

final class RoomCoreData {
  private let coreDataStack: CoreDataStack
  private var repository: CoreDataRepository<Room>
  
  init() {
    coreDataStack = CoreDataStack()
    repository = CoreDataRepository<Room>(context: coreDataStack.context)
  }
  
  func fetch() -> Observable<[Room]> {
    repository.query(predicate: nil, sortDescriptors: [])
  }
  
  func save(room: Room) {
    repository.save(room)
  }
  
  func delete(room: Room) {
    repository.delete(room)
  }
}
