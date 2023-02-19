import Foundation

struct MemberData: Decodable {
  let userID: String
  let lastReadMessageID: String?
  let enteredAt: String
  
  enum CodingKeys: String, CodingKey {
    case userID = "userId"
    case lastReadMessageID = "lastReadMsgId"
    case enteredAt
  }
}

extension MemberData {
  func toDomain() -> Member {
    return Member(
      userID: userID,
      lastReadMessageID: lastReadMessageID,
      enteredAt: enteredAt
    )
  }
}
