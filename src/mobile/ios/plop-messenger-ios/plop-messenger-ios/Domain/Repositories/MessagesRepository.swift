import Combine
import Foundation

protocol MessagesRepository {
  func send(_ message: Message) -> AnyPublisher<Message, Never>
  func receive() -> AnyPublisher<Message, Never>
}
