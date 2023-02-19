import Foundation
import RxSwift

final class ChatRoomUseCase {
  private let network = ChatRoomNetwork()
  private let tokenUseCase = TokenUseCase()
  private let chatRoomRealm = ChatRoomRealm()
  
  func createChatRoom(creator: String, messageTo: String) -> Observable<ChatRoom> {
    return network.createChatRoom(creator: creator, to: messageTo)
      .flatMap({ result in
        switch result {
        case .success(let chatRoom):
          return self.chatRoomRealm.save(chatRoom)
            .map({ return chatRoom })
        case .failure(let error):
          throw error
        }
      })
  }
  
  func createGroupChatRoom(creator: String, with members: [String]) -> Observable<ChatRoom> {
    return network.createGroupChatRoom(creator: creator, members: members)
      .flatMap({ result in
        switch result {
        case .success(let chatRoom):
          return self.chatRoomRealm.save(chatRoom)
            .map({ return chatRoom })
        case .failure(let error):
          throw error
        }
      })
  }
  
  //TODO: - 내부DB에 Member 정보 업데이트 로직
  func invite(members: [String], to room: ChatRoom) -> Observable<Result<Void, Error>> {
    return network.invite(members: members, to: room.roomID)
      .map({ result in
        switch result {
        case .success(_):
          //Realm에 Room 정보 update
          return .success(())
        case .failure(let error):
          return .failure(error)
        }
      })
  }
  
  func fetchChatRooms() -> Observable<Void> {
    return network.fetchChatRooms()
      .flatMap({ [unowned self] result in
        switch result {
        case .success(let rooms):
          return self.chatRoomRealm.save(rooms: rooms)
        case .failure(let error):
          throw error
        }
      })
  }
  
  func leave(room: ChatRoom) -> Observable<Void> {
    return network.leave(room.roomID)
      .flatMap({ [unowned self] in
        return self.chatRoomRealm.delete(room.roomID)
      })
  }
  
  func fetchChatRoomInfo(_ roomID: String) -> Observable<ChatRoom> {
    return network.fetchChatRoomInfo(roomID)
      .flatMap({ result in
        switch result {
        case .success(let room):
          return self.chatRoomRealm.save(room)
            .map({ return room })
        case .failure(let error):
          throw error
        }
      })
  }
  
  func fetchNewMessages(_ roomID: String, from lastMessageID: String) -> Observable<Void> {
    return network.fetchNewMessages(room: roomID, from: lastMessageID)
      .flatMap({ result in
        switch result {
        case .success(let messages):
          return self.chatRoomRealm.save(
            messages: messages,
            in: roomID
          )
        case .failure(let error):
          throw error
        }
      })
  }
  
  func saveLastReadMessage(_ roomID: String, _ userID: String, _ messageID: String) -> Observable<Void> {
    return network.saveLastMessage(roomID, userID, messageID)
      .flatMap({ [unowned self] in
        return self.chatRoomRealm.saveLastReadMessage(
          roomID, userID, messageID)
      })
  }
}

