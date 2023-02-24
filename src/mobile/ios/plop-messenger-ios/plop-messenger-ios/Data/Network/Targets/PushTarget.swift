import Foundation
import Moya

enum PushTarget {
  case register(_ tokenID: String)
}

extension PushTarget: TargetType {
  var baseURL: URL {
    return URL(string: "http://3.39.130.186:8000")!
  }
  
  var path: String {
    switch self {
    case .register(_):
      return "/push/v1/register"
    }
  }
  
  var method: Moya.Method {
    switch self {
    case .register(_):
      return .post
    }
  }
  
  var task: Moya.Task {
    switch self {
    case .register(let tokenID):
      return .requestParameters(
        parameters: [
          "tokenId": tokenID
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

extension PushTarget: AccessTokenAuthorizable {
  var authorizationType: AuthorizationType? {
    switch self {
    case .register(_):
      return .bearer
    }
  }
}
