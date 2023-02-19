import Foundation
import RxSwift
import RxCocoa

enum ChatRoomError: Error {
  case failedToCreateChatRoom
}

final class ChatRoomViewModel: ViewModelType {
  //MARK: - Input & Output
  struct Input {
    let fetchMessageHistoryTrigger: Driver<Void>
    let fetchNewMessagesTrigger: Driver<String?>
    let createRoomTrigger: Driver<ChatRoom>
    let sendTrigger: Driver<String>
    let saveLastMessageTrigger: Driver<String>
  }
  
  struct Output {
    let messageHistory: Driver<[Message]>
    let fetchNewMessages: Driver<Void>
    let createRoom: Driver<ChatRoom>
    let chatRoomListener: Driver<ChatRoom>
    let saveLastMessage: Driver<Void>
  }
  
  //MARK: - Properties
  private let currentRoom: ChatRoom
  private let usecase = ChatRoomUseCase()
  private let chatRoomRealm = ChatRoomRealm()
  
  private let currentUserEmail: String
  private let currentUserID: String
  
  init(_ currentRoom: ChatRoom) {
    self.currentRoom = currentRoom
    
    currentUserEmail = UserDefaults.standard.string(forKey: "currentEmail")!
    currentUserID = UserDefaults.standard.string(forKey: "currentUserID")!
  }
  
  //MARK: - Methods
  func transform(_ input: Input) -> Output {
    let chatRoomListener = chatRoomRealm
      .observeSingleChatRoom(with: currentRoom.roomID)
      .asDriverOnErrorJustComplete()
    
    let messageHistory = input.fetchMessageHistoryTrigger
      .flatMap({
        return self.chatRoomRealm.fetch(with: self.currentRoom.roomID)
          .compactMap({ room in
            return room?.messages
          })
          .asDriverOnErrorJustComplete()
      })
    
    let fetchNewMessages = input.fetchNewMessagesTrigger
      .flatMap({ lastMessageID in
        guard let lastMessageID = lastMessageID else {
          return Observable.just(())
            .asDriverOnErrorJustComplete()
        }
        return self.usecase.fetchNewMessages(
          self.currentRoom.roomID,
          from: lastMessageID)
        .asDriverOnErrorJustComplete()
      })
    
    let createRoom = input.createRoomTrigger
      .flatMap({ [unowned self] chatRoom in
        if chatRoom.type == "DM" {
          return self.usecase.createChatRoom(
            creator: currentUserID,
            messageTo: chatRoom.members.first?.userID ?? "")
          .asDriverOnErrorJustComplete()
        } else {
          return self.usecase.createGroupChatRoom(
            creator: currentUserID,
            with: chatRoom.members.map({ $0.userID })
          )
          .asDriverOnErrorJustComplete()
        }
      })
    
    let saveLastMessage = input.saveLastMessageTrigger
      .flatMap({ [unowned self] lastMessageID in
        print(lastMessageID)
        return self.usecase.saveLastReadMessage(
          currentRoom.roomID,
          currentUserID,
          lastMessageID)
        .asDriverOnErrorJustComplete()
      })
    
    return Output(
      messageHistory: messageHistory,
      fetchNewMessages: fetchNewMessages,
      createRoom: createRoom,
      chatRoomListener: chatRoomListener,
      saveLastMessage: saveLastMessage)
  }
}
