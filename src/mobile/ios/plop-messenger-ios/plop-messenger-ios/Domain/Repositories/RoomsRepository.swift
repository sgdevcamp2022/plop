import Foundation
import Combine

protocol RoomsRepository {
  func fetch() -> AnyPublisher<[Room], Never>
  func connect(_ id: Int) -> AnyPublisher<Room, Never>
  func create(_ membersID: [Int], with title: String?) -> AnyPublisher<Room, Never>
}
