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
  }
  
  struct Output {
    let chatRoomList: Driver<[ChatRoom]>
    let finishedFetchChatRoomList: Driver<Void>
    let createChatRoom: Driver<Void>
    let presentChatRoom: Driver<Void>
  }
  
  private let coordinator: ChatRoomListCoordinator
  
  private let usecase = ChatRoomUseCase()
  private let userUsecase = UserUseCase()
  private let chatRoomRealm = ChatRoomRealm()
  private let chatRoomWebsocket: SwiftStomp
  
  private var rooms = [ChatRoom]()
  private let currentUserID = UserDefaults.standard.string(
    forKey: "currentUserID")
  private let disposeBag = DisposeBag()
  
  init(coordinator: ChatRoomListCoordinator) {
    self.coordinator = coordinator
    
    let chatRoomWebsocketURL = URL(
      string: "ws://3.39.130.186:8011/ws-chat")!
    chatRoomWebsocket = SwiftStomp(
      host: chatRoomWebsocketURL,
      headers: [
        "Content-Type": "application/json"
      ])
    chatRoomWebsocket.autoReconnect = true
    chatRoomWebsocket.delegate = self
    chatRoomWebsocket.connect()
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
      .map({ [unowned self] chatRoom in
        self.coordinator.pushChatRoom(
          chatRoom, websocket: self.chatRoomWebsocket)
      })
  
    return Output(
      chatRoomList: chatRoomList,
      finishedFetchChatRoomList: finishedFetchChatRoomList,
      createChatRoom: createChatRoom,
      presentChatRoom: presentChatRoom
    )
  }
}

extension ChatRoomListViewModel: SwiftStompDelegate {
  func onConnect(
    swiftStomp: SwiftStomp,
    connectType: StompConnectType
  ) {
    print("✅ Connected : \(connectType)")
    if connectType == .toStomp {
      rooms.forEach({
        swiftStomp.subscribe(
          to: "/chatting/topic/room/\($0.roomID)")
      })
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
    do {
      guard let message = message as? String else { throw NetworkError.failedToFetchUnreadMessages }
      guard let data = message.data(using: .utf8) else {
        throw NetworkError.failedToLogin
      }
      let msg = try JSONDecoder().decode(MessageRequest.self, from: data)
      
      let messageToSave = msg.toDomain(messageId)
      chatRoomRealm.save(message: messageToSave, in: messageToSave.roomID)
        .subscribe()
        .disposed(by: disposeBag)
      
    } catch {
      print(error)
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
  ) {
    print(eventName)
    print(description)
  }
}
