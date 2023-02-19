import Foundation
import Moya

enum UserTarget {
  case fetchUserInfo(_ target: String)
  case updateUserInfo(_ target: String, _ updatedProfile: Profile)
  case search(_ target: String)
  case fetchFriendList
  case requestFriend(_ target: String)
  case cancelRequest(_ target: String)
  case friendRequestList
  case requestSendedList
  case deleteFriend(_ friendID: String)
  case respondToFriendRequest(_ target: String, _ method: Moya.Method)
}

extension UserTarget: TargetType {
  var baseURL: URL {
    return URL(string: "http://3.39.130.186:8000")!
  }
  
  var path: String {
    switch self {
    case .fetchUserInfo(_):
      return "/user/v1/profile"
    case .updateUserInfo(_, _):
      return "/user/v1/profile"
    case .search(_):
      return "/user/v1/search"
    case .fetchFriendList:
      return "/user/v1/friend"
    case .requestFriend(_):
      return "/user/v1/friend/request"
    case .cancelRequest(_):
      return "/user/v1/friend/request"
    case .friendRequestList:
      return "/user/v1/friend/response"
    case .requestSendedList:
      return "/user/v1/friend/request"
    case .deleteFriend(_):
      return "/user/v1/friend"
    case .respondToFriendRequest(_, _):
      return "/user/v1/friend/response"
    }
  }
  
  var method: Moya.Method {
    switch self {
    case .fetchUserInfo(_):
      return .get
    case .updateUserInfo(_, _):
      return .put
    case .search(_):
      return .get
    case .fetchFriendList:
      return .get
    case .requestFriend(_):
      return .post
    case .cancelRequest(_):
      return .delete
    case .friendRequestList:
      return .get
    case .requestSendedList:
      return .get
    case .deleteFriend(_):
      return .delete
    case .respondToFriendRequest(_, let method):
      return method
    }
  }
  
  var task: Moya.Task {
    switch self {
    case .fetchUserInfo(let target):
      return .requestParameters(
        parameters: [
          "target": target
        ],
        encoding: URLEncoding.queryString)
    case .updateUserInfo(let target, let updatedProfile):
      return .requestParameters(
        parameters: [
          "target": target,
          "img": updatedProfile.imageURL ?? "",
          "nickname": updatedProfile.nickname
        ],
        encoding: URLEncoding.queryString)
    case .search(let target):
      return .requestParameters(
        parameters: [
          "target": target
        ],
        encoding: URLEncoding.queryString)
    case .fetchFriendList:
      return .requestPlain
    case .requestFriend(let target):
      return .requestParameters(
        parameters: [
          "target": target
        ],
        encoding: JSONEncoding.default)
    case .cancelRequest(let target):
      return .requestParameters(
        parameters: [
          "target": target
        ],
        encoding: JSONEncoding.default)
    case .friendRequestList:
      return .requestPlain
    case .requestSendedList:
      return .requestPlain
    case .deleteFriend(let friendID):
      return .requestParameters(
        parameters: [
          "friendid": friendID
        ],
        encoding: URLEncoding.queryString)
    case .respondToFriendRequest(let target, _):
      return .requestParameters(
        parameters: [
          "target": target
        ],
        encoding: JSONEncoding.default)
    }
  }
  
  var headers: [String : String]? {
    return [
      "Content-Type": "application/json"
    ]
  }
}

extension UserTarget: AccessTokenAuthorizable {
  var authorizationType: AuthorizationType? {
    switch self {
    default:
      return .bearer
    }
  }
}
