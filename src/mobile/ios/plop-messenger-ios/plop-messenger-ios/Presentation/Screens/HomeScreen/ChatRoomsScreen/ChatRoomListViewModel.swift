import Foundation
import RxSwift
import RxCocoa
import SwiftStomp

final class ChatRoomListViewModel: ViewModelType {
  struct Input {
    //TODO: - WebSocket 관련 로직 작성
    let fetchChatRoomListTrigger: Driver<Void>
    let createChatRoomTrigger: Driver<Void>
    let chatRoomSelectedTrigger: Driver<ChatRoom>
    
    let connectTrigger: Driver<Void>
    let disconnectTrigger: Driver<Void>
  }
  
  struct Output {
    let chatRoomList: Driver<[ChatRoom]>
    let finishedFetchChatRoomList: Driver<Void>
    let createChatRoom: Driver<Void>
    let presentChatRoom: Driver<Void>
    let connected: Driver<Void>
    let disconnected: Driver<Void>
  }
  
  private let coordinator: ChatRoomListCoordinator
  
  private let usecase = ChatRoomUseCase()
  private let userUsecase = UserUseCase()
  private let chatRoomRealm = ChatRoomRealm()
  private var chatRoomWebsocket: SwiftStomp?
  
  private var rooms = [ChatRoom]()
  private let currentUserID = UserDefaults.standard.string(
    forKey: "currentUserID")
  private let disposeBag = DisposeBag()
  
  init(coordinator: ChatRoomListCoordinator) {
    self.coordinator = coordinator
    
    configureWebsocket()
  }

  func transform(_ input: Input) -> Output {
    let chatRoomList = chatRoomRealm.observeFetchAll()
      .map({ [unowned self] rooms in
        self.rooms = rooms
        self.rooms.sort()
        return self.rooms
      })
      .asDriverOnErrorJustComplete()
    
    let finishedFetchChatRoomList = input.fetchChatRoomListTrigger
      .flatMap({ [unowned self] in
        self.usecase.fetchChatRooms()
          .asDriverOnErrorJustComplete()
      })
    
    let createChatRoom = input.createChatRoomTrigger
      .map({ [unowned self] in
        self.coordinator.toCreateChatRoom()
      })
    
    let presentChatRoom = input.chatRoomSelectedTrigger
      .map({ [weak self, chatRoomWebsocket] chatRoom in
        guard let self = self, let chatRoomWebsocket = chatRoomWebsocket else {
          return
        }
        
        self.coordinator.pushChatRoom(
          chatRoom,
          websocket: chatRoomWebsocket)
      })
    
    let connected = input.connectTrigger
      .map({ [weak self] in
        guard let self = self else { return }
        self.connectWebsocket()
      })
    
    let disconnected = input.disconnectTrigger
      .map({ [weak self] in
        guard let self = self else { return }
        self.disconnectWebsocket()
      })
  
    return Output(
      chatRoomList: chatRoomList,
      finishedFetchChatRoomList: finishedFetchChatRoomList,
      createChatRoom: createChatRoom,
      presentChatRoom: presentChatRoom,
      connected: connected,
      disconnected: disconnected
    )
  }
  
  private func configureWebsocket() {
    guard let url = URL(string: URLConstants.chatWebsocketURL) else {
      return
    }
    
    chatRoomWebsocket = SwiftStomp(
      host: url,
      headers: [
        "Content-Type": "application/json"
      ])
    
    guard let chatRoomWebsocket = chatRoomWebsocket else { return }
    chatRoomWebsocket.autoReconnect = true
    chatRoomWebsocket.delegate = self
  }
  
  private func connectWebsocket() {
    guard let chatRoomWebsocket = chatRoomWebsocket else { return }
    if !chatRoomWebsocket.isConnected {
      chatRoomWebsocket.connect()
    }
  }
  
  private func disconnectWebsocket() {
    guard let chatRoomWebsocket = chatRoomWebsocket else { return }
    if chatRoomWebsocket.isConnected {
      chatRoomWebsocket.disconnect()
    }
  }
}

//MARK: - SwiftStompDelegate
extension ChatRoomListViewModel: SwiftStompDelegate {
  func onConnect(
    swiftStomp: SwiftStomp,
    connectType: StompConnectType
  ) {
    guard let userID = UserDefaults.standard.string(forKey: "currentUserID") else { return }

    if connectType == .toStomp {
      rooms.forEach({
        swiftStomp.subscribe(
          to: "/chatting/topic/room/\($0.roomID)")
      })
      swiftStomp.subscribe(to: "/chatting/topic/new-room/\(userID)")
    }
  }
  
  func onDisconnect(
    swiftStomp: SwiftStomp,
    disconnectType: StompDisconnectType
  ) {
    print("Disconnection Type: \(disconnectType)")
  }
  
  func onMessageReceived(
    swiftStomp: SwiftStomp,
    message: Any?,
    messageId: String,
    destination: String,
    headers: [String : String]
  ) {
    guard let message = message as? String,
          let data = message.data(using: .utf8) else { return }
    
    do {
      let messageResponse = try JSONDecoder().decode(WebsocketMessage.self, from: data)
      let domainMessage = messageResponse.toDomain()
      chatRoomRealm.save(message: domainMessage, in: domainMessage.roomID)
        .subscribe()
        .disposed(by: disposeBag)
    } catch {
      return
    }
  }
  
  func onReceipt(
    swiftStomp: SwiftStomp,
    receiptId: String
  ) {}
  
  func onError(
    swiftStomp: SwiftStomp,
    briefDescription: String,
    fullDescription: String?,
    receiptId: String?,
    type: StompErrorType
  ) {}
  
  func onSocketEvent(
    eventName: String,
    description: String
  ) {}
}
