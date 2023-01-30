import Foundation
import CoreData

final class CoreDataStack {
  private let storeCoordinator: NSPersistentStoreCoordinator
  let context: NSManagedObjectContext
  
  init() {
    let bundle = Bundle(for: CoreDataStack.self)
    guard let url = bundle.url(forResource: "PlopMessengerModel", withExtension: "momd"),
          let model = NSManagedObjectModel(contentsOf: url) else {
      fatalError("Failed to read model")
    }
    
    self.storeCoordinator = NSPersistentStoreCoordinator(managedObjectModel: model)
    self.context = NSManagedObjectContext(concurrencyType: .privateQueueConcurrencyType)
    self.context.persistentStoreCoordinator = self.storeCoordinator
    self.migrateStore()
  }
  
  private func migrateStore() {
    guard let url = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).last else {
      fatalError("Failed to fetch url")
    }
    
    let storeURL: URL
    
    if #available(iOS 16.0, *) {
      storeURL = url.appending(component: "PlopMessengerModel.sqlite")
    } else {
      storeURL = url.appendingPathComponent("PlopMessengerModel.sqlite")
    }
    
    do {
      try storeCoordinator.addPersistentStore(
        ofType: NSSQLiteStoreType,
        configurationName: nil,
        at: storeURL,
        options: nil)
    } catch {
      fatalError("Error migrating store: \(error)")
    }
  }
}
