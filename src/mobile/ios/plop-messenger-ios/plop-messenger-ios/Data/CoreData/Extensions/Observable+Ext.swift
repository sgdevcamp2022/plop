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

extension Observable where Element: Sequence, Element.Iterator.Element: DomainConvertibleType {
  typealias DomainType = Element.Iterator.Element.DomainType
  
  func mapToDomain() -> Observable<[DomainType]> {
    return map({ sequence in
      return sequence.mapToDomain()
    })
  }
}

extension Sequence where Iterator.Element: DomainConvertibleType {
  typealias Element = Iterator.Element
  func mapToDomain() -> [Element.DomainType] {
    return map { return $0.toDomain() }
  }
}
