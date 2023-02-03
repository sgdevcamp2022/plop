import Foundation
import RxSwift

final class RoomsUseCase {
  private let network = RoomsNetwork()
  private let tokenUseCase = TokenUseCase()
  private let roomCoreDataUseCase = CDRoomsUseCase()
  
  //MARK: - Network
  func createGroupChatRoom(with members: [String]) -> Observable<Room> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    
    return network.createGroupChatRoom(with: members, token: token)
      .map({ response in
        //TODO: - members 고쳐야함
        let room = Room(
          uid: Int64(response.roomID) ?? 0,
          title: response.title,
          unreadMessagesCount: 0,
          lastMessage: "",
          lastModified: "",
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
  
  func fetchRooms() -> Observable<[String]> {
    guard let token = tokenUseCase.fetchAccessToken() else {
      return Observable.error(UseCaseError.failedToFetchToken)
    }
    
    return network.fetchRooms(token: token)
      .map({ response in
        if response.message == "success" {
          return response.data.map { $0.roomID }
        } else {
          throw UseCaseError.invalidResponse
        }
      })
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
  func save(_ room: Room) -> Observable<Void> {
    return roomCoreDataUseCase.save(room: room)
  }
  
  func fetch() -> Observable<[Room]> {
    return roomCoreDataUseCase.fetch()
  }
  
  func delete(room: Room) -> Observable<Void> {
    return roomCoreDataUseCase.delete(room: room)
  }
}

