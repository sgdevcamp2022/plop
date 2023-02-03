import Foundation
import RxSwift

extension Reactive where Base: URLSessionWebSocketTask {
  func send(text: String) -> Observable<Void> {
    return Observable.create({ subscribe in
      self.base.send(
        .string(text),
        completionHandler: { error in
          if let error = error {
            subscribe.onError(error)
          }
          subscribe.onNext(())
          subscribe.onCompleted()
        })
      return Disposables.create()
    })
  }
  
  func send(data: Data) -> Observable<Void> {
    return Observable.create({ subscribe in
      self.base.send(
        .data(data),
        completionHandler: { error in
          if let error = error {
            subscribe.onError(error)
          }
          subscribe.onNext(())
          subscribe.onCompleted()
        })
      return Disposables.create {}
    })
  }
  
  //Observable.repeatElemet 사용해서 구독
  func listen() -> Observable<URLSessionWebSocketTask.Message> {
    return Observable.create({ subscribe in
      self.base.receive(completionHandler: { result in
        switch result {
        case .failure(let error):
          subscribe.onError(error)
        case .success(let message):
          subscribe.onNext(message)
          subscribe.onCompleted()
        }
      })
      return Disposables.create{}
    })
  }
}
