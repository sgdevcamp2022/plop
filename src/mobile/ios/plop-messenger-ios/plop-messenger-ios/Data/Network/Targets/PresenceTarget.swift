import Moya
import Foundation

enum PresenceTarget {
  case fetchOnlineUsers
  case changeStateToOnline
  case changeStateToOffline
}

extension PresenceTarget: TargetType {
  var baseURL: URL {
    return URL(string: "http://3.39.130.186:8000")!
  }
  
  var path: String {
    switch self {
    case .fetchOnlineUsers:
      return "/presence/v1/users"
    case .changeStateToOnline:
      return "/presence/v1/on"
    case .changeStateToOffline:
      return "/presence/v1/off"
    }
  }
  
  var method: Moya.Method {
    switch self {
    case .fetchOnlineUsers:
      return .get
    case .changeStateToOnline:
      return .put
    case .changeStateToOffline:
      return .put
    }
  }
  
  var task: Moya.Task {
    switch self {
    case .fetchOnlineUsers:
      return .requestPlain
    case .changeStateToOnline:
      return .requestPlain
    case .changeStateToOffline:
      return .requestPlain
    }
  }
  
  var headers: [String : String]? {
    return [
      "Content-Type": "application/json"
    ]
  }
}

extension PresenceTarget: AccessTokenAuthorizable {
  var authorizationType: AuthorizationType? {
    switch self {
    default:
      return .bearer
    }
  }
}
