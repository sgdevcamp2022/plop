import Foundation
import RxSwift
import RxCocoa

final class RoomsNetwork {
  //TODO: - RoomID 요청
  private let scheduler: ConcurrentDispatchQueueScheduler
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func createGroupChatRoom(with members: [String], token: String) -> Observable<RoomResponse> {
    let json: [String: Any] = [
      "members" : members
    ]
    do {
      let data = try JSONSerialization.data(withJSONObject: json)
      
      guard var request = NetworkHelper.createRequest(
        path: "/chatting/room/v1/group-creation",
        httpMethod: "POST",
        httpBody: data,
        queries: []) else {
        throw NetworkError.failedToCreateRequest
      }
      request.addValue(token, forHTTPHeaderField: "Authorization")
      
      return URLSession.shared.rx.data(request: request)
        .observe(on: scheduler)
        .map({ data in
          return try JSONDecoder().decode(RoomResponse.self, from: data)
        })
    } catch {
      return Observable.error(error)
    }
  }
  
  func inviteMembers(with members: [String], to roomID: String) -> Observable<RoomInviteResponse> {
    let json: [String: Any] = [
      "room_id" : roomID,
      "members" : members
    ]
    
    do {
      let data = try JSONSerialization.data(withJSONObject: json)
      guard let request = NetworkHelper.createRequest(
        path: "/chatting/room/v1/invitation",
        httpMethod: "POST",
        httpBody: data,
        queries: []) else {
        throw NetworkError.failedToCreateRequest
      }
      
      return URLSession.shared.rx.data(request: request)
        .observe(on: scheduler)
        .map({ data in
          return try JSONDecoder().decode(RoomInviteResponse.self, from: data)
        })
    } catch {
      return Observable.error(error)
    }
  }
  
  func fetchRooms(token: String) -> Observable<RoomsListResponse> {
    guard var request = NetworkHelper.createRequest(
      path: "/chatting/room/v1/my-rooms",
      httpMethod: "GET",
      httpBody: nil,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        return try JSONDecoder().decode(RoomsListResponse.self, from: data)
      })
  }
  
  func leaveRoom(token: String, id: String) -> Observable<Void> {
    guard var request = NetworkHelper.createRequest(
      path: "/chatting/room/v1/out/\(id)",
      httpMethod: "DELETE",
      httpBody: nil,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .mapToVoid()
  }
  
  func fetchUnreadMessages(
    token: String,
    in roomID: String,
    from lastReadMessageID: String
  ) -> Observable<MessageResponse> {
    guard var request = NetworkHelper.createRequest(
      path: "/chatting/room/v1/new-message/\(roomID)/\(lastReadMessageID)",
      httpMethod: "GET",
      httpBody: nil,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    request.addValue(token, forHTTPHeaderField: "Authorization")
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        return try JSONDecoder().decode(MessageResponse.self, from: data)
      })
  }
  
  //TODO: - 채팅 페이지네이션
  
  func fetchRoomInfo(_ id: String) -> Observable<RoomResponse> {
    guard let request = NetworkHelper.createRequest(
      path: "/chatting/room/v1/info/\(id)",
      httpMethod: "GET",
      httpBody: nil,
      queries: []) else {
      return Observable.error(NetworkError.failedToCreateRequest)
    }
    
    return URLSession.shared.rx.data(request: request)
      .observe(on: scheduler)
      .map({ data in
        return try JSONDecoder().decode(RoomResponse.self, from: data)
      })
  }
}
