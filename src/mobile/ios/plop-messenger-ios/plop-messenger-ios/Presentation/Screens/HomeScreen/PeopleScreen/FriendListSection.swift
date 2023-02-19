import Differentiator

struct FriendListSection {
  var headerTitle: String
  var items: [User]
}

extension FriendListSection: AnimatableSectionModelType {
  typealias Item = User
  
  var identity: String {
    return headerTitle
  }
  
  init(original: FriendListSection, items: [User]) {
    self = original
    self.items = items
  }
}
