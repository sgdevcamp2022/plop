import Differentiator

struct ChatRoomListSection {
  var headerTitle: String
  var items: [ChatRoom]
}

extension ChatRoomListSection: AnimatableSectionModelType {
  typealias Item = ChatRoom
  
  var identity: String {
    return headerTitle
  }
  
  init(original: ChatRoomListSection, items: [ChatRoom]) {
    self = original
    self.items = items
  }
}
