import Foundation

enum RealmRepositoryError: Error {
  case failedToFetch
  case failedToAdd
  case failedToDelete
}
