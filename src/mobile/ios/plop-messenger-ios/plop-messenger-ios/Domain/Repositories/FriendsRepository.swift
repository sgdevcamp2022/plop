import Combine
import Foundation

protocol FriendsRepository {
  func fetch() -> AnyPublisher<[Friend], Never>
  func sendRequest(_ id: Int) -> AnyPublisher<Void, Never>
  func cancelRequest(_ id: Int) -> AnyPublisher<Void, Never>
  func delete(_ id: Int) -> AnyPublisher<Void, Never>
}
