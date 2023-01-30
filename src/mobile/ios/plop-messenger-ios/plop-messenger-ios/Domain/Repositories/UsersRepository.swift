import Combine
import Foundation

protocol UsersRepository {
  func fetch()
  func update(_ user: User) -> AnyPublisher<User, Never>
  func delete(_ user: User) -> AnyPublisher<Void, Never>
  func create(_ user: User) -> AnyPublisher<User, Never>
  func search(_ query: String) -> AnyPublisher<User, Never>
}
