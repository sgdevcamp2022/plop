import Foundation
import RxDataSources
import Differentiator

struct CreateChatRoomSection {
  var headerTitle: String
  var items: [Item]
}

extension CreateChatRoomSection: AnimatableSectionModelType {
  typealias Item = User
  
  var identity: String {
    return headerTitle
  }
  
  init(original: CreateChatRoomSection, items: [User]) {
    self = original
    self.items = items
  }
}
