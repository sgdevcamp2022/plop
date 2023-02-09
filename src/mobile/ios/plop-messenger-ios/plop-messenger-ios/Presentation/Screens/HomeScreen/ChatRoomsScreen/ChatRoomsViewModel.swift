import Foundation
import RxSwift
import RxCocoa

final class ChatRoomsViewModel: ViewModelType {
  struct Input {
    //TODO: - WebSocket 관련 로직 작성
    
    let fetchRoomsTrigger: Driver<Void>
//    let friendsStateTrigger: Driver<Void>
//    let chatRoomSubscribeTrigger: Driver<Void>
//    let newChatRoomSubscribeTrigger: Driver<Void>
  }
  
  struct Output {
    let roomList: Driver<[RoomListModel]>
  }
  
  private let roomsUseCase = RoomsUseCase()
  
  func transform(_ input: Input) -> Output {
//    let roomList = roomsUseCase.fetchRooms()
//      .asDriverOnErrorJustComplete()
    let roomList = roomsUseCase.mockFetchRooms()
      .asDriverOnErrorJustComplete()
    
    return Output(roomList: roomList)
  }
}
