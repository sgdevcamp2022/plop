import Foundation
import RxSwift

final class RoomsUseCase {
  private let network = RoomsNetwork()
  private let tokenUseCase = TokenUseCase()
  private let coredata = RoomCoreData()
  
  //MARK: - Network
  func createGroupChatRoom(with members: [String]) -> Observable<Room> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    
    return network.createGroupChatRoom(with: members, token: token)
      .map({ response in
        //TODO: - members 고쳐야함
        let room = Room(
          uid: response.roomID,
          title: response.title,
          lastMessage: "",
          members: [],
          messages: []
        )
        return room
      })
  }
  
  func inviteMembers(with members: [String], to roomID: String) -> Observable<[String]> {
    return network.inviteMembers(with: members, to: roomID)
      .map({ response in
        if response.message == "success" {
          return response.data.members
        } else {
          throw UseCaseError.invalidResponse
        }
      })
  }
  
  func fetchRooms() -> Observable<[RoomListModel]> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    
    return network.fetchRooms(token: token)
      .map({ response in
        if response.message == "success" {
          return response.data.map {
            return RoomListModel(
              roomID: $0.roomID,
              title: $0.title ?? "No title",
              lastMessage: $0.lastMessage.content ?? "")
          }
        } else {
          throw UseCaseError.invalidResponse
        }
      })
  }
  
  func mockFetchRooms() -> Observable<[RoomListModel]> {
    let roomList = [
      RoomListModel(roomID: "71b5354c-6b6c-4cc4-aac9-fea92f3af891",
                    title: "No title",
                    lastMessage: "message254"),
      RoomListModel(roomID: "2ee45163-8f74-4a86-bbda-828291a3e82a",
                    title: "No title",
                    lastMessage: "")
    ]
    
    return Observable.just(roomList)
  }
  
  func leaveRoom(_ id: String) -> Observable<Void> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    return network.leaveRoom(token: token, id: id)
  }
  
  func fetchUnreadMessages(
    in roomID: String,
    from lastReadMessageID: String
  ) -> Observable<[Message]> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    
    return network.fetchUnreadMessages(
      token: token,
      in: roomID,
      from: lastReadMessageID)
    .map({ response in
      return response.data.map({ $0.toDomain() })
    })
  }
  
  func fetchRoomInfo(_ id: String) -> Observable<Room> {
    //TODO: - 뭔가 이상한데?
    return network.fetchRoomInfo(id)
      .map({ $0.toDomain() })
  }
  
  //MARK: - CoreData
  func save(_ room: Room) {
    return coredata.save(room: room)
  }
  
  func fetch() -> Observable<[Room]> {
    return coredata.fetch()
  }
  
  func delete(room: Room) {
    return coredata.delete(room: room)
  }
}

