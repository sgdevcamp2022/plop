import Foundation
import SwiftStomp

final class ChatRoomWebsocket {
  static let shared = ChatRoomWebsocket()
  
  private let websocket: SwiftStomp
  
  private init() {
    let chatRoomWebsocketURL = URL(string: "ws://3.39.130.186:8011/ws-chat")!
    
    websocket = SwiftStomp(
      host: chatRoomWebsocketURL)
    
    websocket.autoReconnect = true
    websocket.delegate = self
    websocket.connect()
  }
}

extension ChatRoomWebsocket: SwiftStompDelegate {
  func onConnect(swiftStomp: SwiftStomp, connectType: StompConnectType) {
    print("✅ Connected : \(connectType)")
  }
  
  func onDisconnect(swiftStomp: SwiftStomp, disconnectType: StompDisconnectType) {
    print("Disconnection Type: \(disconnectType)")
  }
  
  func onMessageReceived(swiftStomp: SwiftStomp, message: Any?, messageId: String, destination: String, headers: [String : String]) {
    print("Message : \(message)")
  }
  
  func onReceipt(swiftStomp: SwiftStomp, receiptId: String) {
    print("☑️ On Receipt: \(receiptId)")
  }
  
  func onError(swiftStomp: SwiftStomp, briefDescription: String, fullDescription: String?, receiptId: String?, type: StompErrorType) {
    print("Brief description: \(briefDescription)\nFull description: \(fullDescription)\nType: \(type)")
  }
  
  func onSocketEvent(eventName: String, description: String) {
    print("💬 Event name : \(eventName)\n✏️ Description : \(description)")
  }
}
