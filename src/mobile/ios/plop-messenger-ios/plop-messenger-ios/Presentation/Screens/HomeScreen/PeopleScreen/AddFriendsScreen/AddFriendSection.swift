import Differentiator

struct AddFriendSection {
  var headerTitle: String
  var items: [User]
}

extension AddFriendSection: AnimatableSectionModelType {
  typealias Item = User
  
  var identity: String {
    return headerTitle
  }
  
  init(original: AddFriendSection, items: [User]) {
    self = original
    self.items = items
  }
}
