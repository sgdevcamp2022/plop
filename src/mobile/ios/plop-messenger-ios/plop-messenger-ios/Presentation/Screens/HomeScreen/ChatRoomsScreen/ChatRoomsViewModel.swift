import Foundation
import RxSwift
import RxCocoa

final class ChatRoomsViewModel: ViewModelType {
  struct Input {
    //TODO: - WebSocket 관련 로직 작성
    
//    let fetchRoomsTrigger: Driver<Void>
//    let presentCreateChatRoomTrigger: Driver<Void>
//    let friendsStateTrigger: Driver<Void>
//    let chatRoomSubscribeTrigger: Driver<Void>
//    let newChatRoomSubscribeTrigger: Driver<Void>
  }
  
  struct Output {
//    let roomList: Driver<[RoomListModel]>
  }
  
  private let usecase = RoomsUseCase()
  
  func transform(_ input: Input) -> Output {
//    let roomList = input.fetchRoomsTrigger
//      .flatMap({ [unowned self] in
//        usecase.
//      })
    
//    return Output(roomList: roomList)
    return Output()
  }
}
