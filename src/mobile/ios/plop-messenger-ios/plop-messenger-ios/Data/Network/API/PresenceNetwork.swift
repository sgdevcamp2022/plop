import Foundation
import Moya
import RxSwift
import RxMoya

final class PresenceNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  private let keychainService = KeychainService.shared
  private lazy var provider: MoyaProvider<PresenceTarget> = {
    guard let accessToken = keychainService.fetchAccessToken() else {
      return MoyaProvider<PresenceTarget>()
    }
    let authPlugin = AccessTokenPlugin { _ in accessToken}
    let provider = MoyaProvider<PresenceTarget>(plugins: [authPlugin])
    return provider
  }()
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(
      qos: .background)
  }
  
  func fetchOnlineUsers() -> Observable<[String]> {
    return provider.rx.request(.fetchOnlineUsers)
      .map(PresenceResponse.self)
      .map({
        if let members = $0.members {
          return members
        }
        return []
      })
      .asObservable()
  }
  
  func changeStateToOnline() -> Observable<Void> {
    return provider.rx.request(.changeStateToOnline)
      .asObservable()
      .mapToVoid()
  }
  
  func changeStateToOffline() -> Observable<Void> {
    return provider.rx.request(.changeStateToOffline)
      .subscribe(on: MainScheduler.instance)
      .asObservable()
      .mapToVoid()
  }
}
