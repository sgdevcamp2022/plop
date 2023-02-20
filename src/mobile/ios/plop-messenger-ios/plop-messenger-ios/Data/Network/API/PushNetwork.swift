import Foundation
import RxSwift
import Moya
import RxMoya

final class PushNetwork {
  private let scheduler: ConcurrentDispatchQueueScheduler
  private let keychainService = KeychainService.shared
  
  private lazy var provider: MoyaProvider<PushTarget> = {
    guard let accessToken = keychainService.fetchAccessToken() else {
      return MoyaProvider<PushTarget>()
    }
    let authPlugin = AccessTokenPlugin { _ in accessToken}
    let provider = MoyaProvider<PushTarget>(plugins: [authPlugin])
    return provider
  }()
  
  init() {
    self.scheduler = ConcurrentDispatchQueueScheduler(qos: .background)
  }
  
  func register(_ tokenID: String) -> Observable<Void> {
    return provider.rx.request(.register(tokenID))
      .debug()
      .observe(on: scheduler)
      .asObservable()
      .mapToVoid()
  }
}
