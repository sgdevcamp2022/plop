import Foundation
import RxSwift

protocol WebSocketConnection {
  var delegate: WebSocketConnectionDelegate? { get set }
  func send(text: String)
  func send(data: Data)
  func connect() -> Observable<URLSessionWebSocketTask.Message>
  func disconnect()
}
