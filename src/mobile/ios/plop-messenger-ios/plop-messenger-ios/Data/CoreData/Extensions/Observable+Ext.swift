import Foundation
import RxSwift
import RxCocoa

extension ObservableType {
  func mapToVoid() -> Observable<Void> {
    return map { _ in }
  }
  
  func asDriverOnErrorJustComplete() -> Driver<Element> {
    return asDriver { error in
      return Driver.empty()
    }
  }
  
  func asResult() -> Observable<Result<Element, Error>> {
    return self.map { .success($0) }
      .catch { .just(.failure($0)) }
  }
}
