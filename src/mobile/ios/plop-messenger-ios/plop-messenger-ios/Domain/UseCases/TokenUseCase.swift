import Foundation
import RxSwift

final class TokenUseCase {
  private let repository = KeychainRepository.shared
  private let service = "com.plop.token"
  private let account = "user-token"
  
  func fetchAccessToken() -> String? {
    guard let token = fetchToken() else { return nil }
    return token.accessToken
  }
  
  func fetchRefreshToken() -> String? {
    guard let token = fetchToken() else { return nil }
    return token.refreshToken
  }
  
  func updateToken(_ token: Token) {
    try? repository.save(token, service: service, account: account)
  }
  
  func deleteToken(_ token: Token) {
    repository.delete(service: service, account: account)
  }
  
  private func fetchToken() -> Token? {
    do {
      let token = try repository.read(
        service: "com.plop.token",
        account: "user-token",
        type: Token.self
      )
      return token
    } catch {
      return nil
    }
  }
}
