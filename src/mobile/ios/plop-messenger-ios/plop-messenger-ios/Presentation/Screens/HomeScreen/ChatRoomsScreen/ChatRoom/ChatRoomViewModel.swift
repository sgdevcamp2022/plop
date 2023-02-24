import Foundation
import SwiftStomp
import RxSwift
import RxCocoa

enum ChatRoomError: Error {
  case failedToCreateChatRoom
}

final class ChatRoomViewModel: ViewModelType {
  //MARK: - Input & Output
  struct Input {
    let createChatRoomTrigger: Driver<ChatRoom>
    let subscribeRoomTrigger: Driver<Void>
    let sendFirstMessageTrigger: Driver<String>
    
    let fetchMessageHistoryTrigger: Driver<Void>
    let fetchNewMessagesTrigger: Driver<String>
    
    let sendTrigger: Driver<String>
    let saveLastMessageTrigger: Driver<String>
  }
  
  struct Output {
    let createdRoom: Driver<ChatRoom>
    let subscribedRoom: Driver<Void>
    let firstMessage: Driver<Void>
    
    let messageHistory: Driver<[Message]>
    let fetchNewMessages: Driver<Void>
    
    let chatRoomListener: Driver<ChatRoom>
    let messageSended: Driver<Void>
    
    let saveLastMessage: Driver<Void>
  }
  
  //MARK: - Properties
  let currentRoom: ChatRoom
  private let usecase = ChatRoomUseCase()
  private let chatRoomRealm = ChatRoomRealm()
  
  private let currentUserEmail: String
  let currentUserID: String
  private let websocket: SwiftStomp?
  
  init(_ currentRoom: ChatRoom, _ websocket: SwiftStomp?) {
    self.currentRoom = currentRoom
    self.websocket = websocket
    
    currentUserEmail = UserDefaults.standard.string(forKey: "currentEmail")!
    currentUserID = UserDefaults.standard.string(forKey: "currentUserID")!
  }
  
  //MARK: - Methods
  func transform(_ input: Input) -> Output {
    let createdRoom = input.createChatRoomTrigger
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
    
    let subscribedRoom = input.subscribeRoomTrigger
      .map({ [weak self, websocket] in
        guard let self = self,
              let websocket = websocket else { return }
        websocket.subscribe(to: "/chatting/topic/room/\(self.currentRoom.roomID)")
      })
    
    let firstMessage = input.sendFirstMessageTrigger
      .map({ _ in () })
    
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
    
    let newMessages = input.fetchNewMessagesTrigger
      .flatMap({ lastMessageID in
        return self.usecase.fetchNewMessages(
          self.currentRoom.roomID,
          from: lastMessageID)
        .asDriverOnErrorJustComplete()
      })
    
    let saveLastMessage = input.saveLastMessageTrigger
      .flatMap({ [unowned self] lastMessageID in
        return self.usecase.saveLastReadMessage(
          currentRoom.roomID,
          currentUserID,
          lastMessageID)
        .asDriverOnErrorJustComplete()
      })
    
    let messageSended = input.sendTrigger
      .flatMap({ [unowned self] content in
        return self.usecase.send(
          to: currentRoom.roomID,
          sender: currentUserID,
          type: "TEXT",
          content: content)
        .asDriverOnErrorJustComplete()
      })
    
    return Output(
      createdRoom: createdRoom,
      subscribedRoom: subscribedRoom,
      firstMessage: firstMessage,
      messageHistory: messageHistory,
      fetchNewMessages: newMessages,
      chatRoomListener: chatRoomListener,
      messageSended: messageSended,
      saveLastMessage: saveLastMessage)
  }
}
