import Foundation
import SwiftStomp
import RxSwift
import RxCocoa

final class PresenceViewModel: ViewModelType {
  struct Input {
    let connectTrigger: Driver<Void>
    let disconnectTrigger: Driver<Void>
    let fetchOnlineUsersTrigger: Driver<Void>
    let didEnterBackgroundTrigger: Driver<Void>
  }
  
  struct Output {
    let users: Driver<[String]>
    let connected: Driver<Void>
    let disconnected: Driver<Void>
    let onlineUsers: Driver<Void>
    let didEnterBackground: Driver<Void>
  }
  
  private var websocket: SwiftStomp?
  private let tokenUsecase = TokenUseCase()
  private let presenceNetwork = PresenceNetwork()
  private var userID: String = ""
  private let friendsSubject = BehaviorSubject<[String]>(value: [])
  
  init() {
    configureWebsocket()
    userID = UserDefaults.standard.string(forKey: "currentUserID") ?? ""
  }
  
  func transform(_ input: Input) -> Output {
    let users = friendsSubject
      .asDriverOnErrorJustComplete()
    
    let connected = input.connectTrigger
      .map({ [weak websocket] in
        guard let websocket = websocket else { return }
        if !websocket.isConnected {
          websocket.connect()
        }
      })
    
    let disconnected = input.disconnectTrigger
      .map({ [weak websocket] in
        guard let websocket = websocket else { return }
        if websocket.isConnected {
          websocket.disconnect()
        }
      })
    
    let onlineUsers = input.fetchOnlineUsersTrigger
      .flatMap({ [unowned self] in
        return self.presenceNetwork.fetchOnlineUsers()
          .map({ users in
            self.friendsSubject.onNext(users)
          })
          .asDriverOnErrorJustComplete()
      })
    
    let didEnterBackground = input.didEnterBackgroundTrigger
      .map({ [weak websocket] in
        guard let websocket = websocket else { return }
        websocket.disconnect()
      })
    
    return Output(
      users: users,
      connected: connected,
      disconnected: disconnected,
      onlineUsers: onlineUsers,
      didEnterBackground: didEnterBackground
    )
  }
  
  private func configureWebsocket() {
    guard let url = URL(string: "ws://3.39.130.186:8021/ws-presence"),
          let accessToken = tokenUsecase.fetchAccessToken() else {
      return
    }
    
    websocket = SwiftStomp(
      host: url,
      headers: [
        "Authorization": "Bearer \(accessToken)",
        "Content-Type": "application/json"
      ])
    
    websocket?.delegate = self
  }
}

extension PresenceViewModel: SwiftStompDelegate {
  func onConnect(swiftStomp: SwiftStomp, connectType: StompConnectType) {
    guard userID != "",
          connectType == .toStomp else { return }
    
    swiftStomp.subscribe(to: "/presence/user-sub/\(userID)")
  }
  
  func onDisconnect(swiftStomp: SwiftStomp, disconnectType: StompDisconnectType) {
    print("Disconnect!")
  }
  
  func onMessageReceived(swiftStomp: SwiftStomp, message: Any?, messageId: String, destination: String, headers: [String : String]) {
    guard let message = message as? String,
          let data = message.data(using: .utf8),
          var friends = try? friendsSubject.value() else { return }
    print(message)
    do {
      let decodedMessage = try JSONDecoder().decode(PresenceChangeResponse.self, from: data)
      if decodedMessage.status == "offline" {
        if let index = friends.firstIndex(where: { $0 == decodedMessage.userID }) {
          friends.remove(at: index)
          friendsSubject.onNext(friends)
        }
      } else {
        friends.append(decodedMessage.userID)
        friendsSubject.onNext(friends)
      }
    } catch {
      print(error)
    }
  }
  
  func onReceipt(swiftStomp: SwiftStomp, receiptId: String) {}
  
  func onError(swiftStomp: SwiftStomp, briefDescription: String, fullDescription: String?, receiptId: String?, type: StompErrorType) {
    guard let fullDescription = fullDescription else {
      return
    }
    print("❌ : \(fullDescription)")
  }
  
  func onSocketEvent(eventName: String, description: String) {}

}
