import Foundation
import Moya

enum AuthTarget {
  case login(LoginRequest)
  case reissue(String)
  case logout
  case signup(SignupRequest)
  case requestVerifyCode(_ email: String, _ userID: String)
  case verifyCode(_ email: String, _ userID: String, _ code: String)
  case withdrawal
}

extension AuthTarget: TargetType {
  var baseURL: URL {
    return URL(string: "http://3.39.130.186:8000")!
  }
  
  var path: String {
    switch self {
    case .login:
      return "/auth/v1/login"
    case .reissue:
      return "/auth/v1/reissue"
    case .logout:
      return "/auth/v1/logout"
    case .signup:
      return "/auth/v1/signup"
    case .requestVerifyCode:
      return "/auth/v1/email/code"
    case .verifyCode:
      return "/auth/v1/email/verify"
    case .withdrawal:
      return "/auth/v1/withdrawal"
    }
  }
  
  var method: Moya.Method {
    switch self {
    case .login:
      return .post
    case .reissue:
      return .post
    case .logout:
      return .delete
    case .signup:
      return .post
    case .requestVerifyCode:
      return .post
    case .verifyCode:
      return .post
    case .withdrawal:
      return .delete
    }
  }
  
  var task: Moya.Task {
    switch self {
    case .login(let request):
      return .requestJSONEncodable(request)
    case .reissue(let email):
      return .requestParameters(
        parameters: ["email": email],
        encoding: JSONEncoding.default)
    case .logout:
      return .requestPlain
    case .signup(let request):
      return .requestJSONEncodable(request)
    case .requestVerifyCode(let email, let userID):
      return .requestParameters(
        parameters: [
          "email": email,
          "userId": userID
        ], encoding: JSONEncoding.default)
    case .verifyCode(let email, let userID, let code):
      return .requestParameters(parameters: [
        "email": email,
        "userId": userID,
        "verificationCode": code
      ], encoding: JSONEncoding.default)
    case .withdrawal:
      return .requestPlain
    }
  }
  
  var headers: [String : String]? {
    return [
      "Content-Type": "application/json"
    ]
  }
}

extension AuthTarget: AccessTokenAuthorizable {
  var authorizationType: AuthorizationType? {
    switch self {
    case .login:
      return .none
    case .reissue:
      return .bearer
    case .logout:
      return .bearer
    case .signup:
      return .none
    case .requestVerifyCode:
      return .bearer
    case .verifyCode:
      return .none
    case .withdrawal:
      return .bearer
    }
  }
}
