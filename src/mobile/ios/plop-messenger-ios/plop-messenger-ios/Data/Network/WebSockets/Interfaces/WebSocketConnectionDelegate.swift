import Foundation

protocol WebSocketConnectionDelegate {
  func onConnected(_ connection: WebSocketConnection)
  func onDisconnected(_ connection: WebSocketConnection, error: Error?)
  func onError(_ connection: WebSocketConnection, error: Error)
  func onMessage(_ connection: WebSocketConnection, text: String)
  func onMessage(_ connection: WebSocketConnection, data: Data)
}
