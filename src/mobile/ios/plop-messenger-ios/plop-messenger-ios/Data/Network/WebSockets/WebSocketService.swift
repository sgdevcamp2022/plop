import Foundation
import RxSwift

enum WebSocketError: Error {
  case unknownError
}

enum WebSocketState {
  case disconnected
  case connected
}

final class WebSocketService: NSObject {
  var session: URLSession!
  var task: URLSessionWebSocketTask!
  let delegateQueue = OperationQueue()
  
  init(_ url: URL) {
    super.init()
    self.session = URLSession(
      configuration: .default,
      delegate: nil,
      delegateQueue: delegateQueue
    )
    self.task = session.webSocketTask(with: url)
  }
  
  func send(text: String) -> Observable<Void> {
    return task.rx.send(text: text)
  }
  
  func send(data: Data) -> Observable<Void> {
    return task.rx.send(data: data)
  }
  
  func connect() -> Observable<URLSessionWebSocketTask.Message> {
    task.resume()
    return listen()
  }
  
  func disconnect() {
    task.cancel(with: .goingAway, reason: nil)
  }
  
  func listen() -> Observable<URLSessionWebSocketTask.Message> {
    return task.rx.listen()
  }
}
