import Foundation
import RxSwift

final class WebSocketUseCase {
  
  private let webSocketService: WebSocketService
  
  init(path: String) {
    self.webSocketService = WebSocketService(path)
  }
  
  func connect<T: Decodable>(_ type: T.Type) -> Observable<(T?, String?)> {
    return webSocketService.connect()
      .map({ message -> (T?, String?) in
        switch message {
        case .data(let data):
          let decodedData = try? JSONDecoder().decode(T.self, from: data)
          return (decodedData, nil)
        case .string(let text):
          return (nil, text)
        @unknown default:
          return (nil, nil)
        }
      })
  }
  
  func sendData<T: Encodable>(_ type: T.Type, model: T) -> Observable<Void> {
    do {
      let data = try JSONEncoder().encode(model)
      return webSocketService.send(data: data)
    } catch {
      return Observable.error(UseCaseError.failedToSendMessage)
    }
  }
  
  func sendText(_ text: String) -> Observable<Void> {
    return webSocketService.send(text: text)
  }
  
  func listen<T: Decodable>(_ type: T.Type) -> Observable<(T?, String?)> {
    return webSocketService.listen()
      .map({ message -> (T?, String?) in
        switch message {
        case .data(let data):
          let decodedData = try? JSONDecoder().decode(T.self, from: data)
          return (decodedData, nil)
        case .string(let text):
          return (nil, text)
        @unknown default:
          return (nil, nil)
        }
      })
  }
  
  func disconnect() {
    webSocketService.disconnect()
  }
}
