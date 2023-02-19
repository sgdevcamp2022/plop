import Foundation
import Moya

enum ChatRoomTarget {
  case createChatRoom(_ creator: String, _ messageTo: String)
  case createGroupChatRoom(_ creator: String, _ members: [String])
  case fetchChatRoomInfo(_ roomID: String)
  case invite(_ roomID: String, _ members: [String])
  case fetchChatRoomList
  case leave(_ roomID: String)
  case fetchNewMessages(_ roomID: String, _ lastMessageID: String)
  case saveLastMessage(_ roomID: String, _ userID: String, _ messageID: String)
}

extension ChatRoomTarget: TargetType {
  var baseURL: URL {
    return URL(string: "http://3.39.130.186:8000")!
  }
  
  var path: String {
    switch self {
    case .createChatRoom(_, _):
      return "/chatting/room/v1/dm-creation"
    case .createGroupChatRoom(_, _):
      return "/chatting/room/v1/group-creation"
    case .fetchChatRoomInfo(let roomID):
      return "/chatting/room/v1/info/\(roomID)"
    case .invite(_, _):
      return "/chatting/room/v1/invitation"
    case .fetchChatRoomList:
      return "/chatting/room/v1/my-rooms"
    case .leave(let roomID):
      return "/chatting/room/v1/out/\(roomID)"
    case .fetchNewMessages(let roomID, let messageID):
      return "/chatting/room/v1/new-message/\(roomID)/\(messageID)"
    case .saveLastMessage(_, _, _):
      return "/chatting/room/v1/last-message"
    }
  }
  
  var method: Moya.Method {
    switch self {
    case .createChatRoom(_, _):
      return .post
    case .createGroupChatRoom(_, _):
      return .post
    case .fetchChatRoomInfo(_):
      return .get
    case .invite(_, _):
      return .post
    case .fetchChatRoomList:
      return .get
    case .leave(_):
      return .delete
    case .fetchNewMessages(_, _):
      return .get
    case.saveLastMessage(_, _, _):
      return .put
    }
  }
  
  var task: Moya.Task {
    switch self {
    case .createChatRoom(let creator, let messageTo):
      return .requestParameters(
        parameters: [
          "creator": creator,
          "message_to": messageTo],
        encoding: JSONEncoding.default)
    case .createGroupChatRoom(let creator, let members):
      return .requestParameters(
        parameters: [
          "creator": creator,
          "members": members],
        encoding: JSONEncoding.default)
    case .fetchChatRoomInfo(_):
      return .requestPlain
    case .invite(let roomID, let members):
      return .requestParameters(
        parameters: [
          "room_id": roomID,
          "members": members
        ],
        encoding: JSONEncoding.default)
    case .fetchChatRoomList:
      return .requestPlain
    case .leave(_):
      return .requestPlain
    case .fetchNewMessages(_, _):
      return .requestPlain
    case .saveLastMessage(let roomID, let userID, let messageID):
      return .requestParameters(
        parameters: [
          "room_id": roomID,
          "user_id": userID,
          "message_id": messageID
        ],
        encoding: JSONEncoding.default
      )
    }
  }
  
  var headers: [String : String]? {
    return [
      "Content-Type": "application/json",
    ]
  }
}

extension ChatRoomTarget: AccessTokenAuthorizable {
  var authorizationType: AuthorizationType? {
    switch self {
    case .invite(_, _), .fetchNewMessages(_, _), .saveLastMessage(_, _, _):
      return .none
    default:
      return .bearer
    }
  }
}
