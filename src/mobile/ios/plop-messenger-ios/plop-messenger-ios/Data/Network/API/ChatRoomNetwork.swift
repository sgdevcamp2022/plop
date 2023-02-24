import Foundation
import RxSwift
import RxCocoa
import Moya
import RxMoya

final class ChatRoomNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  private let keychainService = KeychainService.shared
  private lazy var provider: MoyaProvider<ChatRoomTarget> = {
    guard let token = keychainService.fetchAccessToken() else {
      return MoyaProvider<ChatRoomTarget>()
    }
    
    let authPlugin = AccessTokenPlugin(tokenClosure: { _ in token })
    let provider = MoyaProvider<ChatRoomTarget>(plugins: [authPlugin])
    return provider
  }()
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func createChatRoom(
    creator: String,
    to userID: String
  ) -> Observable<Result<ChatRoom, Error>> {
    return provider.rx.request(.createChatRoom(creator, userID))
      .observe(on: scheduler)
      .map(ChatRoomResponse.self)
      .map({ return $0.toDomain() })
      .asObservable()
      .asResult()
  }
  
  func createGroupChatRoom(
    creator: String,
    members: [String]
  ) -> Observable<Result<ChatRoom, Error>> {
    return provider.rx.request(.createGroupChatRoom(creator, members))
      .observe(on: scheduler)
      .map(ChatRoomResponse.self)
      .map({ return $0.toDomain() })
      .asObservable()
      .asResult()
  }
  
  func invite(members: [String], to roomID: String) -> Observable<Result<Void, Error>> {
    return provider.rx.request(.invite(roomID, members))
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
      .asResult()
  }
  
  func fetchChatRooms() -> Observable<Result<[ChatRoom], Error>> {
    return provider.rx.request(.fetchChatRoomList)
      .observe(on: scheduler)
      .map(ChatRoomListResponse.self)
      .map({ roomListResponse in
        if roomListResponse.message == "success" {
          return (roomListResponse.data ?? []).map { $0.toDomain() }
        } else {
          throw NetworkError.failedToFetchChatRooms
        }
      })
      .asObservable()
      .asResult()
  }
  
  func leave(_ roomID: String) -> Observable<Void> {
    return provider.rx.request(.leave(roomID))
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
  }
  
  func fetchChatRoomInfo(_ roomID: String) -> Observable<Result<ChatRoom, Error>> {
    return provider.rx.request(.fetchChatRoomInfo(roomID))
      .observe(on: scheduler)
      .map(ChatRoomResponse.self)
      .map({ return $0.toDomain() })
      .asObservable()
      .asResult()
  }
  
  func fetchNewMessages(room roomID: String, from lastMessageID: String) -> Observable<Result<[Message], Error>> {
    return provider.rx.request(.fetchNewMessages(roomID, lastMessageID))
      .observe(on: scheduler)
      .map(MessageResponse.self)
      .map({ response in
          return response.data.map { $0.toDomain() }
      })
      .asObservable()
      .asResult()
  }
  
  func saveLastMessage(_ roomID: String, _ userID: String, _ messageID: String) -> Observable<Void> {
    return provider.rx.request(.saveLastMessage(roomID, userID, messageID))
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
  }
  
  func sendMessage(_ message: MessageRequest) -> Observable<Void> {
    return provider.rx.request(.sendMessage(message))
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
  }
}
