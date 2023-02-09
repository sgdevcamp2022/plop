import Foundation
import RxSwift
import RxCocoa

final class FriendsUseCase {
  let coreData = CDFriendsUseCase()
  let network = FriendsNetwork()
  
  func fetchFriendsList() {
    
  }
}

