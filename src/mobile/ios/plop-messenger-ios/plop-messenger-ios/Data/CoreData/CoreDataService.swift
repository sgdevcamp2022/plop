import CoreData

final class CoreDataService: ServiceProvider {
  private let persistentContainer: NSPersistentContainer
  
  init(name: String) {
    persistentContainer = NSPersistentContainer(name: name)
    persistentContainer.loadPersistentStores(completionHandler: { description, error in
      if let error = error {
        fatalError("Failed to load persistent container : \(error)")
      }
    })
  }
}

extension CoreDataService {
  func save(_ user: User) {
  }
}

